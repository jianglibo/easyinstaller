package com.jianglibo.vaadin.dashboard.event.view;

import java.util.Collection;

public class TableSelectChangeEvent<T> {
	
	private Collection<T> selected;

	public TableSelectChangeEvent(Collection<T> selected) {
		super();
		this.selected = selected;
	}

	public Collection<T> getSelected() {
		return selected;
	}

	public void setSelected(Collection<T> selected) {
		this.selected = selected;
	}
	
	
}
