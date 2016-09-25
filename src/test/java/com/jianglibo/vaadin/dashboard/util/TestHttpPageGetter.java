package com.jianglibo.vaadin.dashboard.util;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jianglibo.vaadin.dashboard.Tbase;

public class TestHttpPageGetter extends Tbase {
	
	@Autowired
	private HttpPageGetter httpPageGetter;

	@Test
	public void t() {
		boolean success = httpPageGetter.processOneSoftware(applicationConfig.getSoftwareFolderPath(), "tcl--centos7--1.zip");
		assertTrue(success);
	}
}
