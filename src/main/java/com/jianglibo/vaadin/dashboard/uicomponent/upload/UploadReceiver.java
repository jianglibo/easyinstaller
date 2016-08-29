package com.jianglibo.vaadin.dashboard.uicomponent.upload;

import com.vaadin.ui.Upload.Receiver;

public interface UploadReceiver<T> extends Receiver {
	
	UploadReceiver<T> afterInjection(UploadSuccessEventLinstener<T> listener);

	void uploadSuccessed();

	void uploadNotSuccess();
}
