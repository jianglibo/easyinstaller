package com.jianglibo.vaadin.dashboard.event.view;

public class PageMetaEvent {
	private int totalRecord;
	
	private int perPage;
	
	private int totalPage;

	public PageMetaEvent(long totalRecord, int perPage) {
		int page = (int) (totalRecord/perPage);
		if (totalRecord % perPage > 0) {
			page=page+1;
		}
		this.setTotalPage(page);
		this.setTotalRecord(new Long(totalRecord).intValue());
	}
	
	public String getTotalRecordString() {
		return String.valueOf(totalRecord);
	}
	
	public int getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}

	public int getPerPage() {
		return perPage;
	}

	public void setPerPage(int perPage) {
		this.perPage = perPage;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
}
