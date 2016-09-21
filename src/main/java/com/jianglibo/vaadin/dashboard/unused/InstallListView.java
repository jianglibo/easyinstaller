package com.jianglibo.vaadin.dashboard.unused;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;

import com.google.common.eventbus.Subscribe;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.jianglibo.vaadin.dashboard.config.CommonMenuItemIds;
import com.jianglibo.vaadin.dashboard.data.container.JpaContainer;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.event.view.DynMenuClickEvent;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonGroup;
import com.jianglibo.vaadin.dashboard.view.BaseListView;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.UI;

@SpringView(name = InstallListView.VIEW_NAME)
public class InstallListView extends BaseListView<Install, InstallTable, InstallRepository> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(InstallListView.class);
	

	public static final String VIEW_NAME = "install";

	public static final FontAwesome ICON_VALUE = FontAwesome.APPLE;

//	private VaadinTableColumns tableColumns;
	
	
	
	@Autowired
	public InstallListView(InstallRepository repository,Domains domains, MessageSource messageSource,
			ApplicationContext applicationContext) {
		super(applicationContext,messageSource,domains,repository, Install.class, InstallTable.class);
		
//		this.domains = domains;
//		setSizeFull();
//		addStyleName("transactions");
//		
//		tableColumns = domains.getTableColumns().get(Install.class.getSimpleName());
//		
//
//		Layout header = applicationContext.getBean(HeaderLayout.class).afterInjection(eventBus, false, true, MsgUtil.getListViewTitle(messageSource, Install.class.getSimpleName()));
//		
//		addComponent(header);
//		
//		ButtonGroup[] bgs = new ButtonGroup[]{ //
//				new ButtonGroup(new ButtonDescription(CommonMenuItemIds.EDIT, FontAwesome.EDIT, ButtonEnableType.ONE), //
//						new ButtonDescription(CommonMenuItemIds.DELETE, FontAwesome.TRASH, ButtonEnableType.MANY)),//
//				new ButtonGroup(new ButtonDescription(CommonMenuItemIds.REFRESH, FontAwesome.REFRESH, ButtonEnableType.ALWAYS)),
//				new ButtonGroup(new ButtonDescription(CommonMenuItemIds.ADD, FontAwesome.PLUS, ButtonEnableType.ALWAYS))};
//		
//		tableController = applicationContext.getBean(TableController.class).afterInjection(eventBus, bgs);
//
//		addComponent(tableController);
//		table = applicationContext.getBean(InstallTable.class).afterInjection(eventBus);
//
//		addComponent(table);
//		setExpandRatio(table, 1);
	}

//	@Override
//	public void detach() {
//		super.detach();
//		// A new instance of TransactionsView is created every time it's
//		// navigated to so we'll need to clean up references to it on detach.
//		DashboardEventBus.unregister(uel);
//	}
//	
//	@Subscribe
//	public void whenTotalPageChange(PageMetaEvent tpe) {
//		table.setColumnFooter("createdAt", String.valueOf(tpe.getTotalRecord()));	
//	}
//	
//	@Subscribe
//	public void whenCurrentPageChange(CurrentPageEvent cpe) {
//		String nvs = lvfb.setCurrentPage(cpe.getCurrentPage()).toNavigateString();
//		UI.getCurrent().getNavigator().navigateTo(nvs);
//	}
//	
//	@Subscribe
//	public void whenFilterStrChange(FilterStrEvent fse) {
//		String nvs = lvfb.setFilterStr(fse.getFilterStr()).toNavigateString();
//		UI.getCurrent().getNavigator().navigateTo(nvs);
//	}
//	
//	@Subscribe
//	public void whenSortChanged(TableSortEvent tse) {
//		SortUtil.setUrlObSort(tse.getSort(), domains.getTables().get(Install.class.getSimpleName()), lvfb);
//		UI.getCurrent().getNavigator().navigateTo(lvfb.toNavigateString());
//	}
//	
//	@Subscribe
//	public void whenTrashedCheckboxChange(TrashedCheckBoxEvent tce) {
//		String nvs = lvfb.setFilterStr("").setCurrentPage(1).setBoolean(ListViewFragmentBuilder.TRASHED_PARAM_NAME, tce.isChecked()).toNavigateString();
//		UI.getCurrent().getNavigator().navigateTo(nvs);
//	}
//	
//	@Subscribe
//	public void onBackBtnClicked(HistoryBackEvent hbe) {
//		String bu = lvfb.getPreviousView();
//		if (Strings.isNullOrEmpty(bu)) {
//			bu = BoxListView.VIEW_NAME;
//		}
//		UI.getCurrent().getNavigator().navigateTo(bu);
//	}
	
	@SuppressWarnings("unchecked")
	@Subscribe
	public void dynMenuClicked(DynMenuClickEvent dce) {
		Collection<Install> selected;
		switch (dce.getBtnId()) {
		case CommonMenuItemIds.DELETE:
			selected = (Collection<Install>) getTable().getValue();
			selected.forEach(b -> {
				if (b.isArchived()) {
					getRepository().delete(b);
				} else {
					b.setArchived(true);
					getRepository().save(b);
				}
			});
			((JpaContainer)getTable().getContainerDataSource()).refresh();
			break;
		case CommonMenuItemIds.REFRESH:
			((InstallContainer)getTable().getContainerDataSource()).refresh();
			break;
		case CommonMenuItemIds.EDIT:
			selected = (Collection<Install>) getTable().getValue();
			UI.getCurrent().getNavigator().navigateTo(VIEW_NAME + "/edit/" + selected.iterator().next().getId() + "?pv=" + getLvfb().toNavigateString());
			break;
		case CommonMenuItemIds.ADD:
			UI.getCurrent().getNavigator().navigateTo(VIEW_NAME + "/edit");
			break;
		default:
			LOGGER.error("unKnown menuName {}", dce.getBtnId());
		}
	}

//	@Override
//	public void enter(final ViewChangeEvent event) {
//		DashboardEventBus.register(uel);
//		lvfb = new ListViewFragmentBuilder(event);
//		eventBus.post(lvfb);
//		
//		LOGGER.info("parameter is: {}", event.getParameters());
//	}

	@Override
	public void handleException(Throwable exception, SubscriberExceptionContext context) {
		exception.printStackTrace();
		
	}

	@Override
	public void notifyFilterStringChange(String str) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void trashBtnClicked(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gotoPage(int p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDynButtonClicked(ButtonDescription btnDesc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void backward() {
		// TODO Auto-generated method stub
		
	}

	public void notifySort(Sort sort) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public InstallTable createTable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ButtonGroup[] getButtonGroups() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getListViewName() {
		// TODO Auto-generated method stub
		return null;
	}
}
