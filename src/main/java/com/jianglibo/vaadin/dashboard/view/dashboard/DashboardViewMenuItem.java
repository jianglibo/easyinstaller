package com.jianglibo.vaadin.dashboard.view.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.google.common.eventbus.Subscribe;
import com.jianglibo.vaadin.dashboard.DashboardUI;
import com.jianglibo.vaadin.dashboard.annotation.MainMenu;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEventBus;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEvent.NotificationsCountUpdatedEvent;
import com.jianglibo.vaadin.dashboard.view.DboardViewUtil;
import com.jianglibo.vaadin.dashboard.view.MenuItemWrapper;
import com.jianglibo.vaadin.dashboard.view.ValoMenuItemButton;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

@MainMenu(menuOrder = 100)
public class DashboardViewMenuItem implements MenuItemWrapper {
	
	public static final String NOTIFICATIONS_BADGE_ID = "dashboard-menu-notifications-badge";
	
	private Component menuItem;
	
	private Label notificationsBadge;
	
	private final MessageSource messageSource;

	@Autowired
	public DashboardViewMenuItem(MessageSource messageSource) {
		this.messageSource = messageSource;
		Component menuItemComponent = new ValoMenuItemButton(DashboardView.VIEW_NAME, DashboardView.ICON_VALUE, messageSource);
        notificationsBadge = new Label();
        notificationsBadge.setId(NOTIFICATIONS_BADGE_ID);
        this.menuItem = DboardViewUtil.buildBadgeWrapper(menuItemComponent,
                notificationsBadge);
        DashboardEventBus.register(this);
	}
	
//	@Subscribe
//	public void updateNotificationsCount(final NotificationsCountUpdatedEvent event) {
//		int unreadNotificationsCount = DashboardUI.getDataProvider().getUnreadNotificationsCount();
//		notificationsBadge.setValue(String.valueOf(unreadNotificationsCount));
//		notificationsBadge.setVisible(unreadNotificationsCount > 0);
//	}

	public Component getMenuItem() {
		return menuItem;
	}

	@Override
	public void onAttach() {
//		updateNotificationsCount(null);
	}
}
