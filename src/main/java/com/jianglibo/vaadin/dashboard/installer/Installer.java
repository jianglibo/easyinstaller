package com.jianglibo.vaadin.dashboard.installer;

import java.util.List;

import com.jianglibo.vaadin.dashboard.domain.JschExecuteResult;
import com.jianglibo.vaadin.dashboard.domain.Software;

public interface Installer {
	
	List<JschExecuteResult> install(Software software);
}
