package com.jianglibo.vaadin.dashboard.view.box;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jianglibo.vaadin.dashboard.config.CommonMenuItemIds;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEvent.BrowserResizeEvent;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEventBus;
import com.jianglibo.vaadin.dashboard.event.view.PageMetaEvent;
import com.jianglibo.vaadin.dashboard.event.view.TableSortEvent;
import com.jianglibo.vaadin.dashboard.repositories.BoxRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription.ButtonEnableType;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonGroup;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.SortUtil;
import com.jianglibo.vaadin.dashboard.util.TableUtil;
import com.jianglibo.vaadin.dashboard.view.BaseListView;
import com.jianglibo.vaadin.dashboard.view.install.InstallView;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

@SpringView(name = BoxView.VIEW_NAME)
public class BoxView extends BaseListView<Box> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(BoxView.class);

	public static final String VIEW_NAME = "box";

	public static final FontAwesome ICON_VALUE = FontAwesome.DESKTOP;

	private ListViewFragmentBuilder lvfb;

	private EventBus eventBus;
	
	
	private UiEventListener uel = new UiEventListener();
	
	private final BoxRepository repository;
	
	@Autowired
	public BoxView(BoxRepository repository,Domains domains, MessageSource messageSource,
			ApplicationContext applicationContext) {
		super(applicationContext, messageSource, domains, Box.class);
		this.repository = repository;
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
		getTable().setColumnFooter("createdAt", String.valueOf(tpe.getTotalRecord()));	
	}
	
	
	@Subscribe
	public void whenSortChanged(TableSortEvent tse) {
		SortUtil.setUrlObSort(tse.getSort(), getDomains().getTables().get(Box.class.getSimpleName()), lvfb);
		UI.getCurrent().getNavigator().navigateTo(lvfb.toNavigateString());
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

	@Override
	public void enter(final ViewChangeEvent event) {
		DashboardEventBus.register(uel);
		lvfb = new ListViewFragmentBuilder(event);
		eventBus.post(lvfb);
		LOGGER.info("parameter is: {}", event.getParameters());
	}
	
	@Override
	public void onDynButtonClicked(ButtonDescription btnDesc) {
		Collection<Box> selected;
		switch (btnDesc.getItemId()) {
		case CommonMenuItemIds.DELETE:
			selected = (Collection<Box>) getTable().getValue();
			selected.forEach(b -> {
				if (b.isArchived()) {
					repository.delete(b);
				} else {
					b.setArchived(true);
					repository.save(b);
				}
			});
			((BoxContainer)getTable().getContainerDataSource()).refresh();
			break;
		case CommonMenuItemIds.REFRESH:
			((BoxContainer)getTable().getContainerDataSource()).refresh();
			break;
		case CommonMenuItemIds.EDIT:
			selected = (Collection<Box>) getTable().getValue();
			UI.getCurrent().getNavigator().navigateTo(VIEW_NAME + "/edit/" + selected.iterator().next().getId() + "?pv=" + lvfb.toNavigateString());
			break;
		case CommonMenuItemIds.ADD:
			UI.getCurrent().getNavigator().navigateTo(VIEW_NAME + "/edit");
			break;
		case "installedSoftware":
			selected = (Collection<Box>) getTable().getValue();
			UI.getCurrent().getNavigator().navigateTo(InstallView.VIEW_NAME + "/?boxid=" + selected.iterator().next().getId() + "&pv=" + lvfb.toNavigateString());
			break;
		default:
			LOGGER.error("unKnown menuName {}", btnDesc.getItemId());
		}
	}

	@Override
	public ButtonGroup[] getButtonGroups() {
		return new ButtonGroup[]{ //
				new ButtonGroup(new ButtonDescription(CommonMenuItemIds.EDIT, FontAwesome.EDIT, ButtonEnableType.ONE), //
						new ButtonDescription(CommonMenuItemIds.DELETE, FontAwesome.TRASH, ButtonEnableType.MANY)),//
				new ButtonGroup(new ButtonDescription(CommonMenuItemIds.REFRESH, FontAwesome.REFRESH, ButtonEnableType.ALWAYS)), //
				new ButtonGroup(new ButtonDescription(CommonMenuItemIds.ADD, FontAwesome.PLUS, ButtonEnableType.ALWAYS)),//
				new ButtonGroup(new ButtonDescription("installedSoftware", null, ButtonEnableType.ONE))};
	}

	@Override
	public ListViewFragmentBuilder getListViewFragmentBuilder() {
		return lvfb;
	}

	@Override
	public void notifySort(Sort sort) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Table createTable() {
		return getApplicationContext().getBean(BoxTable.class).afterInjection(this);
	}

	@Override
	public String getListViewName() {
		return VIEW_NAME;
	}
}
