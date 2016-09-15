package com.jianglibo.vaadin.dashboard.uicomponent.button;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;

public class NotificationBuilder {
	private String styles = "tray dark small closable login-help";

	private Position position = Position.BOTTOM_CENTER;

	private boolean htmlContentAllowed = true;

	private int delayMsec = 2000;

	private final String notifyId;

	private final MessageSource messageSource;

	public NotificationBuilder(MessageSource messageSource, String notifyId) {
		this.messageSource = messageSource;
		this.notifyId = notifyId;
	}
	

//    "Welcome to Dashboard Demo"
//	"<span>This application is not real, it only demonstrates an application built with the <a href=\"https://vaadin.com\">Vaadin framework</a>.</span> <span>No username or password is required, just click the <b>Sign In</b> button to continue.</span>"	
	
	public Notification build() {
		String cp = MsgUtil.getBottomNotifiMsg(messageSource, notifyId + ".caption");
		String de = MsgUtil.getBottomNotifiMsg(messageSource, notifyId + ".description");
		
        Notification notification = new Notification(cp);
        notification.setDescription(de);
        notification.setHtmlContentAllowed(htmlContentAllowed);
        notification.setStyleName(styles);
        notification.setPosition(position);
        notification.setDelayMsec(delayMsec);
        return notification;
	}
	

	
	public void buildAndShow() {
		build().show(Page.getCurrent());
	}

	public NotificationBuilder setStyles(String styles) {
		this.styles = styles;
		return this;
	}


	public NotificationBuilder setPosition(Position position) {
		this.position = position;
		return this;
	}


	public NotificationBuilder setHtmlContentAllowed(boolean htmlContentAllowed) {
		this.htmlContentAllowed = htmlContentAllowed;
		return this;
	}

	public NotificationBuilder setDelayMsec(int delayMsec) {
		this.delayMsec = delayMsec;
		return this;
	}
}
