package com.jianglibo.vaadin.dashboard.view.boxes;

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
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumns;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.config.CommonMenuItemIds;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEvent.BrowserResizeEvent;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEventBus;
import com.jianglibo.vaadin.dashboard.event.view.CurrentPageEvent;
import com.jianglibo.vaadin.dashboard.event.view.DynMenuClickEvent;
import com.jianglibo.vaadin.dashboard.event.view.FilterStrEvent;
import com.jianglibo.vaadin.dashboard.event.view.PageMetaEvent;
import com.jianglibo.vaadin.dashboard.event.view.TableSortEvent;
import com.jianglibo.vaadin.dashboard.event.view.TrashedCheckBoxEvent;
import com.jianglibo.vaadin.dashboard.repositories.BoxRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription.ButtonEnableType;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonGroup;
import com.jianglibo.vaadin.dashboard.uicomponent.filterform.FilterForm;
import com.jianglibo.vaadin.dashboard.uicomponent.table.TableController;
import com.jianglibo.vaadin.dashboard.uicomponent.viewheader.HeaderLayout;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.jianglibo.vaadin.dashboard.util.TableUtil;
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
import com.vaadin.ui.Table.TableDragMode;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = BoxView.VIEW_NAME)
public class BoxView extends VerticalLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(BoxView.class);
	
	private static final Sort defaultSort = new Sort(Direction.DESC, "createdAt");

	private final MessageSource messageSource;

	public static final String VIEW_NAME = "box";

	public static final FontAwesome ICON_VALUE = FontAwesome.DESKTOP;

	private ApplicationContext applicationContext;

	private final Table table;
	
	private TableController tableController;
	
	private BoxContainer pc;
	
	private ListViewFragmentBuilder vfb;

	// private Upload upload;
	private static final DateFormat DATEFORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
	
	private EventBus eventBus;
	
	private VaadinTableColumns tableColumns;
	
	private Domains domains;
	
	private UiEventListener uel = new UiEventListener();
	
	@Autowired
	public BoxView(BoxRepository boxRepository,Domains domains, MessageSource messageSource,
			ApplicationContext applicationContext) {
		this.messageSource = messageSource;
		this.applicationContext = applicationContext;
		this.domains = domains;
		this.eventBus = new EventBus(this.getClass().getName());
		DashboardEventBus.register(uel);
		eventBus.register(this);
		setSizeFull();
		addStyleName("transactions");
		
		Layout header = applicationContext.getBean(HeaderLayout.class).afterInjection(MsgUtil.getMsg(messageSource, "view.box.title"));
		HorizontalLayout tools = new HorizontalLayout(applicationContext.getBean(FilterForm.class).afterInjection(eventBus, ""));
		tools.setSpacing(true);
		tools.addStyleName("toolbar");

		header.addComponent(tools);
		addComponent(header);
		
		pc = new BoxContainer(eventBus, boxRepository,defaultSort, 15);
		
		ButtonGroup[] bgs = new ButtonGroup[]{ //
				new ButtonGroup(new ButtonDescription(CommonMenuItemIds.EDIT, FontAwesome.EDIT, ButtonEnableType.ONE), //
						new ButtonDescription(CommonMenuItemIds.DELETE, FontAwesome.TRASH, ButtonEnableType.MANY)),//
				new ButtonGroup(new ButtonDescription(CommonMenuItemIds.REFRESH, FontAwesome.REFRESH, ButtonEnableType.ALWAYS)), //
				new ButtonGroup(new ButtonDescription(CommonMenuItemIds.ADD, FontAwesome.PLUS, ButtonEnableType.ALWAYS))};
		
		tableController = applicationContext.getBean(TableController.class).afterInjection(eventBus, bgs);

		addComponent(tableController);
		table = buildTable();

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


	@SuppressWarnings("serial")
	private Table buildTable() {
		final Table table = new Table() {
			@Override
			protected String formatPropertyValue(final Object rowId, final Object colId, final Property<?> property) {
				String result = super.formatPropertyValue(rowId, colId, property);
				if (colId.equals("createdAt")) {
					result = DATEFORMAT.format(((Date) property.getValue()));
				}
				return result;
			}
		};
		
		table.setContainerDataSource(pc);
		
		tableColumns = domains.getTableColumns().get(Box.VAADIN_TABLE_NAME);
		VaadinTable vt = domains.getTables().get(Box.VAADIN_TABLE_NAME);
		
		TableUtil.decorateTable(table, messageSource, vt, tableColumns);
		
		
		table.setColumnFooter("createdAt", "");
		table.setColumnFooter("ip", "Total");

		// Allow dragging items to the reports menu
		table.setDragMode(TableDragMode.MULTIROW);

		table.addItemClickListener(new ItemClickListener() {
			@Override
			public void itemClick(ItemClickEvent event) {
				// event.getItem()
				// TODO Auto-generated method stub
			}
		});

		table.addValueChangeListener(new ValueChangeListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(final ValueChangeEvent event) {
				if (table.getValue() instanceof Set) {
					Set<Object> val = (Set<Object>) table.getValue();
					eventBus.post(val);
				}
			}
		});
		table.setImmediate(true);
		return table;
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
			Collection<Box> selected = (Collection<Box>) table.getValue();
			UI.getCurrent().getNavigator().navigateTo(VIEW_NAME + "/edit/" + selected.iterator().next().getId() + "?pv=" + vfb.toNavigateString());
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
}
