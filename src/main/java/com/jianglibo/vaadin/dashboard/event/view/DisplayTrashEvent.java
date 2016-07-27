package com.jianglibo.vaadin.dashboard.event.view;

public class DisplayTrashEvent {

	private boolean trashView;

	public DisplayTrashEvent(boolean trashView) {
		super();
		this.trashView = trashView;
	}

	public boolean isTrashView() {
		return trashView;
	}

	public void setTrashView(boolean trashView) {
		this.trashView = trashView;
	}
	
	
}
