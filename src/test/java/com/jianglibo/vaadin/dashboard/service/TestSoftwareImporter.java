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

	@Test
	public void timport() throws IOException {
		try (Stream<Path> pstreams = Files.walk(Paths.get("fixtures"))) {
			List<Path> spp = pstreams.filter(p -> {
				return Files.isRegularFile(p) && p.toString().endsWith(".zip");
			}).collect(Collectors.toList());
			
			List<Software> softwares = spp.stream().map(p -> {
				try {
					return softwareImportor.installOneSoftware(p);
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
