package com.jianglibo.vaadin.dashboard.config;

import java.util.List;

public class ComboBoxData {
	
	private List<ComboItem> items;

	public List<ComboItem> getItems() {
		return items;
	}

	public void setItems(List<ComboItem> items) {
		this.items = items;
	}

	public static class ComboItem {
		private boolean localized;
		private String caption;
		private String value;
		private String valueType;
		
		public String getCaption() {
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
	

}
