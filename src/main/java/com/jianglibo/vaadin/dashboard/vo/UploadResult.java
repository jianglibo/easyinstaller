package com.jianglibo.vaadin.dashboard.vo;

public interface UploadResult<T> {
	T getResult();
	String getReason();
	boolean isSuccess();
}
