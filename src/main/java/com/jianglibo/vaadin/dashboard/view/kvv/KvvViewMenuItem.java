package com.jianglibo.vaadin.dashboard.view.kvv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.annotation.MainMenu;
import com.jianglibo.vaadin.dashboard.view.ValoMenuItemButton;
import com.jianglibo.vaadin.dashboard.view.menuatleft.MenuItemWrapper;
import com.vaadin.ui.Component;

@MainMenu(menuOrder = 700)
public class KvvViewMenuItem implements MenuItemWrapper {

	private Component menuItem;

	private final MessageSource messageSource;
	
	
	@Autowired
	public KvvViewMenuItem(MessageSource messageSource) {
		this.messageSource = messageSource;
		this.menuItem = new ValoMenuItemButton(KkvListView.VIEW_NAME, KkvListView.ICON_VALUE, messageSource);
	}
	
	public Component getMenuItem() {
		return menuItem;
	}

	@Override
	public void onAttach() {
		// TODO Auto-generated method stub
		
	}
}
