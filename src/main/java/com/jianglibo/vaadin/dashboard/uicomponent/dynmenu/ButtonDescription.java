package com.jianglibo.vaadin.dashboard.uicomponent.dynmenu;

import java.util.Set;

import com.vaadin.server.Resource;

public interface ButtonDescription {
	public static enum ButtonEnableType {
		ONE, MANY, ALWAYS, NONE
	}

	boolean isEnabled(Set<Object> selected);

	String getItemId();

	Resource getIcon();
}
