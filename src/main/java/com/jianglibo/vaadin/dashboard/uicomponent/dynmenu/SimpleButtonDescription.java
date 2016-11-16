package com.jianglibo.vaadin.dashboard.uicomponent.dynmenu;

import java.util.Set;

import com.vaadin.server.Resource;

public class SimpleButtonDescription implements ButtonDescription {

	private String itemId;
	
	private Resource icon;
	
	private ButtonEnableType enableType;
	
	public boolean isEnabled(Set<Object> selected) {
		int num = selected.size();
		switch (enableType) {
		case ONE:
			return num == 1;
		case MANY:
			return num > 0;
		case ALWAYS:
			return true;
		case NONE:
			return num == 0;
		default:
			return false;
		}
	}
	
	
	public SimpleButtonDescription(String itemId,Resource icon, ButtonEnableType enableType) {
		this.itemId = itemId;
		this.enableType = enableType;
		this.setIcon(icon);
	}


	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public ButtonEnableType getEnableType() {
		return enableType;
	}

	public void setEnableType(ButtonEnableType enableType) {
		this.enableType = enableType;
	}


	public Resource getIcon() {
		return icon;
	}


	public void setIcon(Resource icon) {
		this.icon = icon;
	}
}
