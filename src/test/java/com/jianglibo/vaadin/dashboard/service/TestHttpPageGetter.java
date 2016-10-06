package com.jianglibo.vaadin.dashboard.service;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.jianglibo.vaadin.dashboard.Tbase;
import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.repositories.SoftwareRepository;
import com.jianglibo.vaadin.dashboard.service.PreDefinedSoftwareProcessor.SoftwarelistLine;

public class TestHttpPageGetter extends Tbase {
	
	@Autowired
	private SoftwareRepository softwareRepository;
	
	@Autowired
	private PreDefinedSoftwareProcessor preDefinedSoftwareProcessor;

	@Test
	public void t() throws IOException {
		
		HttpPageGetter mockGetter = mock(HttpPageGetter.class);
		
		when(mockGetter.getPage(anyString())).thenReturn("");
		
		Software sf = softwareRepository.findByNameAndOstypeAndSversion("tcl", "centos7", "1");
		boolean exists = sf != null;
		String zn = "tcl--centos7--1.zip";
		
		if (java.nio.file.Files.exists(applicationConfig.getSoftwareFolderPath().resolve(zn))) {
			java.nio.file.Files.delete(applicationConfig.getSoftwareFolderPath().resolve(zn));
		}
		
		Path tf = Paths.get("softwares", zn);
		String md5 = Files.hash(tf.toFile(), Hashing.md5()).toString();
		SoftwarelistLine sl = new SoftwarelistLine("", applicationConfig.getSoftwareFolderPath(), zn + "," + "aaaaaa");
		boolean success = preDefinedSoftwareProcessor.processOneSoftware(mockGetter, sl);
		assertThat(success, equalTo(!exists));
	}

}
