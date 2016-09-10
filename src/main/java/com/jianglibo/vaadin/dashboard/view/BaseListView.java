package com.jianglibo.vaadin.dashboard.view;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.JpaRepository;

import com.google.common.eventbus.Subscribe;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumns;
import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEvent.BrowserResizeEvent;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEventBus;
import com.jianglibo.vaadin.dashboard.event.view.PageMetaEvent;
import com.jianglibo.vaadin.dashboard.event.view.TableSortEvent;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonGroup;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.DynButtonComponent;
import com.jianglibo.vaadin.dashboard.uicomponent.filterform.FilterForm;
import com.jianglibo.vaadin.dashboard.uicomponent.pager.Pager;
import com.jianglibo.vaadin.dashboard.uicomponent.table.TableBase;
import com.jianglibo.vaadin.dashboard.uicomponent.table.TableController;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.jianglibo.vaadin.dashboard.util.SortUtil;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.jianglibo.vaadin.dashboard.util.TableUtil;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * If a component need to interact with this object, It's better to hold that component's reference here.
 * It makes logic more simple.
 * 
 * @author jianglibo@gmail.com
 *
 * @param <E>
 * @param <T>
 * @param <J>
 */
@SuppressWarnings("serial")
public abstract class BaseListView<E extends BaseEntity, T extends TableBase<E>, J extends JpaRepository<E, Long>> extends VerticalLayout implements View, SubscriberExceptionHandler, ListView {
	
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
	
	private HorizontalLayout headerLayout;
	
	private DynButtonComponent dynMenu;
	
	private Pager pager;
	
	private ListViewFragmentBuilder lvfb;
	
	private FilterForm filterForm;
	
	private Label title;
	
	private Button backBtn;
	
	private UiEventListener uel = new UiEventListener();
	
	private final J repository;
	
	public BaseListView(ApplicationContext applicationContext, MessageSource messageSource, Domains domains, J repository, Class<E> clazz, Class<T> tableClazz) {
		this.messageSource = messageSource;
		this.repository = repository;
		this.domains = domains;
		this.applicationContext = applicationContext;
		this.clazz = clazz;
		this.tableClazz = tableClazz;
		setSizeFull();
		addStyleName("transactions");
		
		tableColumns = domains.getTableColumns().get(clazz.getSimpleName());
		
		headerLayout = createHeaderLayout();
		addComponent(headerLayout);
		
		dynMenu = getDynButtonComponent();
		pager = getPager();

		tableController = new TableController(messageSource,dynMenu,pager,this);

		addComponent(tableController);
		table = createTable();
		
		table.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				if (table.getValue() instanceof Set) {
					Set<Object> val = (Set<Object>) table.getValue();
					dynMenu.onSelectionChange(val.size());
				}
			}
		});

		addComponent(table);
		setExpandRatio(table, 1);
	}
	
	public HorizontalLayout createHeaderLayout() {
		title = new Label("");
		title.setSizeUndefined();
		title.addStyleName(ValoTheme.LABEL_H1);
		title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		HorizontalLayout hl = new HorizontalLayout();
		hl.addComponent(title);
		hl.addStyleName("viewheader");
		hl.setSpacing(true);
		Responsive.makeResponsive(hl);
		HorizontalLayout tools = new HorizontalLayout();
		tools.addStyleName("toolbar");
		filterForm = new FilterForm(messageSource);
		filterForm.addValueChangeListener(str -> {
			this.notifyFilterStringChange(str);
		});
		tools.addComponent(filterForm);
		
		hl.addComponent(tools);
		

		backBtn = new Button(FontAwesome.MAIL_REPLY);
		StyleUtil.hide(backBtn);
		
		backBtn.setDescription(messageSource.getMessage("shared.btn.return", null, UI.getCurrent().getLocale()));
		backBtn.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				BaseListView.this.backward();
			}
		});
		tools.addComponent(backBtn);

		return hl;
	}
	
	public abstract T createTable();
	
	public abstract ButtonGroup[] getButtonGroups();
	
	public abstract String getListViewName();
	
	public DynButtonComponent getDynButtonComponent() {
		return new DynButtonComponent(messageSource, this, getButtonGroups());
	}
	
	public Pager getPager() {
		return new Pager(messageSource, this);
	}
	
	@Override
	public void enter(final ViewChangeEvent event) {
		DashboardEventBus.register(uel);
		lvfb = new ListViewFragmentBuilder(event);
		getTable().getContainer().whenUriFragmentChange(lvfb);
		filterForm.whenUriFragmentChange(lvfb);
		if (lvfb.getPreviousView().isPresent()) {
			StyleUtil.show(backBtn);
		}
		title.setCaption(MsgUtil.getListViewTitle(messageSource, clazz.getSimpleName()));
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
		UI.getCurrent().getNavigator().navigateTo(getLvfb().getPreviousView().orElse(getListViewName()));
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

	public J getRepository() {
		return repository;
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
