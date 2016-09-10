package com.jianglibo.vaadin.dashboard.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

import com.google.common.base.Strings;
import com.google.common.eventbus.Subscribe;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumns;
import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEventBus;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEvent.BrowserResizeEvent;
import com.jianglibo.vaadin.dashboard.event.view.PageMetaEvent;
import com.jianglibo.vaadin.dashboard.event.view.TableSortEvent;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonGroup;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.DynButtonComponent;
import com.jianglibo.vaadin.dashboard.uicomponent.filterform.FilterForm;
import com.jianglibo.vaadin.dashboard.uicomponent.pager.Pager;
import com.jianglibo.vaadin.dashboard.uicomponent.table.TableBase;
import com.jianglibo.vaadin.dashboard.uicomponent.table.TableController;
import com.jianglibo.vaadin.dashboard.uicomponent.viewheader.HeaderLayout;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.jianglibo.vaadin.dashboard.util.SortUtil;
import com.jianglibo.vaadin.dashboard.util.TableUtil;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public abstract class BaseListView<E extends BaseEntity, T extends TableBase<E>> extends VerticalLayout implements View, SubscriberExceptionHandler, ListView {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseListView.class);
	
	private final MessageSource messageSource;
	
	private final Domains domains;
	
	private final T table;
	
	private final ApplicationContext applicationContext;
	
	private final Class<E> clazz;
	
	private final Class<T> tableClazz;
	
	private VaadinTableColumns tableColumns;
	
	private TableController tableController;
	
	private TableController tableHeader;
	
	private HeaderLayout headerLayout;
	
	private DynButtonComponent dynMenu;
	
	private Pager pager;
	
	private ListViewFragmentBuilder lvfb;
	
	private FilterForm filterForm;
	
	private UiEventListener uel = new UiEventListener();
	
	public BaseListView(ApplicationContext applicationContext, MessageSource messageSource, Domains domains, Class<E> clazz, Class<T> tableClazz) {
		this.messageSource = messageSource;
		this.domains = domains;
		this.applicationContext = applicationContext;
		this.clazz = clazz;
		this.tableClazz = tableClazz;
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
	
	public abstract T createTable();
	
//	public T createTable() {
//		return (T) getApplicationContext().getBean(tableClazz).afterInjection(this);
//	}
	
	public abstract ButtonGroup[] getButtonGroups();
	
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
	public void enter(final ViewChangeEvent event) {
		DashboardEventBus.register(uel);
		lvfb = new ListViewFragmentBuilder(event);
		LOGGER.info("parameter is: {}", event.getParameters());
	}
	
	
	@Override
	public void detach() {
		super.detach();
		// A new instance of TransactionsView is created every time it's
		// navigated to so we'll need to clean up references to it on detach.
		DashboardEventBus.unregister(uel);
	}
	
	@Override
	public void trashBtnClicked(boolean b) {
		String nvs = getLvfb().setFilterStr("").setCurrentPage(1).setBoolean(ListViewFragmentBuilder.TRASHED_PARAM_NAME, b).toNavigateString();
		UI.getCurrent().getNavigator().navigateTo(nvs);
	}

	@Override
	public void gotoPage(int p) {
		String nvs = getLvfb().setCurrentPage(p).toNavigateString();
		UI.getCurrent().getNavigator().navigateTo(nvs);
	}

	@Override
	public void notifyFilterStringChange(String str) {
		String nvs = getLvfb().setFilterStr(str).toNavigateString();
		UI.getCurrent().getNavigator().navigateTo(nvs);
	}
	
	@Override
	public void backward() {
		String bu = getLvfb().getPreviousView();
		if (Strings.isNullOrEmpty(bu)) {
			bu = getListViewName();
		}
		UI.getCurrent().getNavigator().navigateTo(bu);

	}
	
	public void whenSortChanged(TableSortEvent tse) {
		SortUtil.setUrlObSort(tse.getSort(), getDomains().getTables().get(Box.class.getSimpleName()), getLvfb());
		UI.getCurrent().getNavigator().navigateTo(getLvfb().toNavigateString());
	}
	
	@Override
	public void handleException(Throwable exception, SubscriberExceptionContext context) {
		exception.printStackTrace();
	}


	
	@Override
	public void onPageMetaEvent(PageMetaEvent pme) {
		
	}

	public T getTable() {
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
	
	
	public ListViewFragmentBuilder getLvfb() {
		return lvfb;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public class UiEventListener {
		
		@Subscribe
		public void browserResized(final BrowserResizeEvent event) {
			// Some columns are collapsed when browser window width gets small
			// enough to make the table fit better.
			if (TableUtil.autoCollapseColumnsNeedChangeState(getTable(), getTableColumns())) {
				for (String propertyId : getTableColumns().getAutoCollapseColumns()) {
					getTable().setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
				}
			}
		}
	}

}
