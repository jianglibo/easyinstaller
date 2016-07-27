package com.jianglibo.vaadin.dashboard.uicomponent.table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;

import com.google.common.eventbus.EventBus;
import com.jianglibo.vaadin.dashboard.event.view.DisplayTrashEvent;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonGroup;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.DynButton;
import com.jianglibo.vaadin.dashboard.uicomponent.pager.Pager;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class TableController extends HorizontalLayout implements ValueChangeListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TableController.class);
	
	@Autowired
	private DynButton menu;
	
	@Autowired
	private Pager pager;
	
	@Autowired
	private MessageSource messageSource;

	private EventBus eventBus;
	
	public TableController afterInjection(EventBus eventBus, ButtonGroup...btnGroups) {
		this.eventBus = eventBus;
		addStyleName("table-controller");
		setWidth("100%");
		addComponent(menu.afterInjection(eventBus, btnGroups));
		
		setComponentAlignment(menu, Alignment.MIDDLE_LEFT);
		CheckBox cb = new CheckBox(messageSource.getMessage("tablecontroller.trashswitcher", null, UI.getCurrent().getLocale()), false);
		cb.setIcon(FontAwesome.TRASH);
		
		cb.addValueChangeListener(this);
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		hl.addComponent(cb);
		hl.addComponent(pager.afterInjection(eventBus));
		addComponent(hl);
		hl.setComponentAlignment(cb, Alignment.MIDDLE_RIGHT);
		setComponentAlignment(hl, Alignment.MIDDLE_RIGHT);
		return this;
	}
	
//	public TableController(EventBus eventBus, MessageSource messageSource, ButtonGroups...btnGroups) {
//		this.eventBus = eventBus;
//		this.messageSource = messageSource;
//		this.pager = new Pager(eventBus, messageSource);
//		addStyleName("table-controller");
//		setWidth("100%");
//		menu = new DynButton(eventBus, messageSource, listener, btnGroups);
//		
//		addComponent(menu);
//		
//		setComponentAlignment(menu, Alignment.MIDDLE_LEFT);
//		CheckBox cb = new CheckBox(messageSource.getMessage("tablecontroller.trashswitcher", null, UI.getCurrent().getLocale()), false);
//		cb.setIcon(FontAwesome.TRASH);
//		
//		cb.addValueChangeListener(this);
//		HorizontalLayout hl = new HorizontalLayout();
//		hl.setSpacing(true);
//		hl.addComponent(cb);
//		hl.addComponent(pager);
//		addComponent(hl);
//		hl.setComponentAlignment(cb, Alignment.MIDDLE_RIGHT);
//		setComponentAlignment(hl, Alignment.MIDDLE_RIGHT);
//	}

	@Override
	public void valueChange(ValueChangeEvent event) {
		eventBus.post(new DisplayTrashEvent((boolean) event.getProperty().getValue()));
	}
}
