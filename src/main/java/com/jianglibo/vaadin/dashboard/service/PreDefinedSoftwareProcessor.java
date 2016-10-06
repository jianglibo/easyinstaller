package com.jianglibo.vaadin.dashboard.service;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.hash.Hashing;
import com.google.common.io.CharStreams;
import com.jianglibo.vaadin.dashboard.Broadcaster;
import com.jianglibo.vaadin.dashboard.Broadcaster.BroadCasterMessage;
import com.jianglibo.vaadin.dashboard.config.ApplicationConfig;
import com.jianglibo.vaadin.dashboard.domain.Person;
import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.init.AppInitializer;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.repositories.SoftwareRepository;
import com.jianglibo.vaadin.dashboard.util.SoftwarePackUtil;
import com.jianglibo.vaadin.dashboard.vo.FileToUploadVo;

@Component
public class PreDefinedSoftwareProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(PreDefinedSoftwareProcessor.class);

	private int newSoftwareCountAfterLastStart = 0;

	@Autowired
	private SoftwareRepository softwareRepository;

	@Autowired
	private SoftwareDownloader softwareDownloader;

	@Autowired
	private ApplicationConfig applicationConfig;

	@Autowired
	private HttpPageGetter httpPageGetter;

	@Autowired
	private ObjectMapper ymlObjectMapper;

	@Autowired
	private PersonRepository personRepository;

	// ten minutes.
	@Scheduled(initialDelay = 1000, fixedDelay = 600000)
	public void fetchSoftwareLists() {
		final String urlBase = "https://raw.githubusercontent.com/jianglibo/easyinstaller/master/softwares/";

		String listfn = "softwarelist.txt";
		final Path sfFolder = applicationConfig.getSoftwareFolderPath();

		if (!Files.exists(sfFolder)) {
			try {
				Files.createDirectories(applicationConfig.getSoftwareFolderPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		LOGGER.info("start fetching from {}", urlBase + listfn);
		String snLines = httpPageGetter.getPage(urlBase + listfn);

		try {
			List<String> lines = CharStreams.readLines(new StringReader(snLines));

			Files.write(sfFolder.resolve(listfn), lines);

			newSoftwareCountAfterLastStart += lines.stream().map(line -> new SoftwarelistLine(urlBase, sfFolder, line))
					.filter(SoftwarelistLine::changed).map(sl -> {
						return processOneSoftware(httpPageGetter, sl);
					}).mapToInt(b -> b ? 1 : 0).sum();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected static class SoftwarelistLine {
		private final String fn;
		private final String md5;

		private final Path softwareFolderPath;

		private String baseUrl;

		public SoftwarelistLine(String baseUrl, Path softwareFolderPath, String line) {
			this.softwareFolderPath = softwareFolderPath;
			int idx = line.lastIndexOf(',');
			this.fn = line.substring(0, idx);
			this.md5 = line.substring(idx + 1);
		}

		@Override
		public String toString() {
			return Objects.toStringHelper(this).add("fn", getFn()).add("md5", getMd5())
					.add("zipFilePath", getZipFilePath()).toString();
		}

		public boolean zipFileExistsAndMd5Right() {
			return Files.exists(getZipFilePath()) && !changed();
		}

		public String[] getFnSegs() {
			String sn = getFn();
			int idx = sn.lastIndexOf('.');
			String snnoext = sn.substring(0, idx);
			return snnoext.split("--");
		}

		public Path getZipFilePath() {
			return softwareFolderPath.resolve(fn);
		}

		public String getUrl() {
			return baseUrl + getFn();
		}

		public String getFn() {
			return fn;
		}

		public String getMd5() {
			return md5;
		}

		public boolean changed() {
			try {
				return !md5.equals(com.google.common.io.Files
						.hash(softwareFolderPath.resolve(fn).toFile(), Hashing.md5()).toString());
			} catch (IOException e) {
				return false;
			}
		}
	}

	@Scheduled(fixedRate = 10000)
	public void broadcastNewSoftware() {
		Broadcaster.broadcast(new BroadCasterMessage(new NewSoftwareMessage(newSoftwareCountAfterLastStart),
				Broadcaster.BroadCasterMessageType.NEW_SOFTWARE));
	}

	public static class NewSoftwareMessage {
		private int newSoftwareCountAfterLastStart;

		public NewSoftwareMessage(int newSoftwareCountAfterLastStart) {
			super();
			this.newSoftwareCountAfterLastStart = newSoftwareCountAfterLastStart;
		}

		public int getNewSoftwareCountAfterLastStart() {
			return newSoftwareCountAfterLastStart;
		}

		public void setNewSoftwareCountAfterLastStart(int newSoftwareCountAfterLastStart) {
			this.newSoftwareCountAfterLastStart = newSoftwareCountAfterLastStart;
		}
	}

	private Software decodeFromYaml(Path unpackedFolder, String[] ss) throws IOException {
		Software sf = null;
		sf = ymlObjectMapper.readValue(Files.newInputStream(unpackedFolder.resolve("description.yml")), Software.class);
		sf.setName(ss[0]);
		sf.setOstype(ss[1]);
		sf.setSversion(ss[2]);
		StringBuffer bf = new StringBuffer();
		com.google.common.io.Files.asCharSource(unpackedFolder.resolve(sf.getCodeToExecute()).toFile(), Charsets.UTF_8)
				.copyTo(bf);
		sf.setCodeToExecute(bf.toString());

		bf = new StringBuffer();
		com.google.common.io.Files.asCharSource(unpackedFolder.resolve(sf.getConfigContent()).toFile(), Charsets.UTF_8)
				.copyTo(bf);
		sf.setConfigContent(bf.toString());

		sf.getFileToUploadVos().stream().filter(FileToUploadVo::isRemoteFile).forEach(vo -> {
			softwareDownloader.submitTasks(vo);
		});

		Person root = personRepository.findByEmail(AppInitializer.firstEmail);
		sf.setCreator(root);
		return sf;
	}

	protected boolean processOneSoftware(HttpPageGetter httpPageGetter, SoftwarelistLine sl) {
		if (sl.zipFileExistsAndMd5Right()) {
			return false;
		} else {
			httpPageGetter.getFile(sl.getUrl(), sl.getZipFilePath());
		}

		String[] ss = sl.getFnSegs();
		Path unpackedFolder = null;

		if (ss.length == 3) {
			unpackedFolder = SoftwarePackUtil.unpack(sl.getZipFilePath());
			Software sf = softwareRepository.findByNameAndOstypeAndSversion(ss[0], ss[1], ss[2]);
			try {
				Software vo = decodeFromYaml(unpackedFolder, ss);
				
				if (sf == null) {
					softwareRepository.save(vo);
				} else {
					sf.copyFrom(vo);
					softwareRepository.save(sf);
				}
				
				
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (unpackedFolder != null) {
					try {
						Files.walkFileTree(unpackedFolder, new SimpleFileVisitor<Path>() {
							@Override
							public FileVisitResult visitFile(Path curFile, BasicFileAttributes bfa) throws IOException {
								Files.deleteIfExists(curFile);
								return FileVisitResult.CONTINUE;
							}

							@Override
							public FileVisitResult postVisitDirectory(Path curPath, IOException arg1)
									throws IOException {
								Files.deleteIfExists(curPath);
								return FileVisitResult.CONTINUE;
							}
						});
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return false;
	}

	public void setNewSoftwareCountAfterLastStart(int newSoftwareCountAfterLastStart) {
		this.newSoftwareCountAfterLastStart = newSoftwareCountAfterLastStart;
	}

	public int getNewSoftwareCountAfterLastStart() {
		return newSoftwareCountAfterLastStart;
	}

}
