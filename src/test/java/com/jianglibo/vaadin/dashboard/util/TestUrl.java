package com.jianglibo.vaadin.dashboard.util;

import static org.hamcrest.Matchers.equalTo;
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
		
		for(int i = 0; i< c; i++) {
			System.out.println(m.group(i));
		}
		assertTrue(m.matches());
	}
}
