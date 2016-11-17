package com.jianglibo.vaadin.dashboard.uicomponent.dynmenu;

import com.jianglibo.vaadin.dashboard.config.CommonMenuItemIds;
import com.vaadin.server.FontAwesome;

public class AddButtonDescription extends SimpleButtonDescription {

	public AddButtonDescription() {
		super(CommonMenuItemIds.ADD, FontAwesome.PLUS, ButtonEnableType.ALWAYS);
	}
}
