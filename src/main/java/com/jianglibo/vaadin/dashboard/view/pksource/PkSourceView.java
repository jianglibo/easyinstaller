package com.jianglibo.vaadin.dashboard.view.pksource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jianglibo.vaadin.dashboard.config.CommonMenuItemIds;
import com.jianglibo.vaadin.dashboard.domain.PkSource;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEvent.BrowserResizeEvent;
import com.jianglibo.vaadin.dashboard.event.view.CurrentPageEvent;
import com.jianglibo.vaadin.dashboard.event.view.DynMenuClickEvent;
import com.jianglibo.vaadin.dashboard.event.view.FilterStrEvent;
import com.jianglibo.vaadin.dashboard.event.view.PageMetaEvent;
import com.jianglibo.vaadin.dashboard.event.view.TableSortEvent;
import com.jianglibo.vaadin.dashboard.event.view.TrashedCheckBoxEvent;
import com.jianglibo.vaadin.dashboard.event.view.UploadFinishEvent;
import com.jianglibo.vaadin.dashboard.formatter.FileLengthFormat;
import com.jianglibo.vaadin.dashboard.repositories.PkSourceRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription.ButtonEnableType;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonGroup;
import com.jianglibo.vaadin.dashboard.uicomponent.filterform.FilterForm;
import com.jianglibo.vaadin.dashboard.uicomponent.table.TableController;
import com.jianglibo.vaadin.dashboard.uicomponent.upload.ImmediateUploader;
import com.jianglibo.vaadin.dashboard.uicomponent.viewheader.HeaderLayout;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.view.box.BoxTable;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.TableDragMode;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringView(name = PkSourceView.VIEW_NAME)
public class PkSourceView extends VerticalLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PkSourceView.class);
	
	private static final Sort defaultSort = new Sort(Direction.DESC, "createdAt");

	private final MessageSource messageSource;

	public static final String VIEW_NAME = "pksource";

	public static final FontAwesome ICON_VALUE = FontAwesome.FILE_ARCHIVE_O;


	private final Table table;
	
	private TableController tableController;
	
	private PkSourceContainer pc;
	
	private ListViewFragmentBuilder vfb;

	private static final DateFormat DATEFORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

	private static final String[] DEFAULT_COLLAPSIBLE = { "length", "originFrom", "createdAt" };
	
	private EventBus eventBus;
	
	@Autowired
	public PkSourceView(PkSourceRepository pkSourceRepository, MessageSource messageSource,
			ApplicationContext applicationContext) {
		this.messageSource = messageSource;
		this.eventBus = new EventBus(this.getClass().getName());
		eventBus.register(this);
		setSizeFull();
		addStyleName("transactions");
		
		Layout header = applicationContext.getBean(HeaderLayout.class).afterInjection("");
		HorizontalLayout tools = new HorizontalLayout(applicationContext.getBean(ImmediateUploader.class).afterInjection(eventBus),
				applicationContext.getBean(FilterForm.class).afterInjection(eventBus, ""));
		tools.setSpacing(true);
		tools.addStyleName("toolbar");

		header.addComponent(tools);
		addComponent(header);
		
		pc = applicationContext.getBean(PkSourceContainer.class).afterInjection(eventBus);
		
		ButtonGroup[] bgs = new ButtonGroup[]{new ButtonGroup(new ButtonDescription(CommonMenuItemIds.EDIT, FontAwesome.EDIT, ButtonEnableType.ONE),new ButtonDescription(CommonMenuItemIds.DELETE, FontAwesome.TRASH, ButtonEnableType.MANY)),
				new ButtonGroup(new ButtonDescription(CommonMenuItemIds.REFRESH, FontAwesome.REFRESH, ButtonEnableType.ALWAYS))};
		
		tableController = applicationContext.getBean(TableController.class).afterInjection(eventBus, bgs);
		

		addComponent(tableController);
//		table = buildTable();
		table = applicationContext.getBean(PkSourceTable.class).afterInjection(eventBus);
		addComponent(table);
		setExpandRatio(table, 1);
	}

	@Override
	public void detach() {
		super.detach();
		// A new instance of TransactionsView is created every time it's
		// navigated to so we'll need to clean up references to it on detach.
		// DashboardEventBus.unregister(this);
	}


	private boolean defaultColumnsVisible() {
		boolean result = true;
		for (String propertyId : DEFAULT_COLLAPSIBLE) {
			if (table.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
				result = false;
			}
		}
		return result;
	}
	
	@Subscribe
	public void whenTotalPageChange(PageMetaEvent tpe) {
		table.setColumnFooter("createdAt", String.valueOf(tpe.getTotalRecord()));	
	}
	
	@Subscribe
	public void whenCurrentPageChange(CurrentPageEvent cpe) {
		String nvs = vfb.setCurrentPage(cpe.getCurrentPage()).toNavigateString();
		UI.getCurrent().getNavigator().navigateTo(nvs);
	}
	
	@Subscribe
	public void whenFilterStrChange(FilterStrEvent fse) {
		String nvs = vfb.setFilterStr(fse.getFilterStr()).toNavigateString();
		UI.getCurrent().getNavigator().navigateTo(nvs);
	}
	
	@Subscribe
	public void whenUploadFinished(UploadFinishEvent ufe) {
		PkSource pkSource = ufe.getPkSource();
		if (pkSource != null && ufe.isNewCreated()) {
			LOGGER.info("upload a new file.");
			table.addItem(pkSource);
		}
	}
	
	@Subscribe
	public void whenSortChanged(TableSortEvent tse) {
		Order od = tse.getSort().iterator().next();
		String nvs = vfb.setSort(od.getProperty(), od.isAscending(), defaultSort).toNavigateString();
		UI.getCurrent().getNavigator().navigateTo(nvs);
	}
	
	@Subscribe
	public void whenTrashedCheckboxChange(TrashedCheckBoxEvent tce) {
		String nvs = vfb.setFilterStr("").setCurrentPage(1).setBoolean(ListViewFragmentBuilder.TRASHED_PARAM_NAME, tce.isChecked()).toNavigateString();
		UI.getCurrent().getNavigator().navigateTo(nvs);
	}
	
	@Subscribe
	public void dynMenuClicked(DynMenuClickEvent dce) {
		switch (dce.getBtnId()) {
		case CommonMenuItemIds.DELETE:
			LOGGER.info(table.getValue().toString());
			break;
		case CommonMenuItemIds.REFRESH:
			pc.refresh();
			break;
		case CommonMenuItemIds.EDIT:
			Collection<PkSource> selected = (Collection<PkSource>) table.getValue();
			UI.getCurrent().getNavigator().navigateTo(VIEW_NAME + "/edit/" + selected.iterator().next().getId() + "?pv=" + vfb.toNavigateString());
			break;
		default:
			LOGGER.error("unKnown menuName {}", dce.getBtnId());
		}
	}

	@Subscribe
	public void browserResized(final BrowserResizeEvent event) {
		// Some columns are collapsed when browser window width gets small
		// enough to make the table fit better.
		if (defaultColumnsVisible()) {
			for (String propertyId : DEFAULT_COLLAPSIBLE) {
				table.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
			}
		}
	}

//	void createNewReportFromSelection() {
//		UI.getCurrent().getNavigator().navigateTo(ReportsView.VIEW_NAME);
//		DashboardEventBus.post(new TransactionReportEvent((Collection<Transaction>) table.getValue()));
//	}

	public Table getTable() {
		return table;
	}

	@Override
	public void enter(final ViewChangeEvent event) {
		vfb = new ListViewFragmentBuilder(event);
		Sort sort = vfb.getSort();
		if (sort == null) {
			sort = defaultSort;
		}
		
		if (sort.iterator().hasNext()) {
			Order od = sort.iterator().next();
			table.setSortContainerPropertyId(od.getProperty());
			table.setSortAscending(od.isAscending());
		}
		eventBus.post(vfb);
		LOGGER.info("parameter is: {}", event.getParameters());
	}

//	@Override
//	public void uploadFinished(PkSource pkSource, boolean newCreate) {
//		if (pkSource != null && newCreate) {
//			LOGGER.info("upload a new file.");
//		}
//		table.addItem(pkSource);
//		table.sort();
//	}

//	@Override
//	public void onMenuClick(String menuName) {
//		switch (menuName) {
//		case "delete":
//			LOGGER.info(table.getValue().toString());
//			break;
//
//		default:
//			LOGGER.error("unKnown menuName {}", menuName);
//		}
//	}
	

//	@Override
//	public void checkBoxChanged(String cbName, boolean state) {
//		// TODO Auto-generated method stub
//		
//	}

	// @Override
	// public void setApplicationContext(ApplicationContext applicationContext)
	// throws BeansException {
	// this.applicationContext = applicationContext;
	// }
}
