package com.jianglibo.vaadin.dashboard.installer;

import java.util.List;

import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.ssh.JschExecuteResult;

public interface Installer {
	
	List<JschExecuteResult> install(Software software);
}
