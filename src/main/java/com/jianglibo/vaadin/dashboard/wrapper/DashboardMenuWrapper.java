package com.jianglibo.vaadin.dashboard.wrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.jianglibo.vaadin.dashboard.view.DashboardMenu;
import com.vaadin.spring.annotation.SpringComponent;

@SpringComponent
@Scope("prototype")
public class DashboardMenuWrapper implements Wrapper<DashboardMenu> {
	
	private final DashboardMenu dm;
	
	@Autowired
	public DashboardMenuWrapper(DashboardMenu dm) {
		this.dm = dm;
	}

	@Override
	public DashboardMenu unwrap() {
		dm.setup();
		return dm;
	}
}
