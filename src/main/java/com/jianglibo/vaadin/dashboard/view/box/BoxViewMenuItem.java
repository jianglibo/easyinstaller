package com.jianglibo.vaadin.dashboard.view.box;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.annotation.MainMenu;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.view.MenuItemWrapper;
import com.jianglibo.vaadin.dashboard.view.ValoMenuItemButton;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Component;

@MainMenu(menuOrder = 200)
public class BoxViewMenuItem implements MenuItemWrapper {

	private Component menuItem;

	private final MessageSource messageSource;
	
	
	@Autowired
	public BoxViewMenuItem(MessageSource messageSource) {
		this.messageSource = messageSource;
		this.menuItem = new ValoMenuItemButton(BoxListView.VIEW_NAME, FontAwesome.CUBE, messageSource);
	}
	
	public Component getMenuItem() {
		return menuItem;
	}

	@Override
	public void onAttach() {
	}
}
