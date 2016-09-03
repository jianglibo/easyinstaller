package com.jianglibo.vaadin.dashboard.uicomponent.upload;

import com.vaadin.ui.Upload.Receiver;

public interface UploadReceiver<T> extends Receiver {

	void uploadSuccessed();

	void uploadNotSuccess();
}
