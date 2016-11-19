package com.jianglibo.vaadin.dashboard.vo;

import com.jianglibo.vaadin.dashboard.domain.Software;

public class SoftwareImportResult {

	private Software software;
	private boolean success;
	private String reason;

	public SoftwareImportResult(Software software) {
		setSuccess(true);
		setSoftware(software);
	}

	public SoftwareImportResult(String reason) {
		setSuccess(false);
		setReason(reason);
	}

	public Software getSoftware() {
		return software;
	}

	public void setSoftware(Software software) {
		this.software = software;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
