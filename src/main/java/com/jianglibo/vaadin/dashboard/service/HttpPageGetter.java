package com.jianglibo.vaadin.dashboard.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.ByteStreams;
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

@Component
public class HttpPageGetter {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpPageGetter.class);
	
	private int newSoftwareCountAfterLastStart = 0;
	
	private int fetchNewsCount = 0;
	
	private List<NewNew> allNews = Lists.newArrayList();

	@Autowired
	private ApplicationConfig applicationConfig;

	@Autowired
	private SoftwareRepository softwareRepository;
	
	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private ObjectMapper ymlObjectMapper;

	public String getPage(String url) {
		return getPage(url, Charsets.UTF_8);
	}

	public void getFile(String url, Path target) {
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			CloseableHttpResponse response = null;
			try {
				Path tmpFile = Files.createTempFile(HttpPageGetter.class.getName(), "");
				HttpGet httpget = new HttpGet(url);
				response = httpclient.execute(httpget);
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream instream = entity.getContent();
					OutputStream outstream = Files.newOutputStream(tmpFile);
					try {
						ByteStreams.copy(instream, outstream);
						instream.close();
						outstream.flush();
						outstream.close();
						Files.move(tmpFile, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
					} finally {

					}

				}
			} catch (IOException e) {
			} finally {
				if (response != null) {
					try {
						response.close();
					} catch (IOException e) {
					}
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public String getPage(String url, Charset cs) {
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			CloseableHttpResponse response = null;
			try {
				HttpGet httpget = new HttpGet(url);
				response = httpclient.execute(httpget);
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream instream = entity.getContent();
					try {
						InputStreamReader isr = new InputStreamReader(instream, cs);
						return CharStreams.toString(isr);
					} finally {
						instream.close();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (response != null) {
					try {
						response.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return "";
	}

	protected boolean processOneSoftware(final Path sfFolder, String sn) {
		// tcl--centos7--1.zip
		int idx = sn.lastIndexOf('.');
		String snnoext = sn.substring(0, idx);
		String[] ss = snnoext.split("--");
		if (ss.length == 3) {
			Software sf = softwareRepository.findByNameAndOstypeAndSversion(ss[0], ss[1], ss[2]);
			if (sf == null) {
				Path unpackedFolder = SoftwarePackUtil.unpack(sfFolder.resolve(sn));
				try {
					sf = ymlObjectMapper.readValue(Files.newInputStream(unpackedFolder.resolve("description.yml")),
							Software.class);
					sf.setName(ss[0]);
					sf.setOstype(ss[1]);
					sf.setSversion(ss[2]);
					StringBuffer bf = new StringBuffer();
					com.google.common.io.Files
							.asCharSource(unpackedFolder.resolve(sf.getCodeToExecute()).toFile(), Charsets.UTF_8)
							.copyTo(bf);
					sf.setCodeToExecute(bf.toString());

					bf = new StringBuffer();
					com.google.common.io.Files
							.asCharSource(unpackedFolder.resolve(sf.getConfigContent()).toFile(), Charsets.UTF_8)
							.copyTo(bf);
					sf.setConfigContent(bf.toString());

					Path filesToUploadPath = unpackedFolder.resolve("filesToUpload");

					Map<String, List<String>> fntypemap = Files.list(filesToUploadPath).map(Path::getFileName)
							.map(Path::toString).collect(Collectors.groupingBy(fn -> fn.endsWith(".placeholder") ? "placeholder" : "real"));

					List<String> phfn = fntypemap.containsKey("placeholder") ? fntypemap.get("placeholder").stream().map(fn -> fn.substring(0, fn.length() - 12))
							.collect(Collectors.toList()) : Lists.newArrayList();

					if (fntypemap.containsKey("real")) {
						phfn.addAll(fntypemap.get("real"));
					}
					sf.setFilesToUpload(Sets.newHashSet(phfn));
					
					Person root = personRepository.findByEmail(AppInitializer.firstEmail);
					sf.setCreator(root);
					softwareRepository.save(sf);
					return true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return false;
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
	
	public static class NewNewsMessage {
		private List<NewNew> newNews;
		
		private int fetchCount;

		public NewNewsMessage(List<NewNew> newNews, int fetchCount) {
			super();
			this.newNews = newNews;
			this.fetchCount = fetchCount;
		}

		public int getFetchCount() {
			return fetchCount;
		}

		public void setFetchCount(int fetchCount) {
			this.fetchCount = fetchCount;
		}

		public List<NewNew> getNewNews() {
			return newNews;
		}

		public void setNewNews(List<NewNew> newNews) {
			this.newNews = newNews;
		}
	}
	
	/**
	 * for every 5 seconds, report the new software inserted count from start of application.
	 */
	@Scheduled(fixedRate = 10000)
	public void broadcastNewSoftware() {
		Broadcaster.broadcast(new BroadCasterMessage(new NewSoftwareMessage(newSoftwareCountAfterLastStart), Broadcaster.BroadCasterMessageType.NEW_SOFTWARE));
		Broadcaster.broadcast(new BroadCasterMessage(new NewNewsMessage(getAllNews(), fetchNewsCount), Broadcaster.BroadCasterMessageType.NEW_NEWS));
	}

	// ten minutes.
	@Scheduled(initialDelay=1000, fixedDelay=600000)
	public void fetchSoftwareLists() {
		String urlBase = "https://raw.githubusercontent.com/jianglibo/easyinstaller/master/softwares/";
		String listfn = "softwarelist.txt";
		final Path sfFolder = applicationConfig.getSoftwareFolderPath();
		if (!Files.exists(sfFolder)) {
			try {
				Files.createDirectories(applicationConfig.getSoftwareFolderPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			LOGGER.info("start fetching from {}", urlBase + listfn);
			String snLines = getPage(urlBase + listfn);
			Files.write(applicationConfig.getSoftwareFolderPath().resolve(listfn), snLines.getBytes(Charsets.UTF_8));
			newSoftwareCountAfterLastStart += CharStreams.readLines(new StringReader(snLines)).stream().map(sn -> {
				Path targetZipFile = sfFolder.resolve(sn); 
				if (!Files.exists(targetZipFile)) {
					getFile(urlBase + sn, targetZipFile);
				}
				if (Files.exists(targetZipFile)) {
					return processOneSoftware(sfFolder, sn);
				} else {
					return false;
				}
			}).mapToInt(b -> b ? 1 : 0).sum();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Scheduled(initialDelay=1000, fixedDelay=600000)
	public void fetchNews() {
		String s = getPage("https://raw.githubusercontent.com/jianglibo/first-vaadin/master/wiki/news/newslist.txt");
		try {
			setAllNews(CharStreams.readLines(new StringReader(s)).stream().map(line -> line.split("\\s+", 3))
					.map(ft -> new NewNew(ft[2], ft[1],
							"https://github.com/jianglibo/first-vaadin/tree/master/wiki/news/" + ft[0]))
					.collect(Collectors.toList()));
		fetchNewsCount++;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setNewSoftwareCountAfterLastStart(int newSoftwareCountAfterLastStart) {
		this.newSoftwareCountAfterLastStart = newSoftwareCountAfterLastStart;
	}

	public int getNewSoftwareCountAfterLastStart() {
		return newSoftwareCountAfterLastStart;
	}

	public List<NewNew> getAllNews() {
		return allNews;
	}

	public void setAllNews(List<NewNew> allNews) {
		this.allNews = allNews;
	}

	public static class NewNew {
		private String title;
		private String datetime;
		private String url;

		public NewNew(String title, String datetime, String url) {
			super();
			this.title = title;
			this.datetime = datetime;
			this.url = url;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDatetime() {
			return datetime;
		}

		public void setDatetime(String datetime) {
			this.datetime = datetime;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		@Override
		public String toString() {
			return String.format("NewNew: %s[%s], %s", getTitle(), getUrl(), getDatetime());
		}
	}
}
