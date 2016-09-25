package com.jianglibo.vaadin.dashboard.view.software;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.google.common.eventbus.Subscribe;
import com.jianglibo.vaadin.dashboard.annotation.MainMenu;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEventBus;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEvent.SoftwareNumberChangeEvent;
import com.jianglibo.vaadin.dashboard.view.DboardViewUtil;
import com.jianglibo.vaadin.dashboard.view.MenuItemWrapper;
import com.jianglibo.vaadin.dashboard.view.ValoMenuItemButton;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

@MainMenu(menuOrder = 500)
public class SoftwareViewMenuItem implements MenuItemWrapper {

	private Component menuItem;

	private final MessageSource messageSource;
	
	private Label notificationsBadge;
	
	private int unreadNotificationsCount = 0;
	
	@Autowired
	public SoftwareViewMenuItem(MessageSource messageSource) {
		this.messageSource = messageSource;
		
        this.notificationsBadge = new Label();
        this.menuItem = DboardViewUtil.buildBadgeWrapper(new ValoMenuItemButton(SoftwareListView.VIEW_NAME, SoftwareListView.ICON_VALUE, messageSource),
                notificationsBadge);
        DashboardEventBus.register(this);
	}
	
	public Component getMenuItem() {
		return menuItem;
	}
	
	@Subscribe
	public void updateNotificationsCount(final SoftwareNumberChangeEvent event) {
		if (event != null) {
			if (event.getNumber() == 1) {
				unreadNotificationsCount++; 
			} else {
				unreadNotificationsCount = 0;
			}
		}
		notificationsBadge.setValue(String.valueOf(unreadNotificationsCount));
		notificationsBadge.setVisible(unreadNotificationsCount > 0);
	}
	
	@Override
	public void onAttach() {
		updateNotificationsCount(null);
	}
}
