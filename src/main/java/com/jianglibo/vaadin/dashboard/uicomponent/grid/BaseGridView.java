package com.jianglibo.vaadin.dashboard.uicomponent.grid;

import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

import com.google.common.eventbus.Subscribe;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridWrapper;
import com.jianglibo.vaadin.dashboard.config.CommonMenuItemIds;
import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEventBus;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEvent.BrowserResizeEvent;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription.ButtonEnableType;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonGroup;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.DynButtonComponent;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.DynButtonComponent.DynaMenuItemClickListener;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public abstract class BaseGridView<E extends BaseEntity, G extends BaseGrid<E>> extends VerticalLayout implements View {
	
	private final ApplicationContext applicationContext;
	
	private final MessageSource messageSource;
	
	private final Domains domains;
	
	private final Class<E> clazz;
	
	private final Class<G> gridClazz;
	
	private VaadinGridWrapper vgw;
	
	private ListViewFragmentBuilder lvfb;
	
	private G grid;
	
	private UiEventListener uel = new UiEventListener();
	
	private Component topBlock;
	private Component middleBlock;
	private Component bottomBlock;
	
	public BaseGridView(ApplicationContext applicationContext, MessageSource messageSource, Domains domains,
			Class<E> clazz, Class<G> gridClazz) {
		this.applicationContext = applicationContext;
		this.messageSource = messageSource;
		this.domains = domains;
		this.clazz = clazz;
		this.gridClazz = gridClazz;
		
		setSizeFull();
		addStyleName("transactions");
		
		this.vgw = domains.getGrids().get(clazz.getSimpleName());

		topBlock = createTopBlock();
		addComponent(topBlock);

		middleBlock = createMiddleBlock();
		addComponent(middleBlock);

		bottomBlock = createBottomBlock();
		addComponent(bottomBlock);

		setExpandRatio(bottomBlock, 1);
	}

	@Override
	public void enter(ViewChangeEvent event) {
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

	
	private Component createTopBlock() {
		TopBlock tb = new TopBlock();

		tb.addClickListener(event -> {
			this.backward();
		});
		return tb;
	}
	

	private Component createMiddleBlock() {
		DynButtonComponent dynMenu = new DynButtonComponent(messageSource,getButtonGroups());
		MiddleBlock mb = new MiddleBlock(dynMenu);

		mb.addDynaMenuItemClickListener(btnDsc -> {
			onDynButtonClicked(btnDsc);
		});
		return mb;
	}
	
	private Component createBottomBlock() {
		grid = createGrid(messageSource, domains, clazz);
		BottomBlock bottomBlock = new BottomBlock();
		
//		/**
//		 * add listener to final event source object.
//		 */
//		bottomBlock.getTable().getContainer().addPageMetaChangeListener(pme -> {
//			bottomBlock.getTable().setFooter(pme);
//			((MiddleBlock)middleBlock).getPager().setTotalPage(pme);
//		});
//
//		bottomBlock.getTable().addValueChangeListener(event -> {
//			if (table.getValue() instanceof Set) {
//				Set<Object> val = (Set<Object>) table.getValue();
//				((MiddleBlock)middleBlock).alterState(val.size());
//			}
//		});
		
		return bottomBlock;
	}

	protected abstract G createGrid(MessageSource messageSource, Domains domains, Class<E> clazz);


	protected abstract void onDynButtonClicked(ButtonDescription btnDsc);
	
	
	public ButtonGroup[] getButtonGroups() {
		return new ButtonGroup[]{ //
		new ButtonGroup( //
				new ButtonDescription(CommonMenuItemIds.EDIT, FontAwesome.EDIT, ButtonEnableType.ONE), //
				new ButtonDescription(CommonMenuItemIds.DELETE, FontAwesome.TRASH, ButtonEnableType.MANY)),//
		new ButtonGroup( //
				new ButtonDescription(CommonMenuItemIds.ADD, FontAwesome.PLUS, ButtonEnableType.ALWAYS), //
				new ButtonDescription(CommonMenuItemIds.REFRESH, FontAwesome.REFRESH, ButtonEnableType.ALWAYS))};
	}
	
	public void backward() {
		if (getLvfb().getPreviousView().isPresent()) {
			UI.getCurrent().getNavigator().navigateTo(getLvfb().getPreviousView().get());
		}
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public Domains getDomains() {
		return domains;
	}

	public Class<E> getClazz() {
		return clazz;
	}

	public Class<G> getGridClazz() {
		return gridClazz;
	}
	
	public ListViewFragmentBuilder getLvfb() {
		return lvfb;
	}
	
	protected class TopBlock extends HorizontalLayout {
		private Label title;
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

			addComponent(tools);

			backBtn = new Button(FontAwesome.MAIL_REPLY);
			StyleUtil.hide(backBtn);

			backBtn.setDescription(messageSource.getMessage("shared.btn.return", null, UI.getCurrent().getLocale()));

			tools.addComponent(backBtn);
		}

		public void alterState(ListViewFragmentBuilder lvfb, String title) {
			this.title.setValue(title);
			if (lvfb.getPreviousView().isPresent()) {
				StyleUtil.show(backBtn);
			}
		}

		public void addClickListener(ClickListener cl) {
			this.backBtn.addClickListener(cl);
		}
	}

	protected class MiddleBlock extends HorizontalLayout {

		private DynButtonComponent menu;

		public MiddleBlock(DynButtonComponent menu) {
			this.menu = menu;
			addStyleName("table-controller");
			setWidth("100%");
			addComponent(menu);

			setComponentAlignment(menu, Alignment.MIDDLE_LEFT);

			HorizontalLayout hl = new HorizontalLayout();
			hl.setSpacing(true);
			addComponent(hl);
			setComponentAlignment(hl, Alignment.MIDDLE_RIGHT);
		}

		public void alterState(ListViewFragmentBuilder lvfb) {
		}

		public void addDynaMenuItemClickListener(DynaMenuItemClickListener dynaMenuItemClickListener) {
			menu.AddDynaMenuItemClickListener(dynaMenuItemClickListener);
		}
		

		public void alterState(int selectNumber) {
			this.menu.onSelectionChange(selectNumber);
		}
		public DynButtonComponent getMenu() {
			return menu;
		}

	}

	protected class BottomBlock extends HorizontalLayout {

		public BottomBlock() {
			setSizeFull();
			addComponent(grid);
		}
		
		public void alterState(ListViewFragmentBuilder lvfb) {
//			table.getContainer().whenUriFragmentChange(lvfb);
		}

		public void addTableValueChangeListener(ValueChangeListener vcl) {
//			this.table.addValueChangeListener(vcl);
		}

		public G getGrid() {
			return grid;
		}
	}
	
	
	
	public G getGrid() {
		return grid;
	}

	protected class UiEventListener {
		@Subscribe
		public void browserResized(final BrowserResizeEvent event) {
			// Some columns are collapsed when browser window width gets small
			// enough to make the table fit better.
			BottomBlock bb = (BaseGridView<E, G>.BottomBlock) bottomBlock;
		}
	}

}
