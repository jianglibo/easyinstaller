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
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.config.CommonMenuItemIds;
import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEvent.BrowserResizeEvent;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEventBus;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription.ButtonEnableType;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonGroup;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.DynButtonComponent;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.DynButtonComponent.DynaMenuItemClickListener;
import com.jianglibo.vaadin.dashboard.uicomponent.filterform.FilterForm;
import com.jianglibo.vaadin.dashboard.uicomponent.pager.Pager;
import com.jianglibo.vaadin.dashboard.uicomponent.table.TableBase;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.jianglibo.vaadin.dashboard.util.SortUtil;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * If a component need to interact with this object, It's better to hold that
 * component's reference here. It makes logic more simple.
 * 
 * BaseListView is a verticlelayout, composed with three blocks in order.
 * TopBlock, MiddleBlock, BottomBlock. If any child class need to customize, can
 * override one of block.
 * 
 * @author jianglibo@gmail.com
 *
 * @param <E>
 * @param <T>
 * @param <J>
 */
@SuppressWarnings("serial")
public abstract class BaseListView<E extends BaseEntity, T extends TableBase<E>, J extends JpaRepository<E, Long>>
		extends VerticalLayout implements View, SubscriberExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseListView.class);

	private final MessageSource messageSource;

	private final Domains domains;

	private final ApplicationContext applicationContext;

	private final Class<E> clazz;

	private ListViewFragmentBuilder lvfb;

	private UiEventListener uel = new UiEventListener();

	private final J repository;

	private Component topBlock;
	private Component middleBlock;
	private Component bottomBlock;

	public BaseListView(ApplicationContext applicationContext, MessageSource messageSource, Domains domains,
			J repository, Class<E> clazz, Class<T> tableClazz) {
		this.messageSource = messageSource;
		this.repository = repository;
		this.domains = domains;
		this.applicationContext = applicationContext;
		this.clazz = clazz;
	}
	
	public void delayCreateContent() {
		setSizeFull();
		addStyleName("transactions");

		topBlock = createTopBlock();
		addComponent(topBlock);

		middleBlock = createMiddleBlock();
		addComponent(middleBlock);

		bottomBlock = createBottomBlock();
		addComponent(bottomBlock);

		setExpandRatio(bottomBlock, 1);
	}


	private Component createTopBlock() {
		TopBlock tb = new TopBlock();
		tb.getFilterForm().addValueChangeListener(str -> {
			this.notifyFilterStringChange(str);
		});

		tb.getBackBtn().addClickListener(event -> {
			this.backward();
		});
		return tb;
	}
	

	private Component createMiddleBlock() {
		DynButtonComponent dynMenu = new DynButtonComponent(messageSource,getButtonGroups());
		Pager pager = new Pager(messageSource);
		MiddleBlock mb = new MiddleBlock(dynMenu, pager);

		mb.addTrashBtnClickListener(event -> {
			String styles = event.getButton().getStyleName();
			if (StyleUtil.hasStyleName(styles, ValoTheme.BUTTON_PRIMARY)) {
				trashBtnClicked(false);
			} else {
				trashBtnClicked(true);
			}
		});
		
		pager.addPageChangeListener(page -> {
			gotoPage(page);
		});
		
		mb.addDynaMenuItemClickListener(btnDsc -> {
			onDynButtonClicked(btnDsc);
		});
		return mb;
	}
	
	private Component createBottomBlock() {
		T table = createTable();
		BottomBlock bottomBlock = new BottomBlock(table);
		
		/**
		 * add listener to final event source object.
		 */
		bottomBlock.getTable().getContainer().addPageMetaChangeListener(pme -> {
			bottomBlock.getTable().setFooter(pme);
			((MiddleBlock)middleBlock).getPager().setTotalPage(pme);
		});

		bottomBlock.getTable().addValueChangeListener(event -> {
			if (table.getValue() instanceof Set) {
				Set<Object> val = (Set<Object>) table.getValue();
				((MiddleBlock)middleBlock).alterState(val.size());
			}
		});
		
		return bottomBlock;
	}

	protected abstract void onDynButtonClicked(ButtonDescription btnDsc);

	public abstract T createTable();
	
	public ButtonGroup[] getButtonGroups() {
		return new ButtonGroup[]{ //
		new ButtonGroup(new ButtonDescription(CommonMenuItemIds.EDIT, FontAwesome.EDIT, ButtonEnableType.ONE), //
				new ButtonDescription(CommonMenuItemIds.DELETE, FontAwesome.TRASH, ButtonEnableType.MANY)),//
		new ButtonGroup(new ButtonDescription(CommonMenuItemIds.REFRESH, FontAwesome.REFRESH, ButtonEnableType.ALWAYS))};
	}

	public abstract String getListViewName();


	@Override
	public void enter(final ViewChangeEvent event) {
		DashboardEventBus.register(uel);
		lvfb = new ListViewFragmentBuilder(event);

		// start alter state.
		((TopBlock) topBlock).alterState(lvfb, MsgUtil.getListViewTitle(messageSource, clazz.getSimpleName()));
		((MiddleBlock)middleBlock).alterState(lvfb);
		((BottomBlock) bottomBlock).alterState(lvfb);
	}

	@Override
	public void detach() {
		super.detach();
		// A new instance of TransactionsView is created every time it's
		// navigated to so we'll need to clean up references to it on detach.
		DashboardEventBus.unregister(uel);
	}

	public void trashBtnClicked(boolean b) {
		String nvs = getLvfb().setFilterStr("").setCurrentPage(1)
				.setBoolean(ListViewFragmentBuilder.TRASHED_PARAM_NAME, b).toNavigateString();
		UI.getCurrent().getNavigator().navigateTo(nvs);
	}

	public void gotoPage(int p) {
		String nvs = getLvfb().setCurrentPage(p).toNavigateString();
		UI.getCurrent().getNavigator().navigateTo(nvs);
	}

	public void notifyFilterStringChange(String str) {
		String nvs = getLvfb().setFilterStr(str).toNavigateString();
		UI.getCurrent().getNavigator().navigateTo(nvs);
	}

	public void backward() {
		UI.getCurrent().getNavigator().navigateTo(getLvfb().getPreviousView().orElse(getListViewName()));
	}

	@Override
	public void handleException(Throwable exception, SubscriberExceptionContext context) {
		exception.printStackTrace();
	}

	 public T getTable() {
		 return ((BottomBlock)bottomBlock).getTable();
	 }

	public ApplicationContext getApplicationContext() {
		return applicationContext;
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
	
	public boolean autoCollapseColumnsNeedChangeState(VaadinTableWrapper vtw, Table table) {
		boolean result = true;
		for (String propertyId : vtw.getAutoCollapseColumns()) {
			if (table.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
				result = false;
			}
		}
		return result;
	}

	protected class UiEventListener {
		@Subscribe
		public void browserResized(final BrowserResizeEvent event) {
			// Some columns are collapsed when browser window width gets small
			// enough to make the table fit better.
			BottomBlock bb = (BaseListView<E, T, J>.BottomBlock) bottomBlock;
			VaadinTableWrapper vtw = getDomains().getTables().get(clazz);
			if (vtw != null) {
				if (autoCollapseColumnsNeedChangeState(vtw ,bb.getTable())) {
					for (String propertyId : vtw.getAutoCollapseColumns()) {
						bb.getTable().setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
					}
				}
			}
		}
	}

	protected class TopBlock extends HorizontalLayout {
		private Label title;
		private FilterForm filterForm;
		private Button backBtn;

		public TopBlock() {
			addStyleName("viewheader");
			setSpacing(true);
			Responsive.makeResponsive(this);

			title = new Label("");
			title.setSizeUndefined();
			title.addStyleName(ValoTheme.LABEL_H1);
			title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
			addComponent(title);
			
			
			HorizontalLayout tools = new HorizontalLayout();
			tools.addStyleName("toolbar");
			filterForm = new FilterForm(messageSource);
			tools.addComponent(filterForm);

			addComponent(tools);

			backBtn = new Button(FontAwesome.MAIL_REPLY);
			StyleUtil.hide(backBtn);

			backBtn.setDescription(messageSource.getMessage("shared.btn.return", null, UI.getCurrent().getLocale()));

			tools.addComponent(backBtn);
		}

		public void alterState(ListViewFragmentBuilder lvfb, String title) {
			this.title.setValue(title);
			filterForm.whenUriFragmentChange(lvfb);
			if (lvfb.getPreviousView().isPresent()) {
				StyleUtil.show(backBtn);
			}
		}
		
		public FilterForm getFilterForm() {
			return filterForm;
		}
		
		public Button getBackBtn() {
			return backBtn;
		}
	}

	protected class MiddleBlock extends HorizontalLayout {

		private Button trashBt;
		
		private DynButtonComponent menu;
		
		private Pager pager;

		public MiddleBlock(DynButtonComponent menu, Pager pager) {
			this.menu = menu;
			this.pager = pager;
			addStyleName("table-controller");
			setWidth("100%");
			addComponent(menu);

			setComponentAlignment(menu, Alignment.MIDDLE_LEFT);
			trashBt = new Button(
					messageSource.getMessage("tablecontroller.trashswitcher", null, UI.getCurrent().getLocale()),
					FontAwesome.TRASH);

			HorizontalLayout hl = new HorizontalLayout();
			hl.setSpacing(true);
			hl.addComponent(trashBt);
			hl.addComponent(pager);
			addComponent(hl);
			hl.setComponentAlignment(trashBt, Alignment.MIDDLE_RIGHT);
			setComponentAlignment(hl, Alignment.MIDDLE_RIGHT);
		}

		public void alterState(ListViewFragmentBuilder lvfb) {
			if (lvfb.isTrashed()) {
				trashBt.addStyleName(ValoTheme.BUTTON_PRIMARY);
			} else {
				trashBt.removeStyleName(ValoTheme.BUTTON_PRIMARY);
			}
			pager.setCurrentPageAndPerPage(lvfb);
		}

		public void addDynaMenuItemClickListener(DynaMenuItemClickListener dynaMenuItemClickListener) {
			menu.AddDynaMenuItemClickListener(dynaMenuItemClickListener);
		}

		public void addTrashBtnClickListener(ClickListener cl) {
			trashBt.addClickListener(cl);
		}

		public void alterState(int selectNumber) {
			this.menu.onSelectionChange(selectNumber);
		}

		public Button getTrashBt() {
			return trashBt;
		}

		public DynButtonComponent getMenu() {
			return menu;
		}

		public Pager getPager() {
			return pager;
		}
	}

	protected class BottomBlock extends HorizontalLayout {
		private T table;

		public BottomBlock(T table) {
			this.table = table;
			setWidth("100%");
			setHeight("100%");
			addComponent(table);
		}
		
		public void alterState(ListViewFragmentBuilder lvfb) {
			table.getContainer().whenUriFragmentChange(lvfb);
		}

		public T getTable() {
			return table;
		}
	}

}
