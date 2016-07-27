package com.jianglibo.vaadin.dashboard.event.view;

public class TrashedCheckBoxEvent {

	private boolean checked;

	public TrashedCheckBoxEvent(boolean checked) {
		super();
		this.checked = checked;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	
}
