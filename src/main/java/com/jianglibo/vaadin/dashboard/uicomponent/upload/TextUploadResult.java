package com.jianglibo.vaadin.dashboard.uicomponent.upload;

import com.jianglibo.vaadin.dashboard.vo.UploadResult;

public class TextUploadResult implements UploadResult<String> {
	
	public static TextUploadResult createFailed(Exception reason) {
		TextUploadResult tur = new TextUploadResult();
		tur.setReason(reason);
		tur.setSuccess(false);
		return tur;
	}
	
	public static TextUploadResult createSuccessed(UploadMeta uploadMeta, String result) {
		TextUploadResult tur = new TextUploadResult();
		tur.setResult(result);
		tur.setSuccess(true);
		tur.setUploadMeta(uploadMeta);
		return tur;
	}
	
	private UploadMeta uploadMeta;
	
	private String result;
	private Exception reason;
	private boolean success;

	public void setResult(String result) {
		this.result = result;
	}

	public void setReason(Exception reason) {
		this.reason = reason;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	@Override
	public String getResult() {
		return result;
	}

	@Override
	public Exception getReason() {
		return reason;
	}

	@Override
	public boolean isSuccess() {
		return success;
	}

	@Override
	public UploadMeta getUploadMeta() {
		return uploadMeta;
	}

	public void setUploadMeta(UploadMeta uploadMeta) {
		this.uploadMeta = uploadMeta;
	}

}
