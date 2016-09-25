package com.jianglibo.vaadin.dashboard.util;

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

import javax.annotation.PostConstruct;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import com.jianglibo.vaadin.dashboard.config.ApplicationConfig;
import com.jianglibo.vaadin.dashboard.domain.Person;
import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.init.AppInitializer;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.repositories.SoftwareRepository;

@Component
public class HttpPageGetter {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpPageGetter.class);

	private CloseableHttpClient httpclient;

	@Autowired
	private ApplicationConfig applicationConfig;

	@Autowired
	private SoftwareRepository softwareRepository;
	
	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private ObjectMapper ymlObjectMapper;

	@PostConstruct
	public void after() {
		httpclient = HttpClients.createDefault();
	}

	public String getPage(String url) {
		return getPage(url, Charsets.UTF_8);
	}

	public void getFile(String url, Path target) {
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
	}

	public String getPage(String url, Charset cs) {
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
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
				}
			}
		}
		return "";
	}

	public static interface NewSoftwareAddedEventListener {
		void newSoftwareAdded();
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

	@Async
	public void fetchSoftwareLists(NewSoftwareAddedEventListener nsfael) {
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
			String snLines = getPage(urlBase + listfn);

			Files.write(applicationConfig.getSoftwareFolderPath().resolve(listfn), snLines.getBytes(Charsets.UTF_8));
			CharStreams.readLines(new StringReader(snLines)).stream().forEach(sn -> {
				Path targetZipFile = sfFolder.resolve(sn); 
				if (!Files.exists(targetZipFile)) {
					getFile(urlBase + sn, targetZipFile);
				}
				
				if (Files.exists(targetZipFile)) {
					boolean success = processOneSoftware(sfFolder, sn);
					if (success) {
						nsfael.newSoftwareAdded();
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Because this is a special web application, most of times only one user is
	 * active, and it shutdown often.
	 * 
	 * Make a version marker at github site, and save every user's last access
	 * version in database. So when new version arrive, can alert user.
	 * 
	 * @return
	 */
	@Async
	public void fetchNews() {
		String s = getPage("https://raw.githubusercontent.com/jianglibo/first-vaadin/master/wiki/news/newslist.txt");
		try {
			List<NewNew> nn = CharStreams.readLines(new StringReader(s)).stream().map(line -> line.split("\\s+", 3))
					.map(ft -> new NewNew(ft[2], ft[1],
							"https://github.com/jianglibo/first-vaadin/tree/master/wiki/news/" + ft[0]))
					.collect(Collectors.toList());
			nn.stream().forEach(n -> LOGGER.info(n.toString()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Document doc = Jsoup.parse(s, "https://github.com");
		// List<String[]> tus = doc.select("div.file-wrap tr.js-navigation-item
		// td.content a") //
		// .stream() //
		// .map(ele -> new String[] { tripExt(ele.html()), ele.absUrl("href")
		// }).collect(Collectors.toList());
		//
		// List<String> datetimes = doc.select("div.file-wrap
		// tr.js-navigation-item td.age time-ago").stream() //
		// .map(ele -> ele.attr("datetime")).collect(Collectors.toList());
		//
		// List<NewNew> news = Lists.newArrayList();
		//
		// for (int i = 0; i < tus.size(); i++) {
		// news.add(new NewNew(tus.get(i)[0], datetimes.get(i), tus.get(i)[1]));
		// }
		//
		// return news;
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
