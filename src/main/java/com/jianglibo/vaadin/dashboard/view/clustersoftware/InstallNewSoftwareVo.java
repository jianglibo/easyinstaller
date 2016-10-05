package com.jianglibo.vaadin.dashboard.view.clustersoftware;

import com.jianglibo.vaadin.dashboard.domain.Software;

public class InstallNewSoftwareVo {
	
	private Software software;
	
	private String action;

	public Software getSoftware() {
		return software;
	}

	public void setSoftware(Software software) {
		this.software = software;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
}
