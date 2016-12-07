package com.jianglibo.vaadin.dashboard.uicomponent.upload;

import com.jianglibo.vaadin.dashboard.vo.UploadResult;

public interface SimplifiedUploadResultLinstener<E, T extends UploadResult<E>> {
	void onUploadResult(T ufe);
}
