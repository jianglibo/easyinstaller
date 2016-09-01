package com.jianglibo.vaadin.dashboard.event.ui;

public class TwinGridFieldItemClickEvent {

	private Object itemValue;
	
	private boolean leftClicked;

	public TwinGridFieldItemClickEvent(Object itemValue, boolean leftClicked) {
		super();
		this.itemValue = itemValue;
		this.leftClicked = leftClicked;
	}
	
	public Object getItemValue() {
		return itemValue;
	}

	public void setItemValue(Object itemValue) {
		this.itemValue = itemValue;
	}

	public boolean isLeftClicked() {
		return leftClicked;
	}

	public void setLeftClicked(boolean leftClicked) {
		this.leftClicked = leftClicked;
	}
}
