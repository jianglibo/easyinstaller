package com.jianglibo.vaadin.dashboard.util;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class TestMisc {

	@Test
	public void replaceall() {
		String s = "a\\bcd";
		assertThat(s.replaceAll("\\\\", "/"), equalTo("a/bcd"));
	}
}
