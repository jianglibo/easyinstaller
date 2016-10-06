package com.jianglibo.vaadin.dashboard.config;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.google.gwt.thirdparty.guava.common.collect.Maps;

@Component
@ConfigurationProperties(prefix="application")
public class RawApplicationConfig {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RawApplicationConfig.class);
	
	private String uploadDst;

	/**
	 * where application save zipped software files.
	 */
	private String softwareFolder;
	
	/**
	 * where filesToUpload saved.
	 */
	private String localFolder;
	
	/**
	 * where jsch to find ssh key files. 
	 */
	private String sshKeyFolder;
	
	/**
	 * the folder in server box to store tmp files.
	 */
	private String remoteFolder;
	
	private boolean autoLogin;
	
	private Map<String, List<ComboItem>> comboDatas = Maps.newHashMap();
	
	

	
	public String getUploadDst() {
		return uploadDst;
	}

	public void setUploadDst(String uploadDst) {
		this.uploadDst = uploadDst;
	}

	public Map<String, List<ComboItem>> getComboDatas() {
		return comboDatas;
	}

	public void setComboDatas(Map<String, List<ComboItem>> comboDatas) {
		this.comboDatas = comboDatas;
	}

	public String getSoftwareFolder() {
		return softwareFolder;
	}

	public void setSoftwareFolder(String softwareFolder) {
		this.softwareFolder = softwareFolder;
	}

	public String getLocalFolder() {
		return localFolder;
	}

	public void setLocalFolder(String localFolder) {
		this.localFolder = localFolder;
	}

	public String getRemoteFolder() {
		return remoteFolder;
	}

	public void setRemoteFolder(String remoteFolder) {
		this.remoteFolder = remoteFolder;
	}

	public String getSshKeyFolder() {
		return sshKeyFolder;
	}

	public void setSshKeyFolder(String sshKeyFolder) {
		this.sshKeyFolder = sshKeyFolder;
	}

	public boolean isAutoLogin() {
		return autoLogin;
	}

	public void setAutoLogin(boolean autoLogin) {
		this.autoLogin = autoLogin;
	}
	
	
}
