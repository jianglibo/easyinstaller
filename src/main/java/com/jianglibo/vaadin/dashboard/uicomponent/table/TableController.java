package com.jianglibo.vaadin.dashboard.uicomponent.table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jianglibo.vaadin.dashboard.config.InterestInUriFragemnt;
import com.jianglibo.vaadin.dashboard.event.view.TrashedCheckBoxEvent;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonGroup;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.DynButton;
import com.jianglibo.vaadin.dashboard.uicomponent.pager.Pager;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class TableController extends HorizontalLayout implements /*ValueChangeListener,*/ InterestInUriFragemnt {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TableController.class);
	
	@Autowired
	private DynButton menu;
	
	@Autowired
	private Pager pager;
	
	@Autowired
	private MessageSource messageSource;
	
//	private CheckBox cb;
	
	private Button trashBt;

	private EventBus eventBus;
	
	public TableController afterInjection(EventBus eventBus, ButtonGroup...btnGroups) {
		this.eventBus = eventBus;
		this.eventBus.register(this);
		addStyleName("table-controller");
		setWidth("100%");
		addComponent(menu.afterInjection(eventBus, btnGroups));
		
		setComponentAlignment(menu, Alignment.MIDDLE_LEFT);
		trashBt = new Button(messageSource.getMessage("tablecontroller.trashswitcher", null, UI.getCurrent().getLocale()), FontAwesome.TRASH);
		
		trashBt.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				String styles = event.getButton().getStyleName();
				if (StyleUtil.hasStyleName(styles, ValoTheme.BUTTON_PRIMARY)) {
					eventBus.post(new TrashedCheckBoxEvent(false));
				} else {
					eventBus.post(new TrashedCheckBoxEvent(true));
				}
			}
		});
//		cb = new CheckBox(messageSource.getMessage("tablecontroller.trashswitcher", null, UI.getCurrent().getLocale()), false);
//		cb.setIcon(FontAwesome.TRASH);
//		
//		cb.addValueChangeListener(this);
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		hl.addComponent(trashBt);
		hl.addComponent(pager.afterInjection(eventBus));
		addComponent(hl);
		hl.setComponentAlignment(trashBt, Alignment.MIDDLE_RIGHT);
		setComponentAlignment(hl, Alignment.MIDDLE_RIGHT);
		return this;
	}

//	@Override
//	public void valueChange(ValueChangeEvent event) {
//		if (!checkEventFromUri) {
//			eventBus.post(new TrashedCheckBoxEvent((boolean) event.getProperty().getValue()));
//		}
//		checkEventFromUri = false;
//	}

	@Override
	@Subscribe
	public void whenUriFragementChange(ListViewFragmentBuilder vfb) {
		boolean trashed = vfb.getBoolean(ListViewFragmentBuilder.TRASHED_PARAM_NAME);
//		checkEventFromUri = true;
		if (trashed) {
			trashBt.addStyleName(ValoTheme.BUTTON_PRIMARY);
		} else {
			trashBt.removeStyleName(ValoTheme.BUTTON_PRIMARY);
		}
	}
}
