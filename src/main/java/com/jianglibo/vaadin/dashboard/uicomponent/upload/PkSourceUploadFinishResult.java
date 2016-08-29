package com.jianglibo.vaadin.dashboard.uicomponent.upload;

import com.jianglibo.vaadin.dashboard.domain.PkSource;

public class PkSourceUploadFinishResult {

	private PkSource pkSource;
	
	private boolean newCreated;
	
	public PkSourceUploadFinishResult(PkSource pkSource) {
		if (pkSource == null) {
			setNewCreated(false);
		} else {
			setNewCreated(true);
		}
		setPkSource(pkSource);
	}

	public PkSource getPkSource() {
		return pkSource;
	}

	public void setPkSource(PkSource pkSource) {
		this.pkSource = pkSource;
	}

	public boolean isNewCreated() {
		return newCreated;
	}

	public void setNewCreated(boolean newCreated) {
		this.newCreated = newCreated;
	}
}
