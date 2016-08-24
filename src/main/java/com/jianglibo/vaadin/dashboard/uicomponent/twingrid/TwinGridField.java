package com.jianglibo.vaadin.dashboard.uicomponent.twingrid;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

@SuppressWarnings("serial")
public class TwinGridField<T> extends CustomField<T>{

	@Override
	protected Component initContent() {
		return new TwinGridLayout();
	}

	@Override
	public Class<? extends T> getType() {
		return null;
	}
}
