package com.jianglibo.vaadin.dashboard.event.view;

public class DynMenuClickEvent {

	private String btnId;

	public DynMenuClickEvent(String btnId) {
		super();
		this.btnId = btnId;
	}

	public String getBtnId() {
		return btnId;
	}

	public void setBtnId(String btnId) {
		this.btnId = btnId;
	}
	
	
}
