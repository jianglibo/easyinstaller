package com.jianglibo.vaadin.dashboard.sshrunner;

import com.jianglibo.vaadin.dashboard.domain.JschExecuteResult;
import com.jianglibo.vaadin.dashboard.domain.StepRun;
import com.jianglibo.vaadin.dashboard.ssh.JschSession;

public interface BaseRunner {

	JschExecuteResult run(JschSession jsession, StepRun stepRun);
	
	String uniqueName();
}
