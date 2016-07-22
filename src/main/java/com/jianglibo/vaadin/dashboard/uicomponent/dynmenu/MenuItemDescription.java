package com.jianglibo.vaadin.dashboard.uicomponent.dynmenu;

import com.vaadin.server.Resource;

public class MenuItemDescription {
	
	public static enum MenuItemEnableType {
		ONE, MANY, ALWAYS, NONE
	}
	private String itemId;
	
	private Resource icon;
	
	private MenuItemEnableType enableType;
	
	public boolean isEnabled(int num) {
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
	
	
	public MenuItemDescription(String itemId,Resource icon, MenuItemEnableType enableType) {
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

	public MenuItemEnableType getEnableType() {
		return enableType;
	}

	public void setEnableType(MenuItemEnableType enableType) {
		this.enableType = enableType;
	}


	public Resource getIcon() {
		return icon;
	}


	public void setIcon(Resource icon) {
		this.icon = icon;
	}
}
