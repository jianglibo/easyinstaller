package com.jianglibo.vaadin.dashboard.view.boxgroup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.annotation.MainMenu;
import com.jianglibo.vaadin.dashboard.domain.BoxGroup;
import com.jianglibo.vaadin.dashboard.view.ValoMenuItemButton;
import com.jianglibo.vaadin.dashboard.view.menuatleft.MenuItemWrapper;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Component;

@MainMenu(menuOrder = 300)
public class BoxGroupViewMenuItem implements MenuItemWrapper {

	private Component menuItem;

	private final MessageSource messageSource;
	
	
	@Autowired
	public BoxGroupViewMenuItem(MessageSource messageSource) {
		this.messageSource = messageSource;
		this.menuItem = new ValoMenuItemButton(BoxGroupListView.VIEW_NAME, FontAwesome.CUBES, messageSource);
	}
	
	public Component getMenuItem() {
		return menuItem;
	}

	@Override
	public void onAttach() {
		// TODO Auto-generated method stub
		
	}
}
