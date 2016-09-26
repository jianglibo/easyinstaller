package com.jianglibo.vaadin.dashboard.vo;

import java.net.MalformedURLException;
import java.net.URL;

public class FileToUploadVo {
	
	private boolean remoteFile;
	
	private String orignValue;

	public FileToUploadVo(String orignValue) {
		super();
		this.orignValue = orignValue;
		verify();
	}

	private void verify() {
		try {
			URL url = new URL(getOrignValue());
			if ("http".equals(url.getProtocol()) || "https".equals(url.getProtocol())) {
				setRemoteFile(true);
			} else {
				setRemoteFile(false);
			}
		} catch (MalformedURLException e) {
			setRemoteFile(false);
		}		
	}
	
	public String getRelative() {
		if (isRemoteFile()) {
			int idx = getOrignValue().lastIndexOf('/') + 1;
			return getOrignValue().substring(idx);
		} else {
			return getOrignValue();
		}
	}

	public boolean isRemoteFile() {
		return remoteFile;
	}

	public void setRemoteFile(boolean remoteFile) {
		this.remoteFile = remoteFile;
	}

	public String getOrignValue() {
		return orignValue;
	}

	public void setOrignValue(String orignValue) {
		this.orignValue = orignValue;
	}
	
	
	
}
