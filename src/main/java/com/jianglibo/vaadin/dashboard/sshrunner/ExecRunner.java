package com.jianglibo.vaadin.dashboard.sshrunner;

import com.jianglibo.vaadin.dashboard.annotation.Runner;
import com.jianglibo.vaadin.dashboard.domain.JschExecuteResult;

@Runner(name="executor")
public class ExecRunner implements SshRunner {

	@Override
	public JschExecuteResult run(String runContent, String...args) {
		return null;
	}

}
