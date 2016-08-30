package com.jianglibo.vaadin.dashboard.sshrunner;

import com.jianglibo.vaadin.dashboard.domain.JschExecuteResult;

public interface SshRunner {

	JschExecuteResult run(String runContent, String...args);
}
