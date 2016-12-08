package com.jianglibo.vaadin.dashboard.vo;

import com.jianglibo.vaadin.dashboard.uicomponent.upload.UploadMeta;

public interface UploadResult<T> {
	T getResult();
	Exception getReason();
	UploadMeta getUploadMeta();
	boolean isSuccess();
}
