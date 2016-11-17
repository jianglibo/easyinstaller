package com.jianglibo.vaadin.dashboard.ssh;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.jcraft.jsch.JSchException;
import com.jianglibo.vaadin.dashboard.ConfigForTt;
import com.jianglibo.vaadin.dashboard.Tbase;

public class SshBaset extends Tbase {

	@Autowired
	private ConfigForTt configForTt;

	public JschSession getJschSession() throws JSchException {
		return new JschSession.JschSessionBuilder()//
				.setHost(configForTt.getSshHost())//
				.setKeyFile(configForTt.getSshKeyfile()) //
//				.setKnownHosts(configForTt.getSshKnownhostsFile())//
				.setSshUser("root").build();
	}
	
	public JschSession getJschSessionKeyFromDb() throws JSchException, IOException {
		String ks = Files.toString(new File(configForTt.getSshKeyfile()), Charsets.ISO_8859_1);
		File.createTempFile("ssh", null);
		
		return new JschSession.JschSessionBuilder()//
				.setHost(configForTt.getSshHost())//
				.setKeyFile(configForTt.getSshKeyfile()) //
//				.setKnownHosts(configForTt.getSshKnownhostsFile())//
				.setSshUser("root").build();
	}
	
	@Test
	public void placeholder() {
		assertTrue(true);
	}
}
