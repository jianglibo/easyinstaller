package com.jianglibo.vaadin.dashboard.view.software;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.annotation.MainMenu;
import com.jianglibo.vaadin.dashboard.view.MenuItemWrapper;
import com.jianglibo.vaadin.dashboard.view.ValoMenuItemButton;
import com.vaadin.ui.Component;

@MainMenu(menuOrder = 10)
public class SoftwareViewMenuItem implements MenuItemWrapper {

	private Component menuItem;

	private final MessageSource messageSource;
	
	
	@Autowired
	public SoftwareViewMenuItem(MessageSource messageSource) {
		this.messageSource = messageSource;
		this.menuItem = new ValoMenuItemButton(SoftwareView.VIEW_NAME, SoftwareView.ICON_VALUE, messageSource);
	}
	
	public Component getMenuItem() {
		return menuItem;
	}

	@Override
	public void onAttach() {
		// TODO Auto-generated method stub
		
	}
}
