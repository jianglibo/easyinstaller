package com.jianglibo.vaadin.dashboard;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jianglibo.vaadin.dashboard.config.ApplicationConfig;


public class TestAppConfig extends Tbase {
	
	@Autowired
	private ApplicationConfig applicationConfig;
	
	@Test
	public void tscriptSources() {
		List<String> ss = applicationConfig.getScriptSources();
		assertThat("should have 2 sources", ss.size(), equalTo(3));
		assertThat("default script source should be classpath.", ss.get(ss.size() - 1), equalTo(ApplicationConfig.defaultScriptSource));
	}

}
