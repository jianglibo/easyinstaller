package com.jianglibo.vaadin.dashboard.uicomponent.upload;

public class UploadMeta {
	
	private String filename;
	private long length;
	private String mIMEType;
	
	public UploadMeta(String filename, long length, String mIMEType) {
		super();
		this.filename = filename;
		this.length = length;
		this.mIMEType = mIMEType;
	}
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public long getLength() {
		return length;
	}
	public void setLength(long length) {
		this.length = length;
	}
	public String getmIMEType() {
		return mIMEType;
	}
	public void setmIMEType(String mIMEType) {
		this.mIMEType = mIMEType;
	}
	
	

}
