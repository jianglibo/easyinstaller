package com.jianglibo.vaadin.dashboard.wrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.jianglibo.vaadin.dashboard.DashboardNavigator;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.ComponentContainer;

@SpringComponent
@Scope("prototype")
public class DashboardNavigatorWrapper {

	private final DashboardNavigator dn;
	
	@Autowired
	public DashboardNavigatorWrapper(DashboardNavigator dn, SpringViewProvider provider) {
		this.dn = dn;
	}
	
	public DashboardNavigator unwrap(ComponentContainer container) {
		dn.setup(container);
		return dn;
	}
	
	
}
