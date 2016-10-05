package com.jianglibo.vaadin.dashboard.view.boxgrouphistory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.google.common.eventbus.Subscribe;
import com.jianglibo.vaadin.dashboard.annotation.MainMenu;
import com.jianglibo.vaadin.dashboard.domain.BoxHistory;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEventBus;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEvent.NotificationsCountUpdatedEvent;
import com.jianglibo.vaadin.dashboard.view.DboardViewUtil;
import com.jianglibo.vaadin.dashboard.view.MenuItemWrapper;
import com.jianglibo.vaadin.dashboard.view.ValoMenuItemButton;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

@MainMenu(menuOrder = 700)
public class BoxGroupHistoryViewMenuItem implements MenuItemWrapper {

	private Component menuItem;

	private final MessageSource messageSource;
	
	private Label notificationsBadge;
	
	
	@Autowired
	public BoxGroupHistoryViewMenuItem(MessageSource messageSource) {
		this.messageSource = messageSource;
        this.notificationsBadge = new Label();
        this.menuItem = DboardViewUtil.buildBadgeWrapper(new ValoMenuItemButton(BoxGroupHistoryListView.VIEW_NAME, BoxGroupHistoryListView.ICON_VALUE, messageSource),
                notificationsBadge);
        DashboardEventBus.register(this);
	}
	
	public Component getMenuItem() {
		return menuItem;
	}
	
	@Subscribe
	public void updateNotificationsCount(final NotificationsCountUpdatedEvent event) {
		int unreadNotificationsCount = 55; //read from other source.
		notificationsBadge.setValue(String.valueOf(unreadNotificationsCount));
		notificationsBadge.setVisible(unreadNotificationsCount > 0);
	}

	@Override
	public void onAttach() {
		updateNotificationsCount(null);
	}
}
