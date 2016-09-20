package com.jianglibo.vaadin.dashboard.unused;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.view.MenuItemWrapper;
import com.jianglibo.vaadin.dashboard.view.ValoMenuItemButton;
import com.vaadin.ui.Component;

public class StepDefineViewMenuItem implements MenuItemWrapper {

	private Component menuItem;

	private final MessageSource messageSource;
	
	
	@Autowired
	public StepDefineViewMenuItem(MessageSource messageSource) {
		this.messageSource = messageSource;
		this.menuItem = new ValoMenuItemButton(StepDefineListView.VIEW_NAME, StepDefineListView.ICON_VALUE, messageSource);
	}
	
	public Component getMenuItem() {
		return menuItem;
	}

	@Override
	public void onAttach() {
		
	}
}
