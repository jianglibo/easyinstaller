package com.jianglibo.vaadin.dashboard.uicomponent.table;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.DynMenu;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.DynMenuListener;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.MenuItemDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.pager.Pager;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;

@SuppressWarnings("serial")
public class TableController extends HorizontalLayout implements SelectionChangeLinster {
	
	private DynMenu menu;
	
	private Pager pager;
	
	private MessageSource messageSource;
	private DynMenuListener listener;
	private MenuItemDescription[] menuItemDescriptions;
	
	public TableController(MessageSource messageSource, DynMenuListener listener, MenuItemDescription...menuItemDescriptions) {
		this.messageSource = messageSource;
		this.listener = listener;
		this.menuItemDescriptions = menuItemDescriptions;
		addStyleName("table-controller");
		setWidth("100%");
		menu = new DynMenu(messageSource, listener, menuItemDescriptions);
		pager = new Pager();
		addComponent(menu);
		addComponent(pager);
		setComponentAlignment(menu, Alignment.MIDDLE_LEFT);
		setComponentAlignment(pager, Alignment.MIDDLE_RIGHT);
	}

	@Override
	public void onSelectionChange(int num) {
		this.menu.onSelectionChange(num);
	}
}
