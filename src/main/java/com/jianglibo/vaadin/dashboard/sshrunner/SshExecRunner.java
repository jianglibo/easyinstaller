package com.jianglibo.vaadin.dashboard.sshrunner;

import com.jianglibo.vaadin.dashboard.annotation.Runner;
import com.jianglibo.vaadin.dashboard.domain.JschExecuteResult;

@Runner(SshExecRunner.UNIQUE_NAME)
public class SshExecRunner implements BaseRunner {
	
	public static final String UNIQUE_NAME = "SshExecRunner";

	@Override
	public JschExecuteResult run(String runContent, String...args) {
		return null;
	}

	@Override
	public String uniqueName() {
		return UNIQUE_NAME;
	}

}
