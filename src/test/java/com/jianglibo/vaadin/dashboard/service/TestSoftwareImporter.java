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
import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.repositories.SoftwareRepository;

public class TestSoftwareImporter extends Tbase {
	
	@Autowired
	private SoftwareImportor softwareImportor;
	
	@Autowired
	private SoftwareRepository softwareRepository;
	
	@Before
	public void be() {
		softwareRepository.deleteAll();
	}
	
	private Path fixtureFolder = Paths.get("fixtures", "installscripts", "centos7-ps-2.7.3");
	
	@Test
	public void timportFromFolder() throws IOException {
		
		softwareRepository.deleteAll();
		
		List<Software> sfs = softwareImportor.installSoftwareFromFolder(fixtureFolder, false);
		
		sfs = softwareImportor.installSoftwareFromFolder(fixtureFolder, false);
		
		sfs = softwareRepository.findAll();
		
		assertThat("thers should be 1 software", sfs.size(), equalTo(1));
		assertThat("this software should has 1 textfile", sfs.get(0).getTextfiles().size(), equalTo(1));
		assertThat("textfile name should be right", sfs.get(0).getTextfiles().iterator().next().getName(), equalTo("etc/hadoop/hadoop-env.sh"));
		assertThat("textfile's software field should be right", sfs.get(0).getTextfiles().iterator().next().getSoftware(), equalTo(sfs.get(0)));
	}

	@Test
	public void timportFromZip() throws IOException {
		try (Stream<Path> pstreams = Files.walk(Paths.get("fixtures"))) {
			List<Path> spp = pstreams.filter(p -> {
				return Files.isRegularFile(p) && p.toString().endsWith(".zip");
			}).collect(Collectors.toList());
			
			List<Software> softwares = spp.stream().map(p -> {
				try {
					return softwareImportor.installSoftwareFromZipFile(p);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}).filter(Objects::nonNull).flatMap(sfs -> sfs.stream()).collect(Collectors.toList());
			assertThat("zip file count should equal to new software count", spp.size(), equalTo(softwares.size()));
			assertThat("zip file count should equal to software count in db", new Long(spp.size()), equalTo(softwareRepository.count()));
		}
	}
}
