package com.jianglibo.vaadin.dashboard.event.view;

import com.jianglibo.vaadin.dashboard.domain.PkSource;

public class UploadFinishEvent {

	private PkSource pkSource;
	
	private boolean newCreated;
	
	public UploadFinishEvent(PkSource pkSource) {
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
