package com.jianglibo.vaadin.dashboard.util;


import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindowEvent;
import com.gargoylesoftware.htmlunit.WebWindowListener;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class TestHtmlUnit {

	@Test
	public void t() throws FailingHttpStatusCodeException, IOException {
		try (WebClient webClient = new WebClient()) {
			webClient.addWebWindowListener(new WebWindowListener() {
				@Override
			    public void webWindowOpened(WebWindowEvent event) {
			    	
			    }
				@Override
			    public void webWindowClosed(WebWindowEvent event) {}

				@Override
				public void webWindowContentChanged(WebWindowEvent event) {
					System.out.println(event.getNewPage().getWebResponse().getStatusCode());
				}
			});
			
			HtmlPage page = webClient.getPage("http://www.oracle.com/technetwork/java/javase/downloads/index.html");
			page.getElementById("javasejdk");
			HtmlAnchor ha = page.getFirstByXPath("//h3[@id='javasejdk']/a[1]");
			ha.click();
//			assertThat(ha.getBaseURI(), equalTo("http://www.oracle.com"));
			System.out.println(ha.getHrefAttribute());
			assertThat(ha, notNullValue());			
		}
	}
}
