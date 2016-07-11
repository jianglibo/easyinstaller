package com.jianglibo.vaadin.dashboard.view.transactions;

import com.jianglibo.vaadin.dashboard.view.MenuItemWrapper;
import com.jianglibo.vaadin.dashboard.view.ValoMenuItemButton;
import com.vaadin.ui.Component;

//@MainMenu
public class TransactionsViewMenuItem implements MenuItemWrapper {
	
	private Component menuItem;
	
	public TransactionsViewMenuItem() {
		this.menuItem = new ValoMenuItemButton(TransactionsView.VIEW_NAME, TransactionsView.ICON_VALUE);
	}
	
	public Component getMenuItem() {
		return menuItem;
	}
}
