package com.jianglibo.vaadin.dashboard.uicomponent.upload;

import com.jianglibo.vaadin.dashboard.domain.PkSource;
import com.jianglibo.vaadin.dashboard.vo.UploadResult;

public class PkSourceUploadResult implements UploadResult<PkSource>{

	private PkSource result;
	private Exception reason;
	private boolean success;
	private boolean newCreated;
	
	private UploadMeta uploadMeta;
	
	public static PkSourceUploadResult createFailed(Exception reason) {
		PkSourceUploadResult tur = new PkSourceUploadResult();
		tur.setReason(reason);
		tur.setSuccess(false);
		return tur;
	}
	
	public static PkSourceUploadResult createSuccessed(UploadMeta uploadMeta,PkSource result) {
		PkSourceUploadResult tur = new PkSourceUploadResult();
		if (result == null) {
			tur.setNewCreated(false);
		} else {
			tur.setNewCreated(true);
		}
		tur.setResult(result);
		tur.setSuccess(true);
		tur.setUploadMeta(uploadMeta);
		return tur;
	}
	
	public void setResult(PkSource result) {
		this.result = result;
	}

	public void setReason(Exception reason) {
		this.reason = reason;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean isNewCreated() {
		return newCreated;
	}

	public void setNewCreated(boolean newCreated) {
		this.newCreated = newCreated;
	}

	@Override
	public PkSource getResult() {
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
