package com.jianglibo.vaadin.dashboard.util;

import org.springframework.context.MessageSource;

import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

public class NotificationUtil {

	public static void humanized(MessageSource messageSource, String msg) {
		Notification.show(MsgUtil.getMsgFallbackToSelf(messageSource, "nofitication.humanized.", msg), "", Type.HUMANIZED_MESSAGE);
	}
}
