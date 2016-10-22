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
import java.util.stream.Collectors;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import com.jianglibo.vaadin.dashboard.Broadcaster;
import com.jianglibo.vaadin.dashboard.Broadcaster.BroadCasterMessage;
import com.jianglibo.vaadin.dashboard.Broadcaster.BroadCasterMessageBody;
import com.jianglibo.vaadin.dashboard.Broadcaster.BroadCasterMessageType;

@Component
public class HttpPageGetter {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpPageGetter.class);
	

	
	private int fetchNewsCount = 0;
	
	private List<NewNew> allNews = Lists.newArrayList();

	public String getPage(String url) {
		return getPage(url, Charsets.UTF_8);
	}

	public Path getFile(String url, Path target) {
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
						if (target == null) {
							return tmpFile;
						} else {
							Files.move(tmpFile, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
							return target;
						}
					} finally {

					}
				}
			} catch (IOException e) {
				e.printStackTrace();
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
		return null;
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

	

	
	public static class NewNewsMessage implements BroadCasterMessageBody {
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

		@Override
		public BroadCasterMessageType getBroadCasterMessageType() {
			return BroadCasterMessageType.NEW_NEWS;
		}
	}
	
	/**
	 * for every 5 seconds, report the new software inserted count from start of application.
	 */
	@Scheduled(fixedRate = 10000)
	public void broadcastNewSoftware() {
		Broadcaster.broadcast(new BroadCasterMessage(new NewNewsMessage(getAllNews(), fetchNewsCount)));
	}



	/**
	 * fetch once for every 10 minutes.
	 */
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
