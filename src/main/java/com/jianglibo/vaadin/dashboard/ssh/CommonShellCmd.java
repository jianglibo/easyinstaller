package com.jianglibo.vaadin.dashboard.ssh;

import com.jianglibo.vaadin.dashboard.domain.JschExecuteResult.ResultType;

public class CommonShellCmd {
	
	public static boolean isAppInstalled(JschSession session, String appExecutable) {
		return session.exec("which " + appExecutable).getState() == ResultType.ZERO;
	}
	
	public static boolean isAppInstalled(JschSession session,String detectApp, String appExecutable) {
		return session.exec(detectApp + " " + appExecutable).getState() == ResultType.ZERO;
	}

}
