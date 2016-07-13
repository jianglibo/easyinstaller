package com.jianglibo.vaadin.dashboard.view.globalsetting;

import com.jianglibo.vaadin.dashboard.event.DashboardEventBus;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@SpringView(name=GlobalSettingView.VIEW_NAME)
public class GlobalSettingView extends Panel implements View {
	
	public static final String VIEW_NAME = "globalsetting";
	public static final FontAwesome ICON_VALUE = FontAwesome.WRENCH;
	
	public GlobalSettingView() {
        setSizeFull();
        addStyleName("schedule");
        DashboardEventBus.register(this);
        Label l = new Label("hello");
        Layout ly = new VerticalLayout(l);
        setContent(ly);
	}

	@Override
	public void enter(ViewChangeEvent event) {

		
	}

}
