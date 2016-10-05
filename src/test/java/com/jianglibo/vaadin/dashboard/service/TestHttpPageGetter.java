package com.jianglibo.vaadin.dashboard.service;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jianglibo.vaadin.dashboard.Tbase;
import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.repositories.SoftwareRepository;
import com.jianglibo.vaadin.dashboard.service.HttpPageGetter;

public class TestHttpPageGetter extends Tbase {
	
	@Autowired
	private HttpPageGetter httpPageGetter;
	
	@Autowired
	private SoftwareRepository softwareRepository;

	@Test
	public void t() {
		Software sf = softwareRepository.findByNameAndOstypeAndSversion("tcl", "centos7", "1");
		boolean exists = sf != null;
		boolean success = httpPageGetter.processOneSoftware(applicationConfig.getSoftwareFolderPath(), "tcl--centos7--1.zip");
		assertThat(success, equalTo(!exists));
	}

}
