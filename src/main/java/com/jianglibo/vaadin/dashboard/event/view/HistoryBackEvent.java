package com.jianglibo.vaadin.dashboard.event.view;

public class HistoryBackEvent {
	
	private String id;
	
	public HistoryBackEvent(){}
	
	public HistoryBackEvent(String id){
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
