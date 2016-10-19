package com.jianglibo.vaadin.dashboard.util;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.junit.Test;
import org.springframework.core.io.Resource;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.jianglibo.vaadin.dashboard.Tbase;

public class HttpsResource extends Tbase {
	
	@Test
	public void https() throws IOException {
//		String url = "https://raw.githubusercontent.com/jianglibo/easyinstaller/master/src/main/resources/scripts/tcl/shared.tcl";
		String url =  "https://www.baidu.com/";
		Resource r = context.getResource(url);
		String content = CharStreams.toString(new InputStreamReader(r.getInputStream(), Charsets.UTF_8));
		assertTrue("should contains 百度", content.contains("百度"));
	}
	
	@Test
	public void scriptSources() {
		List<String> ss = applicationConfig.getScriptSources();
		assertThat("script sources number should be 2", ss.size(), equalTo(2));
		assertThat(ss.get(0), equalTo("classpath:scripts/"));
		assertTrue("should start with file:///", ss.get(1).startsWith("file:///"));
	}

}
