package com.jianglibo.vaadin.dashboard.uicomponent.dynmenu;

import com.jianglibo.vaadin.dashboard.config.CommonMenuItemIds;
import com.vaadin.server.FontAwesome;

public class RefreshButtonDescription extends SimpleButtonDescription {

	public RefreshButtonDescription() {
		super(CommonMenuItemIds.REFRESH, FontAwesome.REFRESH, ButtonEnableType.ALWAYS);
	}

}
