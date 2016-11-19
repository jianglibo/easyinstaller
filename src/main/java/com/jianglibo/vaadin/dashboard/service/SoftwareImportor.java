package com.jianglibo.vaadin.dashboard.service;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.jianglibo.vaadin.dashboard.domain.Person;
import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.domain.TextFile;
import com.jianglibo.vaadin.dashboard.init.AppInitializer;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.repositories.SoftwareRepository;
import com.jianglibo.vaadin.dashboard.repositories.TextFileRepository;
import com.jianglibo.vaadin.dashboard.util.SoftwareFolder;
import com.jianglibo.vaadin.dashboard.util.SoftwarePackUtil;
import com.jianglibo.vaadin.dashboard.util.ThrowableUtil;
import com.jianglibo.vaadin.dashboard.vo.FileToUploadVo;
import com.jianglibo.vaadin.dashboard.vo.SoftwareImportResult;

/**
 * 
 * 
 * @author jianglibo@gmail.com
 *
 */
@Component
public class SoftwareImportor {

	private static final Logger LOGGER = LoggerFactory.getLogger(SoftwareImportor.class);

	private int newSoftwareCountAfterLastStart = 0;

	@Autowired
	private SoftwareRepository softwareRepository;

	@Autowired
	private SoftwareDownloader softwareDownloader;

	@Autowired
	private ObjectMapper ymlObjectMapper;
	
	@Autowired
	private TextFileRepository textFileRepository;

	@Autowired
	private PersonRepository personRepository;
	
	
	private void setupTextFiles(final Software sf,final Path baseFolder) {
		Path configfiles = baseFolder.resolve("configfiles");
		if (Files.exists(configfiles) && Files.isDirectory(configfiles)) {
			try (Stream<Path> cfgfs = Files.walk(configfiles)) {
				sf.setTextfiles(cfgfs.filter(Files::isRegularFile).map(f -> {
					try {
						String content = com.google.common.io.Files.toString(f.toFile(), Charsets.UTF_8);
						String name = configfiles.relativize(f).toString().replaceAll("\\\\", "/");
						
						TextFile tf = textFileRepository.findByNameAndSoftware(name, sf);
						if (tf == null) {
							tf = new TextFile(name, content);
							tf.setSoftware(sf);
						} else {
							tf.setContent(content);
						}
						tf = textFileRepository.save(tf);
						return tf;
					} catch (Exception e) {
						return null;
					}
				}).filter(java.util.Objects::nonNull).collect(Collectors.toSet()));
			} catch (Exception e) {
				LOGGER.error("walk {} failed", configfiles.toAbsolutePath().toString());
			}
		}
	}
	
	private List<SoftwareImportResult> decodeFromYaml(Path unpackedFolder) throws IOException {
		try (Stream<Path> fstream = Files.walk(unpackedFolder)) {
			return fstream.filter(p -> {
				return Files.isRegularFile(p) && SoftwareFolder.descriptionyml.equals(p.getFileName().toString());
			}).map(yf -> {
				try {
					Path baseFolder = yf.getParent();
					Software sf = ymlObjectMapper.readValue(Files.newInputStream(yf), Software.class);
					sf.setCodeToExecute(com.google.common.io.Files.toString(baseFolder.resolve(sf.getCodeToExecute()).toFile(), Charsets.UTF_8));
					sf.setConfigContent(com.google.common.io.Files.toString(baseFolder.resolve(sf.getConfigContent()).toFile(), Charsets.UTF_8));
					sf.getFileToUploadVos().stream().filter(FileToUploadVo::isRemoteFile).forEach(vo -> {
						softwareDownloader.submitTasks(vo);
					});
					
					Software sfInDb = softwareRepository.findByNameAndOstypeAndSversion(sf.getName(), sf.getOstype(),sf.getSversion());
					Software newSf;
					
					if (sfInDb == null) {
						Person root = personRepository.findByEmail(AppInitializer.firstEmail);
						sf.setCreator(root);
						setupTextFiles(sf,baseFolder);
						newSf = softwareRepository.save(sf);
					} else {
						sfInDb.copyFrom(sf);
						setupTextFiles(sfInDb, baseFolder);
						newSf = softwareRepository.save(sfInDb);
					}
					return new SoftwareImportResult(newSf);
				} catch (Exception e) {
					return new SoftwareImportResult(ThrowableUtil.printToString(e));
				}
			}).collect(Collectors.toList());
		}
	}
	
	public List<SoftwareImportResult> installSoftwareFromFolder(Path folder) throws IOException {
		return installSoftwareFromFolder(folder, false);
	}
	
	
	public List<SoftwareImportResult> installSoftwareFromFolder(Path folder, boolean removeFolder) throws IOException {
		return decodeFromYaml(folder).stream().map(sir -> {
			if (sir.isSuccess()) {
				Software sfInZip = sir.getSoftware();
				Software sfInDb = softwareRepository.findByNameAndOstypeAndSversion(sfInZip.getName(), sfInZip.getOstype(),sfInZip.getSversion());
				Software newSf;
				if (sfInDb == null) {
					newSf = softwareRepository.save(sfInZip);
				} else {
					sfInDb.copyFrom(sfInZip);
					sfInDb.setTextfiles(
					sfInDb.getTextfiles().stream().map(tf -> {
						tf.setSoftware(sfInDb);
						return textFileRepository.save(tf);
					}).collect(Collectors.toSet()));
					newSf = softwareRepository.save(sfInDb);
				}
				sir.setSoftware(newSf);
			try {
				if (removeFolder) {
					Files.walkFileTree(folder, new SimpleFileVisitor<Path>() {
						@Override
						public FileVisitResult visitFile(Path curFile, BasicFileAttributes bfa) throws IOException {
							Files.deleteIfExists(curFile);
							return FileVisitResult.CONTINUE;
						}

						@Override
						public FileVisitResult postVisitDirectory(Path curPath, IOException arg1) throws IOException {
							Files.deleteIfExists(curPath);
							return FileVisitResult.CONTINUE;
						}
					});
				}
			} catch (IOException e) {
				LOGGER.error("remove folder {} failed", folder.toAbsolutePath().toString());
			}
		}
		return sir;
		}).collect(Collectors.toList());
	}

	public List<SoftwareImportResult> installSoftwareFromZipFile(Path zipFilePath) throws IOException {
		return installSoftwareFromFolder(SoftwarePackUtil.unpack(zipFilePath), true);
	}

	public void setNewSoftwareCountAfterLastStart(int newSoftwareCountAfterLastStart) {
		this.newSoftwareCountAfterLastStart = newSoftwareCountAfterLastStart;
	}

	public int getNewSoftwareCountAfterLastStart() {
		return newSoftwareCountAfterLastStart;
	}
	
//	protected boolean processOneSoftware(HttpPageGetter httpPageGetter, SoftwarelistLine sl) {
//	if (!sl.zipFileExistsAndMd5Right()) {
//		httpPageGetter.getFile(sl.getUrl(), sl.getZipFilePath());
//	}
//
//	String[] ss = sl.getFnSegs();
//	Path unpackedFolder = null;
//
//	unpackedFolder = SoftwarePackUtil.unpack(sl.getZipFilePath());
//	Software sf = softwareRepository.findByNameAndOstypeAndSversion(ss[0], ss[1], ss[2]);
//	try {
//		Software vo = decodeFromYaml(unpackedFolder, ss);
//
//		if (sf == null) {
//			softwareRepository.save(vo);
//		} else {
//			sf.copyFrom(vo);
//			softwareRepository.save(sf);
//		}
//
//		return true;
//	} catch (Exception e) {
//		e.printStackTrace();
//	} finally {
//		if (unpackedFolder != null) {
//			try {
//				Files.walkFileTree(unpackedFolder, new SimpleFileVisitor<Path>() {
//					@Override
//					public FileVisitResult visitFile(Path curFile, BasicFileAttributes bfa) throws IOException {
//						Files.deleteIfExists(curFile);
//						return FileVisitResult.CONTINUE;
//					}
//
//					@Override
//					public FileVisitResult postVisitDirectory(Path curPath, IOException arg1) throws IOException {
//						Files.deleteIfExists(curPath);
//						return FileVisitResult.CONTINUE;
//					}
//				});
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//	return false;
//}
	
	// ten minutes.
//	@Scheduled(initialDelay = 1000, fixedDelay = 600000)
//	public void fetchSoftwareLists() {
//		final String urlBase = "https://raw.githubusercontent.com/jianglibo/easyinstaller/master/softwares/";
//
//		String listfn = "softwarelist.txt";
//		final Path sfFolder = applicationConfig.getSoftwareFolderPath();
//
//		if (!Files.exists(sfFolder)) {
//			try {
//				Files.createDirectories(applicationConfig.getSoftwareFolderPath());
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//		LOGGER.info("start fetching from {}", urlBase + listfn);
//		String snLines = httpPageGetter.getPage(urlBase + listfn);
//		
//		int retry = 3;
//		
//		while (retry > 0) {
//			if (!snLines.isEmpty()) {
//				break;
//			} else {
//				snLines = httpPageGetter.getPage(urlBase + listfn);
//			}
//			retry--;
//		}
//
//		try {
//			List<String> lines = CharStreams.readLines(new StringReader(snLines));
//
//			Files.write(sfFolder.resolve(listfn), lines);
//
//			newSoftwareCountAfterLastStart += lines.stream().map(line -> new SoftwarelistLine(urlBase, sfFolder, line))
//					.filter(sl -> {
//						String[] ss = sl.getFnSegs();
//						if (ss.length != 3) {
//							return false;
//						}
//						Software sw = softwareRepository.findByNameAndOstypeAndSversion(ss[0], ss[1], ss[2]);
//						boolean indb = false;
//						if (sw != null) {
//							makeSureRemoteFileDownloaded(sw);
//							indb = true;
//						}
//						boolean changed = sl.changed();
//						return !indb || changed; // indb and not changed, but
//													// file not downloaded.
//					}).map(sl -> {
//						return processOneSoftware(httpPageGetter, sl);
//					}).mapToInt(b -> b ? 1 : 0).sum();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

//	private void makeSureRemoteFileDownloaded(Software sw) {
//		sw.getFileToUploadVos().forEach(vo -> {
//			softwareDownloader.submitTasks(vo);
//		});
//	}

//	protected static class SoftwarelistLine {
//		private final String fn;
//		private final String md5;
//
//		private final Path softwareFolderPath;
//
//		private String baseUrl;
//
//		public SoftwarelistLine(String baseUrl, Path softwareFolderPath, String line) {
//			this.softwareFolderPath = softwareFolderPath;
//			this.baseUrl = baseUrl;
//			int idx = line.lastIndexOf(',');
//			this.fn = line.substring(0, idx);
//			this.md5 = line.substring(idx + 1);
//		}
//
//		@Override
//		public String toString() {
//			return Objects.toStringHelper(this).add("fn", getFn()).add("md5", getMd5())
//					.add("zipFilePath", getZipFilePath()).toString();
//		}
//
//		public boolean zipFileExistsAndMd5Right() {
//			return Files.exists(getZipFilePath()) && !changed();
//		}
//
//		public String[] getFnSegs() {
//			String sn = getFn();
//			int idx = sn.lastIndexOf('.');
//			String snnoext = sn.substring(0, idx);
//			return snnoext.split("--");
//		}
//
//		public Path getZipFilePath() {
//			return softwareFolderPath.resolve(fn);
//		}
//
//		public String getUrl() {
//			return baseUrl + getFn();
//		}
//
//		public String getFn() {
//			return fn;
//		}
//
//		public String getMd5() {
//			return md5;
//		}
//
//		public boolean changed() {
//			try {
//				return !md5.equals(com.google.common.io.Files
//						.hash(softwareFolderPath.resolve(fn).toFile(), Hashing.md5()).toString());
//			} catch (IOException e) {
//				return false;
//			}
//		}
//	}

//	@Scheduled(fixedRate = 10000)
//	public void broadcastNewSoftware() {
//		Broadcaster.broadcast(new BroadCasterMessage(new NewSoftwareMessage(newSoftwareCountAfterLastStart)));
//	}
//
//	public static class NewSoftwareMessage implements BroadCasterMessageBody {
//		private int newSoftwareCountAfterLastStart;
//
//		public NewSoftwareMessage(int newSoftwareCountAfterLastStart) {
//			super();
//			this.newSoftwareCountAfterLastStart = newSoftwareCountAfterLastStart;
//		}
//
//		public int getNewSoftwareCountAfterLastStart() {
//			return newSoftwareCountAfterLastStart;
//		}
//
//		public void setNewSoftwareCountAfterLastStart(int newSoftwareCountAfterLastStart) {
//			this.newSoftwareCountAfterLastStart = newSoftwareCountAfterLastStart;
//		}
//
//		@Override
//		public BroadCasterMessageType getBroadCasterMessageType() {
//			return BroadCasterMessageType.NEW_SOFTWARE;
//		}
//	}

//	private Software decodeFromYaml(Path unpackedFolder, String[] ss) throws IOException {
//		Software sf = null;
//		sf = ymlObjectMapper.readValue(Files.newInputStream(unpackedFolder.resolve(SoftwareFolder.descriptionyml)), Software.class);
//		sf.setName(ss[0]);
//		sf.setOstype(ss[1]);
//		sf.setSversion(ss[2]);
//		StringBuffer bf = new StringBuffer();
//		com.google.common.io.Files.asCharSource(unpackedFolder.resolve(sf.getCodeToExecute()).toFile(), Charsets.UTF_8)
//				.copyTo(bf);
//		sf.setCodeToExecute(bf.toString());
//
//		bf = new StringBuffer();
//		com.google.common.io.Files.asCharSource(unpackedFolder.resolve(sf.getConfigContent()).toFile(), Charsets.UTF_8)
//				.copyTo(bf);
//		sf.setConfigContent(bf.toString());
//
//		sf.getFileToUploadVos().stream().filter(FileToUploadVo::isRemoteFile).forEach(vo -> {
//			softwareDownloader.submitTasks(vo);
//		});
//
//		Person root = personRepository.findByEmail(AppInitializer.firstEmail);
//		sf.setCreator(root);
//		return sf;
//	}
}
