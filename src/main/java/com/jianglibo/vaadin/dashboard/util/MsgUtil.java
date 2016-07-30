package com.jianglibo.vaadin.dashboard.util;

import org.springframework.context.MessageSource;

import com.vaadin.ui.UI;

public class MsgUtil {
	
	public static String getMsg(MessageSource ms, String key) {
		return ms.getMessage(key, null, UI.getCurrent().getLocale());
	}
}
