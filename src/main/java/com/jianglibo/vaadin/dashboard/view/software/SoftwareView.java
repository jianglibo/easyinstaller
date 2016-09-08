package com.jianglibo.vaadin.dashboard.view.software;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumns;
import com.jianglibo.vaadin.dashboard.config.CommonMenuItemIds;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEvent.BrowserResizeEvent;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEventBus;
import com.jianglibo.vaadin.dashboard.event.view.CurrentPageEvent;
import com.jianglibo.vaadin.dashboard.event.view.DynMenuClickEvent;
import com.jianglibo.vaadin.dashboard.event.view.FilterStrEvent;
import com.jianglibo.vaadin.dashboard.event.view.PageMetaEvent;
import com.jianglibo.vaadin.dashboard.event.view.TableSortEvent;
import com.jianglibo.vaadin.dashboard.event.view.TrashedCheckBoxEvent;
import com.jianglibo.vaadin.dashboard.repositories.SoftwareRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription.ButtonEnableType;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonGroup;
import com.jianglibo.vaadin.dashboard.uicomponent.table.TableController;
import com.jianglibo.vaadin.dashboard.uicomponent.viewheader.HeaderLayout;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.jianglibo.vaadin.dashboard.util.SortUtil;
import com.jianglibo.vaadin.dashboard.util.TableUtil;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = SoftwareView.VIEW_NAME)
public class SoftwareView extends VerticalLayout implements View, SubscriberExceptionHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SoftwareView.class);
	

	public static final String VIEW_NAME = "software";

	public static final FontAwesome ICON_VALUE = FontAwesome.CUBES;

	private final Table table;
	
	private TableController tableController;
	
	private ListViewFragmentBuilder lvfb;

	private EventBus eventBus;
	
	private VaadinTableColumns tableColumns;
	
	private UiEventListener uel = new UiEventListener();
	
	private final SoftwareRepository repository;
	
	private final Domains domains;
	
	@Autowired
	public SoftwareView(SoftwareRepository repository,Domains domains, MessageSource messageSource,
			ApplicationContext applicationContext) {
		this.eventBus = new EventBus(this);
		this.repository = repository;
		this.domains = domains;
		eventBus.register(this);
		setSizeFull();
		addStyleName("transactions");
		
		tableColumns = domains.getTableColumns().get(Software.class.getSimpleName());
		

		Layout header = applicationContext.getBean(HeaderLayout.class).afterInjection(eventBus, true, false, MsgUtil.getListViewTitle(messageSource, Software.class.getSimpleName()));
		addComponent(header);
		
		ButtonGroup[] bgs = new ButtonGroup[]{ //
				new ButtonGroup(new ButtonDescription(CommonMenuItemIds.EDIT, FontAwesome.EDIT, ButtonEnableType.ONE), //
						new ButtonDescription(CommonMenuItemIds.DELETE, FontAwesome.TRASH, ButtonEnableType.MANY)),//
				new ButtonGroup(new ButtonDescription(CommonMenuItemIds.REFRESH, FontAwesome.REFRESH, ButtonEnableType.ALWAYS))};
//				new ButtonGroup(new ButtonDescription(CommonMenuItemIds.ADD, FontAwesome.PLUS, ButtonEnableType.ALWAYS))};
		
		tableController = applicationContext.getBean(TableController.class).afterInjection(eventBus, bgs);

		addComponent(tableController);
		table = applicationContext.getBean(SoftwareTable.class).afterInjection(eventBus);

		addComponent(table);
		setExpandRatio(table, 1);
	}

	@Override
	public void detach() {
		super.detach();
		// A new instance of TransactionsView is created every time it's
		// navigated to so we'll need to clean up references to it on detach.
		DashboardEventBus.unregister(uel);
	}
	
	@Subscribe
	public void whenTotalPageChange(PageMetaEvent tpe) {
		table.setColumnFooter("createdAt", String.valueOf(tpe.getTotalRecord()));	
	}
	
	@Subscribe
	public void whenCurrentPageChange(CurrentPageEvent cpe) {
		String nvs = lvfb.setCurrentPage(cpe.getCurrentPage()).toNavigateString();
		UI.getCurrent().getNavigator().navigateTo(nvs);
	}
	
	@Subscribe
	public void whenFilterStrChange(FilterStrEvent fse) {
		String nvs = lvfb.setFilterStr(fse.getFilterStr()).toNavigateString();
		UI.getCurrent().getNavigator().navigateTo(nvs);
	}
	
	@Subscribe
	public void whenSortChanged(TableSortEvent tse) {
		SortUtil.setUrlObSort(tse.getSort(), domains.getTables().get(Software.class.getSimpleName()), lvfb);
		UI.getCurrent().getNavigator().navigateTo(lvfb.toNavigateString());
	}
	
	@Subscribe
	public void whenTrashedCheckboxChange(TrashedCheckBoxEvent tce) {
		String nvs = lvfb.setFilterStr("").setCurrentPage(1).setBoolean(ListViewFragmentBuilder.TRASHED_PARAM_NAME, tce.isChecked()).toNavigateString();
		UI.getCurrent().getNavigator().navigateTo(nvs);
	}
	
	@SuppressWarnings("unchecked")
	@Subscribe
	public void dynMenuClicked(DynMenuClickEvent dce) {
		Collection<Software> selected;
		switch (dce.getBtnId()) {
		case CommonMenuItemIds.DELETE:
			selected = (Collection<Software>) table.getValue();
			selected.forEach(b -> {
				if (b.isArchived()) {
					repository.delete(b);
				} else {
					b.setArchived(true);
					repository.save(b);
				}
			});
			((SoftwareContainer)table.getContainerDataSource()).refresh();
			break;
		case CommonMenuItemIds.REFRESH:
			((SoftwareContainer)table.getContainerDataSource()).refresh();
			break;
		case CommonMenuItemIds.EDIT:
			selected = (Collection<Software>) table.getValue();
			UI.getCurrent().getNavigator().navigateTo(VIEW_NAME + "/edit/" + selected.iterator().next().getId() + "?pv=" + lvfb.toNavigateString());
			break;
		case CommonMenuItemIds.ADD:
			UI.getCurrent().getNavigator().navigateTo(VIEW_NAME + "/edit");
			break;
		default:
			LOGGER.error("unKnown menuName {}", dce.getBtnId());
		}
	}
	
	public class UiEventListener {
		
		@Subscribe
		public void browserResized(final BrowserResizeEvent event) {
			// Some columns are collapsed when browser window width gets small
			// enough to make the table fit better.
			if (TableUtil.autoCollapseColumnsNeedChangeState(table, tableColumns)) {
				for (String propertyId : tableColumns.getAutoCollapseColumns()) {
					table.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
				}
			}
		}
	}

	@Override
	public void enter(final ViewChangeEvent event) {
		DashboardEventBus.register(uel);
		lvfb = new ListViewFragmentBuilder(event);
		eventBus.post(lvfb);
		
		LOGGER.info("parameter is: {}", event.getParameters());
	}

	@Override
	public void handleException(Throwable exception, SubscriberExceptionContext context) {
		exception.printStackTrace();
		
	}
}
