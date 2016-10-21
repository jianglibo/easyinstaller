package com.jianglibo.vaadin.dashboard.vo;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jianglibo.vaadin.dashboard.Tbase;
import com.jianglibo.vaadin.dashboard.service.AppObjectMappers;

public class TestConfigCentent extends Tbase {
	
	@Autowired
	private AppObjectMappers appObjectMappers;

	@Test
	public void tpattern() {
		String json = "{a: 1, b: 2}";
		String converted = null;
		ConfigContent cc = new ConfigContent(json);
		converted = cc.getConverted(appObjectMappers, "JSON");
		
		assertThat("should be normalized json.", converted, equalTo("{\"a\":1,\"b\":2}"));
		
		String yaml = "a: 1\r\nb: 2";
		cc = new ConfigContent(yaml);
		converted = cc.getConverted(appObjectMappers, "JSON");
		assertThat("should be normalized json.", converted, equalTo("{\"a\":1,\"b\":2}"));
	}
}
