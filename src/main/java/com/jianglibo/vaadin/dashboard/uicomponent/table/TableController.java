package com.jianglibo.vaadin.dashboard.uicomponent.table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import com.google.common.eventbus.EventBus;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonGroups;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.DynButton;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.DynMenuListener;
import com.jianglibo.vaadin.dashboard.uicomponent.pager.Pager;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class TableController extends HorizontalLayout implements SelectionChangeLinster, ValueChangeListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TableController.class);
	
	private DynButton menu;
	
	private Pager pager;
	
	private MessageSource messageSource;
	
	private DynMenuListener listener;
	
	private EventBus eventBus;
	
	public TableController(EventBus eventBus,Pager pager, MessageSource messageSource, TableControllerListener listener, ButtonGroups...btnGroups) {
		this.eventBus = eventBus;
		this.messageSource = messageSource;
		this.listener = listener;
		addStyleName("table-controller");
		setWidth("100%");
		menu = new DynButton(eventBus, messageSource, listener, btnGroups);
		
		this.pager = pager;
		addComponent(menu);
		
		setComponentAlignment(menu, Alignment.MIDDLE_LEFT);
		CheckBox cb = new CheckBox(messageSource.getMessage("tablecontroller.trashswitcher", null, UI.getCurrent().getLocale()), false);
		cb.setIcon(FontAwesome.TRASH);
		
		cb.addValueChangeListener(this);
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		hl.addComponent(cb);
		hl.addComponent(pager);
		addComponent(hl);
		hl.setComponentAlignment(cb, Alignment.MIDDLE_RIGHT);
		setComponentAlignment(hl, Alignment.MIDDLE_RIGHT);
	}

	@Override
	public void onSelectionChange(int num) {
		this.menu.onSelectionChange(num);
	}

	@Override
	public void valueChange(ValueChangeEvent event) {
		LOGGER.info("checkbox value: {}", event.getProperty().getValue());
		
	}
}
