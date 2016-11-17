package com.jianglibo.vaadin.dashboard.uicomponent.dynmenu;

import com.jianglibo.vaadin.dashboard.config.CommonMenuItemIds;
import com.vaadin.server.FontAwesome;

public class DeleteButtonDescription extends SimpleButtonDescription {

	public DeleteButtonDescription() {
		super(CommonMenuItemIds.DELETE, FontAwesome.TRASH, ButtonEnableType.MANY);
	}
}
