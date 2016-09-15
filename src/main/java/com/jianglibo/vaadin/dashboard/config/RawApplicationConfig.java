package com.jianglibo.vaadin.dashboard.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;
import com.google.gwt.thirdparty.guava.common.collect.Maps;

@Component
@ConfigurationProperties(prefix="application")
public class RawApplicationConfig {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RawApplicationConfig.class);
	
	private String uploadDst;

	/**
	 * where application read custom install steps.
	 */
	private String stepFolder;
	
	/**
	 * where upload runner to find files.
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


	public String getStepFolder() {
		return stepFolder;
	}

	public void setStepFolder(String stepFolder) {
		this.stepFolder = stepFolder;
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
}
