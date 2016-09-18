package com.jianglibo.vaadin.dashboard.sshrunner;

import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.ssh.JschSession;
import com.jianglibo.vaadin.dashboard.taskrunner.TaskDesc;

public interface BaseRunner {
	void run(JschSession jsession,Box box, TaskDesc taskDesc);
}
