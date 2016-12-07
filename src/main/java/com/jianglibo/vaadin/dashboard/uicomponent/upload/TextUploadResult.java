package com.jianglibo.vaadin.dashboard.uicomponent.upload;

import com.jianglibo.vaadin.dashboard.vo.UploadResult;

public class TextUploadResult implements UploadResult<String> {
	
	public static TextUploadResult createFailed(String reason) {
		TextUploadResult tur = new TextUploadResult();
		tur.setReason(reason);
		tur.setSuccess(false);
		return tur;
	}
	
	public static TextUploadResult createSuccessed(String result) {
		TextUploadResult tur = new TextUploadResult();
		tur.setResult(result);
		tur.setSuccess(true);
		return tur;
	}
	
	private String result;
	private String reason;
	private boolean success;

	public void setResult(String result) {
		this.result = result;
	}

	public void setReason(String reason) {
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
	public String getReason() {
		return reason;
	}

	@Override
	public boolean isSuccess() {
		return success;
	}

}
