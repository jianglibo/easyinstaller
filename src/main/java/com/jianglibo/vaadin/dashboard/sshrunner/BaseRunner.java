package com.jianglibo.vaadin.dashboard.sshrunner;

import com.jianglibo.vaadin.dashboard.ssh.JschSession;
import com.jianglibo.vaadin.dashboard.taskrunner.OneThreadTaskDesc;

public interface BaseRunner {
	void run(JschSession jsession, OneThreadTaskDesc oneThreadTaskDesc);
}
