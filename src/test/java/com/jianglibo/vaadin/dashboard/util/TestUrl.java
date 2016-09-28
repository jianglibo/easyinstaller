package com.jianglibo.vaadin.dashboard.util;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;

import org.junit.Test;

public class TestUrl {

	@Test(expected = MalformedURLException.class)
	public void fileNameIsNotURL() throws MalformedURLException {
		URL url = new URL("a.txt");
	}

	@Test
	public void t() throws MalformedURLException {
		URL http = new URL("http://www.jianglibo.com/a.txt");
		assertThat(http.getProtocol(), equalTo("http"));

		http = new URL("HTTP://www.jianglibo.com/a.txt");
		assertThat(http.getProtocol(), equalTo("http"));
		
		URL https = new URL("https://www.jianglibo.com/a.txt");
		assertThat(https.getProtocol(), equalTo("https"));
		URL ftp = new URL("ftp://www.jianglibo.com/a.txt");
		assertThat(ftp.getProtocol(), equalTo("ftp"));
	}
	
	
	
	@Test
	public void turl() {
		Matcher m = Patterns.WEB_URL.matcher("uu--abc---1.zip");
		m.matches();
		System.out.println(m.groupCount());
		int c = m.groupCount();
		assertThat(c, equalTo(11));
		assertTrue(m.matches());
	}
	
	@Test
	public void textractUrl() {
		String url = "http://www.example.com/uu--abc---1.zip";
		Matcher m = Patterns.WEB_URL.matcher(url);
		m.matches();
		int c = m.groupCount();
		
		assertThat(c, equalTo(11));
		assertThat(m.group(), equalTo(url));
		assertThat("group 0", m.group(0), equalTo(url));
		assertThat("group 1",m.group(1), equalTo(url));
		assertThat("group 2",m.group(2), equalTo("http://www.example.com"));
		assertThat("group 3",m.group(3), equalTo("www.example.com"));
		assertThat("group 4",m.group(4), equalTo("example."));
		assertThat("group 5",m.group(5), equalTo("com"));
		assertThat("group 6",m.group(6), nullValue());
		assertThat("group 7",m.group(7), nullValue());
		assertThat("group 8",m.group(8), nullValue());
		assertThat("group 9",m.group(9), nullValue());
		assertThat("group 10",m.group(10), nullValue());
		assertTrue(m.matches());
	}
}
