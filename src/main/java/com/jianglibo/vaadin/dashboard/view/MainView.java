package com.jianglibo.vaadin.dashboard.view;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;

/*
 * Dashboard MainView is a simple HorizontalLayout that wraps the menu on the
 * left and creates a simple container for the navigator on the right.
 */
@SuppressWarnings("serial")
public class MainView extends HorizontalLayout {
	
	@Autowired @Named("contentContainer")
	private ComponentContainer content;

    public MainView() {
        setSizeFull();
        addStyleName("mainview");
        addComponent(new DashboardMenu());
//        ComponentContainer content = new CssLayout();
//        content.addStyleName("view-content");
//        content.setSizeFull();
        addComponent(content);
        setExpandRatio(content, 1.0f);
//        new DashboardNavigator(content);
    }
}
