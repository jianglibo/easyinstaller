package com.jianglibo.vaadin.dashboard.uicomponent.button;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;

@SuppressWarnings("serial")
public abstract class BottomNotifi extends Notification{

	public BottomNotifi(String caption) {
		super(caption);
		setHtmlContentAllowed(true);
		setStyleName("tray dark small closable login-help");
		setPosition(Position.BOTTOM_CENTER);
		setDelayMsec(20000);
		customize();
		show(Page.getCurrent());
	}
	
	public abstract void customize();

}
