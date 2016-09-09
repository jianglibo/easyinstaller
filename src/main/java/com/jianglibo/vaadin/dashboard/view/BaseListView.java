package com.jianglibo.vaadin.dashboard.view;

import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

import com.google.common.base.Strings;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumns;
import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.event.view.PageMetaEvent;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonGroup;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.DynButtonComponent;
import com.jianglibo.vaadin.dashboard.uicomponent.filterform.FilterForm;
import com.jianglibo.vaadin.dashboard.uicomponent.pager.Pager;
import com.jianglibo.vaadin.dashboard.uicomponent.table.TableController;
import com.jianglibo.vaadin.dashboard.uicomponent.viewheader.HeaderLayout;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public abstract class BaseListView<T extends BaseEntity> extends VerticalLayout implements View, SubscriberExceptionHandler, ListView {
	
	private final MessageSource messageSource;
	
	private final Domains domains;
	
	private final Table table;
	
	private final ApplicationContext applicationContext;
	
	private final Class<T> clazz;
	
	private VaadinTableColumns tableColumns;
	
	private TableController tableController;
	
	private TableController tableHeader;
	
	private HeaderLayout headerLayout;
	
	private DynButtonComponent dynMenu;
	
	private Pager pager;
	
	private FilterForm filterForm;
	
	public BaseListView(ApplicationContext applicationContext, MessageSource messageSource, Domains domains, Class<T> clazz) {
		this.messageSource = messageSource;
		this.domains = domains;
		this.applicationContext = applicationContext;
		this.clazz = clazz;
		setSizeFull();
		addStyleName("transactions");
		
		tableColumns = domains.getTableColumns().get(clazz.getSimpleName());
		
		filterForm = getFilterForm();
		
		headerLayout = new HeaderLayout(messageSource, getFilterForm(), false, MsgUtil.getListViewTitle(messageSource, clazz.getSimpleName()), this);
		addComponent(headerLayout);
		
		dynMenu = getDynButtonComponent();
		pager = getPager();

		tableController = new TableController(messageSource,dynMenu,pager,this);

		addComponent(tableController);
		table = createTable();

		addComponent(table);
		setExpandRatio(table, 1);
	}
	
	public abstract Table createTable();
	
	public abstract ButtonGroup[] getButtonGroups();
	
	public abstract ListViewFragmentBuilder getListViewFragmentBuilder();
	
	public abstract String getListViewName();
	
	public DynButtonComponent getDynButtonComponent() {
		return new DynButtonComponent(messageSource, getButtonGroups());
	}
	
	public Pager getPager() {
		return new Pager(messageSource, this);
	}
	
	private FilterForm getFilterForm() {
		return new FilterForm(messageSource, this);
	}
	
	
	
	@Override
	public void trashBtnClicked(boolean b) {
		String nvs = getListViewFragmentBuilder().setFilterStr("").setCurrentPage(1).setBoolean(ListViewFragmentBuilder.TRASHED_PARAM_NAME, b).toNavigateString();
		UI.getCurrent().getNavigator().navigateTo(nvs);
	}

	@Override
	public void gotoPage(int p) {
		String nvs = getListViewFragmentBuilder().setCurrentPage(p).toNavigateString();
		UI.getCurrent().getNavigator().navigateTo(nvs);
	}

	@Override
	public void notifyFilterStringChange(String str) {
		String nvs = getListViewFragmentBuilder().setFilterStr(str).toNavigateString();
		UI.getCurrent().getNavigator().navigateTo(nvs);
	}
	
	@Override
	public void backward() {
		String bu = getListViewFragmentBuilder().getPreviousView();
		if (Strings.isNullOrEmpty(bu)) {
			bu = getListViewName();
		}
		UI.getCurrent().getNavigator().navigateTo(bu);

	}
	
	@Override
	public void handleException(Throwable exception, SubscriberExceptionContext context) {
		exception.printStackTrace();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		
	}
	
	@Override
	public void onPageMetaEvent(PageMetaEvent pme) {
		
	}

	public Table getTable() {
		return table;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}


	public VaadinTableColumns getTableColumns() {
		return tableColumns;
	}


	public TableController getTableController() {
		return tableController;
	}


	public TableController getTableHeader() {
		return tableHeader;
	}


	public Domains getDomains() {
		return domains;
	}
	
	

}
