package com.jianglibo.vaadin.dashboard.sshrunner;

import org.springframework.stereotype.Component;

import com.jianglibo.vaadin.dashboard.annotation.Runner;
import com.jianglibo.vaadin.dashboard.domain.JschExecuteResult;
import com.jianglibo.vaadin.dashboard.domain.StepRun;

/**
 * It must upload execute content to server before execute.
 * A working folder to hold uploaded code file is needed.
 * 
 * @author jianglibo@gmail.com
 *
 */
@Runner(SshExecRunner.UNIQUE_NAME)
@Component(SshExecRunner.UNIQUE_NAME)
public class SshExecRunner implements BaseRunner {
	
	public static final String UNIQUE_NAME = "SshExecRunner";

	@Override
	public JschExecuteResult run(StepRun stepRun) {
		return null;
	}

	@Override
	public String uniqueName() {
		return UNIQUE_NAME;
	}

}
