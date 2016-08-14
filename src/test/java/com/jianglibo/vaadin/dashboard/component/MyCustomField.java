package com.jianglibo.vaadin.dashboard.component;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

@SuppressWarnings("serial")
public class MyCustomField extends CustomField<String> {

	public MyCustomField() {

	}

	@Override
	protected Component initContent() {
		return null;
	}

	@Override
	public Class<? extends String> getType() {
		return null;
	}
}
