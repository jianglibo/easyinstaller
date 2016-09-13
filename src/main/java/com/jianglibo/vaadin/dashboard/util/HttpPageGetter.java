package com.jianglibo.vaadin.dashboard.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;

@Component
public class HttpPageGetter {

	private CloseableHttpClient httpclient;

	@PostConstruct
	public void after() {
		httpclient = HttpClients.createDefault();
	}

	public String getPage(String url) {
		return getPage(url, Charsets.UTF_8);
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

	public List<NewNew> getNews() {
		String s = getPage("https://github.com/jianglibo/first-vaadin/tree/master/wiki/news/newslist.txt");
		try {
			return CharStreams.readLines(new StringReader(s)).stream().map(line -> line.split("\\s+", 3))
					.map(ft -> new NewNew(ft[2], ft[1],
							"https://github.com/jianglibo/first-vaadin/tree/master/wiki/news/" + ft[0]))
					.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Lists.newArrayList();
//		Document doc = Jsoup.parse(s, "https://github.com");
//		List<String[]> tus = doc.select("div.file-wrap tr.js-navigation-item td.content a") //
//				.stream() //
//				.map(ele -> new String[] { tripExt(ele.html()), ele.absUrl("href") }).collect(Collectors.toList());
//
//		List<String> datetimes = doc.select("div.file-wrap tr.js-navigation-item td.age time-ago").stream() //
//				.map(ele -> ele.attr("datetime")).collect(Collectors.toList());
//
//		List<NewNew> news = Lists.newArrayList();
//
//		for (int i = 0; i < tus.size(); i++) {
//			news.add(new NewNew(tus.get(i)[0], datetimes.get(i), tus.get(i)[1]));
//		}
//
//		return news;
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
