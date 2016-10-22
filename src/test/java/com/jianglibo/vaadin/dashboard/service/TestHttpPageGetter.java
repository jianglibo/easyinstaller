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
import com.jianglibo.vaadin.dashboard.service.SoftwareImportor.SoftwarelistLine;

public class TestHttpPageGetter extends Tbase {
	
	@Autowired
	private SoftwareRepository softwareRepository;
	
	@Autowired
	private SoftwareImportor preDefinedSoftwareProcessor;

	@Test
	public void t() throws IOException {
		
		HttpPageGetter mockGetter = mock(HttpPageGetter.class);
		
		when(mockGetter.getPage(anyString())).thenReturn("");
		
		Software sf = softwareRepository.findByNameAndOstypeAndSversion("tcl", "centos7", "1");
		boolean exists = sf != null;
		
		if (!exists) {
			return;
		}
		
		String zn = "tcl--centos7--1.zip";
		
		Path zpath = applicationConfig.getSoftwareFolderPath().resolve(zn);
		Path tf = Paths.get("softwares", zn);
		
		if (!java.nio.file.Files.exists(zpath)) {
			java.nio.file.Files.copy(tf, zpath);
		}
		
		String md5 = Files.hash(tf.toFile(), Hashing.md5()).toString();
		SoftwarelistLine sl = new SoftwarelistLine("", applicationConfig.getSoftwareFolderPath(), zn + "," + "aaaaaa");
		// event software exists in db, because of md5 changed, software should get updated.
		boolean success = preDefinedSoftwareProcessor.processOneSoftware(mockGetter, sl);
		assertThat(success, equalTo(exists));
	}

}
