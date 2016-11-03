package com.jianglibo.vaadin.dashboard.view.envfixture;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.jianglibo.vaadin.dashboard.Tbase;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.BoxGroup;
import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.service.AppObjectMappers;

public class TestEnvFixtureCreator extends Tbase {
	
	@Autowired
	private EnvFixtureCreator envFixtureCreator;
	
	@Autowired
	private AppObjectMappers appObjectMappers;

	@Test
	public void tsuccess() throws IOException {
		Path p = Paths.get("fixtures", "installscripts", "centos7-ps-2.7.3");
		envFixtureCreator.create(p);
		Path fixture = p.resolve("fixtures").resolve("envforcodeexec.json");
		
		String s = Files.asCharSource(fixture.toFile(), Charsets.UTF_8).read();
		
		EnvForCodeExecSimple efc = appObjectMappers.getObjectMapperNoIdent().readValue(s, EnvForCodeExecSimple.class);
		
		assertThat("box is right", efc.getBox().getIp(), equalTo("192.168.33.110"));
		
	}
	
	private static class EnvForCodeExecSimple {
		private String remoteFolder;
		private Box box;
		
		private BoxGroup boxGroup;
		
		private Software software;

		public String getRemoteFolder() {
			return remoteFolder;
		}

		public void setRemoteFolder(String remoteFolder) {
			this.remoteFolder = remoteFolder;
		}

		public Box getBox() {
			return box;
		}

		public void setBox(Box box) {
			this.box = box;
		}

		public BoxGroup getBoxGroup() {
			return boxGroup;
		}

		public void setBoxGroup(BoxGroup boxGroup) {
			this.boxGroup = boxGroup;
		}

		public Software getSoftware() {
			return software;
		}

		public void setSoftware(Software software) {
			this.software = software;
		}
	}
}
