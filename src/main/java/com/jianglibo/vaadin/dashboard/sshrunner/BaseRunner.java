package com.jianglibo.vaadin.dashboard.sshrunner;

import com.jianglibo.vaadin.dashboard.domain.JschExecuteResult;
import com.jianglibo.vaadin.dashboard.domain.StepRun;

public interface BaseRunner {

	JschExecuteResult run(StepRun stepRun);
	
	String uniqueName();
}
