package com.jianglibo.vaadin.dashboard.view.boxes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.annotation.MainMenu;
import com.jianglibo.vaadin.dashboard.view.MenuItemWrapper;
import com.jianglibo.vaadin.dashboard.view.ValoMenuItemButton;
import com.vaadin.ui.Component;

@MainMenu(menuOrder = 10)
public class BoxViewMenuItem implements MenuItemWrapper {

	private Component menuItem;

	private final MessageSource messageSource;
	
	
	@Autowired
	public BoxViewMenuItem(MessageSource messageSource) {
		this.messageSource = messageSource;
		this.menuItem = new ValoMenuItemButton(BoxView.VIEW_NAME, BoxView.ICON_VALUE, messageSource);
	}
	
	public Component getMenuItem() {
		return menuItem;
	}

	@Override
	public void onAttach() {
		// TODO Auto-generated method stub
		
	}
}
