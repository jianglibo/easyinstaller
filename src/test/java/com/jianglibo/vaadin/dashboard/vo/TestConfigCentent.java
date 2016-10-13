package com.jianglibo.vaadin.dashboard.vo;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;

import org.junit.Test;

import com.jianglibo.vaadin.dashboard.Tbase;

public class TestConfigCentent extends Tbase {

	@Test
	public void tpattern() {
		Matcher m = ConfigContent.convertPth.matcher("<!--   xml -> yaml -->");
		assertTrue("meta line shoud match.", m.matches());
		assertThat("from should be xml", m.group(1), equalTo("xml"));
		assertThat("to should be xml", m.group(2), equalTo("yaml"));
		
		m = ConfigContent.convertPth.matcher("<!--   xml->yaml -->");
		assertTrue("meta line shoud match.", m.matches());
		assertThat("from should be xml", m.group(1), equalTo("xml"));
		assertThat("to should be xml", m.group(2), equalTo("yaml"));
		
		m = ConfigContent.convertPth.matcher("  <!--   xml->yaml -->  ");
		assertTrue("meta line shoud match.", m.matches());
		assertThat("from should be xml", m.group(1), equalTo("xml"));
		assertThat("to should be xml", m.group(2), equalTo("yaml"));
	}
}
