package com.jianglibo.vaadin.dashboard.uicomponent.upload;

import com.jianglibo.vaadin.dashboard.domain.PkSource;

public interface UploadSuccessListener {
	void uploadFinished(PkSource pkSource, boolean newCreate);
}
