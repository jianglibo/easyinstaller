package com.jianglibo.vaadin.dashboard.view.schedule;

import com.jianglibo.vaadin.dashboard.annotation.MainMenu;
import com.jianglibo.vaadin.dashboard.view.MenuItemWrapper;
import com.jianglibo.vaadin.dashboard.view.ValoMenuItemButton;
import com.vaadin.ui.Component;

@MainMenu(menuOrder=15)
public class ScheduleViewMenuItem implements MenuItemWrapper {
	
	private Component menuItem;
	
	public ScheduleViewMenuItem() {
		this.menuItem = new ValoMenuItemButton(ScheduleView.VIEW_NAME, ScheduleView.ICON_VALUE);
	}
	
	public Component getMenuItem() {
		return menuItem;
	}

	@Override
	public void onAttach() {
		// TODO Auto-generated method stub
		
	}
}
