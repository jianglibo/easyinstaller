package com.jianglibo.vaadin.dashboard.view.globalsetting;

import com.jianglibo.vaadin.dashboard.annotation.MainMenu;
import com.jianglibo.vaadin.dashboard.view.MenuItemWrapper;
import com.jianglibo.vaadin.dashboard.view.ValoMenuItemButton;
import com.vaadin.ui.Component;

@MainMenu
public class GlobalSettingViewMenuItem implements MenuItemWrapper {
	
	private Component menuItem;
	
	public GlobalSettingViewMenuItem() {
		this.menuItem = new ValoMenuItemButton(GlobalSettingView.VIEW_NAME, GlobalSettingView.ICON_VALUE);
	}
	
	public Component getMenuItem() {
		return menuItem;
	}

	@Override
	public void onAttach() {
		// TODO Auto-generated method stub
		
	}
}
