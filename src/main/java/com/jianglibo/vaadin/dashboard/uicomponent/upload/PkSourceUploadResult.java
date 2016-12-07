package com.jianglibo.vaadin.dashboard.uicomponent.upload;

import com.jianglibo.vaadin.dashboard.domain.PkSource;
import com.jianglibo.vaadin.dashboard.vo.UploadResult;

public class PkSourceUploadResult implements UploadResult<PkSource>{

	private PkSource result;
	private String reason;
	private boolean success;
	private boolean newCreated;
	
	public static PkSourceUploadResult createFailed(String reason) {
		PkSourceUploadResult tur = new PkSourceUploadResult(null);
		tur.setReason(reason);
		tur.setSuccess(false);
		return tur;
	}
	
	public static PkSourceUploadResult createSuccessed(PkSource result) {
		PkSourceUploadResult tur = new PkSourceUploadResult(result);
		tur.setSuccess(true);
		return tur;
	}
	
	
	
	public void setResult(PkSource result) {
		this.result = result;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public PkSourceUploadResult(PkSource pkSource) {
		if (pkSource == null) {
			setNewCreated(false);
		} else {
			setNewCreated(true);
		}
		setResult(pkSource);
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
	public String getReason() {
		return reason;
	}

	@Override
	public boolean isSuccess() {
		return success;
	}
}
