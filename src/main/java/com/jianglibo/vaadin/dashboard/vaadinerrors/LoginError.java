package com.jianglibo.vaadin.dashboard.vaadinerrors;

import com.vaadin.server.ErrorMessage;

public class LoginError implements ErrorMessage {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public ErrorLevel getErrorLevel() {
		return ErrorLevel.WARNING;
	}

	@Override
	public String getFormattedHtmlMessage() {
		return "用户名或密码错误！";
	}

}
