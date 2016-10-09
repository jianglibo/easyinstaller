package com.jianglibo.vaadin.dashboard.config;

import com.google.common.base.Strings;

public class ComboItem {

	private boolean localized;
	private String caption;
	private String value;
	private String valueType = "String";

	public String getCaption() {
		if (Strings.isNullOrEmpty(this.caption)) {
			return getValue();
		}
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public boolean isLocalized() {
		return localized;
	}

	public void setLocalized(boolean localized) {
		this.localized = localized;
	}
}
