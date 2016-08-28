package com.jianglibo.vaadin.dashboard.ssh;

import org.springframework.beans.factory.annotation.Autowired;

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
				.setKnownHosts(configForTt.getSshKnownhostsFile())//
				.setUser("root").build();
	}
}
