package com.jianglibo.vaadin.dashboard.uicomponent.table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jianglibo.vaadin.dashboard.config.InterestInUriFragemnt;
import com.jianglibo.vaadin.dashboard.event.view.TrashedCheckBoxEvent;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonGroup;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.DynButtonComponent;
import com.jianglibo.vaadin.dashboard.uicomponent.pager.Pager;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.jianglibo.vaadin.dashboard.view.ListView;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class TableController extends HorizontalLayout implements /*ValueChangeListener,*/ InterestInUriFragemnt {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TableController.class);
	
	
	private DynButtonComponent menu;
	
	private Pager pager;
	
	private MessageSource messageSource;
	
	private Button trashBt;

	public TableController(MessageSource messageSource, ListView listview) {
		this.messageSource = messageSource;
		this.menu = listview.getDynButtonComponent();
		this.pager = listview.getPager();
		
		addStyleName("table-controller");
		setWidth("100%");
		addComponent(menu);
		
		setComponentAlignment(menu, Alignment.MIDDLE_LEFT);
		trashBt = new Button(messageSource.getMessage("tablecontroller.trashswitcher", null, UI.getCurrent().getLocale()), FontAwesome.TRASH);
		
		trashBt.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				String styles = event.getButton().getStyleName();
				if (StyleUtil.hasStyleName(styles, ValoTheme.BUTTON_PRIMARY)) {
					listview.trashBtnClicked(false);
				} else {
					listview.trashBtnClicked(true);
				}
			}
		});
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		hl.addComponent(trashBt);
		hl.addComponent(pager);
		addComponent(hl);
		hl.setComponentAlignment(trashBt, Alignment.MIDDLE_RIGHT);
		setComponentAlignment(hl, Alignment.MIDDLE_RIGHT);
		
	}
	
//	public TableController afterInjection(MessageSource messageSource, ListView listview) {
//		this.eventBus.register(this);
//		addStyleName("table-controller");
//		setWidth("100%");
//		addComponent(menu.afterInjection(listview));
//		
//		setComponentAlignment(menu, Alignment.MIDDLE_LEFT);
//		trashBt = new Button(messageSource.getMessage("tablecontroller.trashswitcher", null, UI.getCurrent().getLocale()), FontAwesome.TRASH);
//		
//		trashBt.addClickListener(new ClickListener() {
//			@Override
//			public void buttonClick(ClickEvent event) {
//				String styles = event.getButton().getStyleName();
//				if (StyleUtil.hasStyleName(styles, ValoTheme.BUTTON_PRIMARY)) {
//					eventBus.post(new TrashedCheckBoxEvent(false));
//				} else {
//					eventBus.post(new TrashedCheckBoxEvent(true));
//				}
//			}
//		});
//		HorizontalLayout hl = new HorizontalLayout();
//		hl.setSpacing(true);
//		hl.addComponent(trashBt);
//		hl.addComponent(pager.afterInjection(eventBus));
//		addComponent(hl);
//		hl.setComponentAlignment(trashBt, Alignment.MIDDLE_RIGHT);
//		setComponentAlignment(hl, Alignment.MIDDLE_RIGHT);
//		return this;
//	}
	
//	public TableController afterInjection(EventBus eventBus, ButtonGroup...btnGroups) {
//		this.eventBus = eventBus;
//		this.eventBus.register(this);
//		addStyleName("table-controller");
//		setWidth("100%");
//		addComponent(menu.afterInjection(eventBus, btnGroups));
//		
//		setComponentAlignment(menu, Alignment.MIDDLE_LEFT);
//		trashBt = new Button(messageSource.getMessage("tablecontroller.trashswitcher", null, UI.getCurrent().getLocale()), FontAwesome.TRASH);
//		
//		trashBt.addClickListener(new ClickListener() {
//			@Override
//			public void buttonClick(ClickEvent event) {
//				String styles = event.getButton().getStyleName();
//				if (StyleUtil.hasStyleName(styles, ValoTheme.BUTTON_PRIMARY)) {
//					eventBus.post(new TrashedCheckBoxEvent(false));
//				} else {
//					eventBus.post(new TrashedCheckBoxEvent(true));
//				}
//			}
//		});
//		HorizontalLayout hl = new HorizontalLayout();
//		hl.setSpacing(true);
//		hl.addComponent(trashBt);
//		hl.addComponent(pager.afterInjection(eventBus));
//		addComponent(hl);
//		hl.setComponentAlignment(trashBt, Alignment.MIDDLE_RIGHT);
//		setComponentAlignment(hl, Alignment.MIDDLE_RIGHT);
//		return this;
//	}


	@Override
	@Subscribe
	public void whenUriFragementChange(ListViewFragmentBuilder vfb) {
		boolean trashed = vfb.getBoolean(ListViewFragmentBuilder.TRASHED_PARAM_NAME);
		if (trashed) {
			trashBt.addStyleName(ValoTheme.BUTTON_PRIMARY);
		} else {
			trashBt.removeStyleName(ValoTheme.BUTTON_PRIMARY);
		}
	}
}
