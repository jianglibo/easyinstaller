package com.jianglibo.vaadin.dashboard.vo;

import com.vaadin.ui.Field;

public class HandMakeField<T extends Field<?>> {

	private String name;
	
	private T afield;

	public HandMakeField(String name, T afield) {
		super();
		this.name = name;
		this.afield = afield;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public T getAfield() {
		return afield;
	}

	public void setAfield(T afield) {
		this.afield = afield;
	}
	
	
}
