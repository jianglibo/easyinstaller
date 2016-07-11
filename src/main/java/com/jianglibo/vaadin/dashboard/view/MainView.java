package com.jianglibo.vaadin.dashboard.view;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;

import com.jianglibo.vaadin.dashboard.DashboardNavigator;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;

/*
 * Dashboard MainView is a simple HorizontalLayout that wraps the menu on the
 * left and creates a simple container for the navigator on the right.
 */
@SuppressWarnings("serial")
@SpringComponent
@UIScope
public class MainView extends HorizontalLayout {
	
	@Autowired 
    public MainView(DashboardMenu dashboardMenu,@Named("contentContainer") ComponentContainer content, DashboardNavigator dn) {
        setSizeFull();
        addStyleName("mainview");
        addComponent(dashboardMenu);
//        ComponentContainer content = new CssLayout();
//        content.addStyleName("view-content");
//        content.setSizeFull();
        addComponent(content);
        setExpandRatio(content, 1.0f);
        dn.start();
//        new DashboardNavigator(content);
    }
}
