package com.jianglibo.vaadin.dashboard.uicomponent.dynmenu;

import com.jianglibo.vaadin.dashboard.config.CommonMenuItemIds;
import com.vaadin.server.FontAwesome;

public class EditButtonDescription extends SimpleButtonDescription {

	public EditButtonDescription() {
		super(CommonMenuItemIds.EDIT, FontAwesome.EDIT, ButtonEnableType.ONE);
	}

}
