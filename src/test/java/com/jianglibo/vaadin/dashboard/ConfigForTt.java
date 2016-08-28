package com.jianglibo.vaadin.dashboard;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="tests")
public class ConfigForTt {
	
	private String sshHost;
	private String sshKeyfile;
	private String sshUser;
	private String sshKnownhostsFile;
	
	
	public String getSshHost() {
		return sshHost;
	}
	public void setSshHost(String sshHost) {
		this.sshHost = sshHost;
	}
	public String getSshKeyfile() {
		return sshKeyfile;
	}
	public void setSshKeyfile(String sshKeyfile) {
		this.sshKeyfile = sshKeyfile;
	}
	public String getSshUser() {
		return sshUser;
	}
	public void setSshUser(String sshUser) {
		this.sshUser = sshUser;
	}
	public String getSshKnownhostsFile() {
		return sshKnownhostsFile;
	}
	public void setSshKnownhostsFile(String sshKnownhostsFile) {
		this.sshKnownhostsFile = sshKnownhostsFile;
	}
	
	
	  
}
