package com.jianglibo.vaadin.dashboard.util;

import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;

import org.junit.Test;

public class TestSoftwareUtil {

	@Test
	public void tprefix(){
		String s = "http://";
		Matcher m = SoftwareUtil.PREFIX_FIND.matcher(s);
		assertTrue("http:// should exact match.", m.matches());

	}
}
