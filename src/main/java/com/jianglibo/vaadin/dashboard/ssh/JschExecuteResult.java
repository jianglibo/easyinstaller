package com.jianglibo.vaadin.dashboard.ssh;

public interface JschExecuteResult {
	public static enum ResultType {
		ZERO, NONE_ZERO, EXP
	}
	int getExitStatus();
	
	String getCmdOut();

	ResultType getState();
}
