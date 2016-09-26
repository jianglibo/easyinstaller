package com.jianglibo.vaadin.dashboard.util;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.net.MalformedURLException;
import java.net.URL;

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

		URL https = new URL("https://www.jianglibo.com/a.txt");
		assertThat(https.getProtocol(), equalTo("https"));
		URL ftp = new URL("ftp://www.jianglibo.com/a.txt");
		assertThat(ftp.getProtocol(), equalTo("ftp"));
	}
}
