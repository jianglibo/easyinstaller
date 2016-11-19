package com.jianglibo.vaadin.dashboard.service;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jianglibo.vaadin.dashboard.Tbase;
import com.jianglibo.vaadin.dashboard.domain.BoxGroupHistory;
import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.repositories.BoxGroupHistoryRepository;
import com.jianglibo.vaadin.dashboard.repositories.SoftwareRepository;
import com.jianglibo.vaadin.dashboard.vo.SoftwareImportResult;

public class TestSoftwareImporter extends Tbase {
	
	@Autowired
	private SoftwareImportor softwareImportor;
	
	@Autowired
	private SoftwareRepository softwareRepository;
	
	@Autowired
	private BoxGroupHistoryRepository boxGroupHistoryRepository;
	
	@Before
	public void be() {
		List<Software> sfs = softwareRepository.findAll();
		for(Software sw : sfs) {
			List<BoxGroupHistory> bghs = boxGroupHistoryRepository.findBySoftware(sw);
			bghs.forEach(bgh -> boxGroupHistoryRepository.delete(bgh));
			softwareRepository.delete(sw);
		}
	}
	
	private Path fixtureFolder = Paths.get("fixtures", "installscripts", "centos7-ps-2.7.3");
	
	@Test
	public void timportFromFolder() throws IOException {
		
		List<SoftwareImportResult> sirs = softwareImportor.installSoftwareFromFolder(fixtureFolder, false);
		
		sirs = softwareImportor.installSoftwareFromFolder(fixtureFolder, false);
		
		sirs = softwareRepository.findAll().stream().map(sf -> new SoftwareImportResult(sf)).collect(Collectors.toList());
		
		assertThat("thers should be 1 software", sirs.size(), equalTo(1));
		assertThat("this software should has 1 textfile", sirs.get(0).getSoftware().getTextfiles().size(), equalTo(1));
		assertThat("textfile name should be right", sirs.get(0).getSoftware().getTextfiles().iterator().next().getName(), equalTo("etc/hadoop/hadoop-env.sh"));
		assertThat("textfile's software field should be right", sirs.get(0).getSoftware().getTextfiles().iterator().next().getSoftware(), equalTo(sirs.get(0).getSoftware()));
	}

	@Test
	public void timportFromZip() throws IOException {
		try (Stream<Path> pstreams = Files.walk(Paths.get("fixtures"))) {
			List<Path> spp = pstreams.filter(p -> {
				return Files.isRegularFile(p) && p.toString().endsWith(".zip");
			}).collect(Collectors.toList());
			
			List<SoftwareImportResult> sirs = spp.stream().map(p -> {
				try {
					return softwareImportor.installSoftwareFromZipFile(p);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}).filter(Objects::nonNull).flatMap(sfs -> sfs.stream()).collect(Collectors.toList());
			assertThat("zip file count should equal to new software count", spp.size(), equalTo(sirs.size()));
			assertThat("zip file count should equal to software count in db", new Long(spp.size()), equalTo(softwareRepository.count()));
		}
	}
}
