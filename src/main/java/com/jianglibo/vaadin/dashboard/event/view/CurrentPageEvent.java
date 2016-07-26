package com.jianglibo.vaadin.dashboard.event.view;

public class CurrentPageEvent {

	private int currentPage;
	
	public CurrentPageEvent(int currentPage) {
		this.setCurrentPage(currentPage);
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
}
