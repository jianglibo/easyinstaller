package com.jianglibo.vaadin.dashboard.util;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.regex.Matcher;

import org.junit.Test;

import com.jianglibo.vaadin.dashboard.domain.Software;

public class TestSoftwareUtil {

	@Test
	public void tprefix(){
		String s = "http://";
		Matcher m = SoftwareUtil.PREFIX_FIND.matcher(s);
		assertTrue("http:// should exact match.", m.matches());
	}
	
	@Test
	public void timeout() {
		Software sf = new Software();
		sf.setTimeouts("install:10s, update: 100ms,delete: 2m, dddd: 3h, notexists:1.5,skdils");
		Map<String, Long> mp = sf.getTimeOutMaps();
		assertThat("10s should be 10000", mp.get("install"), equalTo(10000L));
		assertThat("100ms should be 100", mp.get("update"), equalTo(100L));
		assertThat("2m should be 2*60*1000", mp.get("delete"), equalTo(2*60*1000L));
		assertThat("3h should be 3*60*60*1000", mp.get("dddd"), equalTo(3*60*60*1000L));
		assertThat("size should be 4", mp.size(), equalTo(4));
	}
}
