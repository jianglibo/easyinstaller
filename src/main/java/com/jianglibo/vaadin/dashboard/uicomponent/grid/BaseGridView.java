package com.jianglibo.vaadin.dashboard.uicomponent.grid;

import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

import com.google.common.eventbus.Subscribe;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEvent.BrowserResizeEvent;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEventBus;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.AddButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonGroup;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.DeleteButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.DynButtonComponent;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.DynButtonComponent.DynaMenuItemClickListener;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.EditButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.RefreshButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.UnArchiveButtonDescription;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
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
public abstract class BaseGridView<E extends BaseEntity, G extends BaseGrid<E, C>, C extends FreeContainer<E>> extends VerticalLayout implements View {
	
	private final ApplicationContext applicationContext;
	
	private final MessageSource messageSource;
	
	private final Domains domains;
	
	private final Class<E> clazz;
	
	private final Class<G> gridClazz;
	
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
	}
	
	public void delayCreateContent() {
		setSizeFull();
		addStyleName("transactions");
		setTopBlock(createTopBlock());
		addComponent(getTopBlock());
		setMiddleBlock(createMiddleBlock());
		addComponent(getMiddleBlock());
		setBottomBlock(createBottomBlock());
		addComponent(getBottomBlock());
		setExpandRatio(getBottomBlock(), 1);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		DashboardEventBus.register(uel);
		setLvfb(new ListViewFragmentBuilder(event));

		// start alter state.
		getTopBlockBase().alterState(getLvfb(), MsgUtil.getListViewTitle(messageSource, getClazz().getSimpleName()));
		
		((MiddleBlock)middleBlock).alterState(getLvfb());
	}
	
	public void setTopBlock(TopBlock topBlock) {
		this.topBlock = topBlock;
	}

	public void setMiddleBlock(Component middleBlock) {
		this.middleBlock = middleBlock;
	}

	public void setBottomBlock(Component bottomBlock) {
		this.bottomBlock = bottomBlock;
	}

	public Component getTopBlock() {
		return topBlock;
	}
	
	public TopBlock getTopBlockBase() {
		return (BaseGridView<E, G, C>.TopBlock) topBlock;
	}


	public Component getMiddleBlock() {
		return middleBlock;
	}

	public Component getBottomBlock() {
		return bottomBlock;
	}

	@Override
	public void detach() {
		super.detach();
		// A new instance of TransactionsView is created every time it's
		// navigated to so we'll need to clean up references to it on detach.
		DashboardEventBus.unregister(uel);
	}

	
	protected TopBlock createTopBlock() {
		TopBlock tb = new TopBlock();

		tb.addClickListener(event -> {
			this.backward();
		});
		return tb;
	}

	public void setLvfb(ListViewFragmentBuilder lvfb) {
		this.lvfb = lvfb;
	}

	public UiEventListener getUel() {
		return uel;
	}

	protected Component createMiddleBlock() {
		DynButtonComponent dynMenu = new DynButtonComponent(messageSource,getButtonGroups());
		MiddleBlock mb = new MiddleBlock(dynMenu);

		mb.addDynaMenuItemClickListener(btnDsc -> {
			onDynButtonClicked(btnDsc);
		});
		return mb;
	}
	
	protected Component createBottomBlock() {
		setGrid(createGrid(messageSource, domains));
		BottomBlock bottomBlock = new BottomBlock();
		
		getGrid().addSelectionListener(event -> {
			((MiddleBlock)middleBlock).alterState(event.getSelected());
		});
		
		return bottomBlock;
	}

	public void setGrid(G grid) {
		this.grid = grid;
	}

	protected abstract G createGrid(MessageSource messageSource, Domains domains);


	protected abstract void onDynButtonClicked(ButtonDescription btnDsc);
	
	
	public ButtonGroup[] getButtonGroups() {
		return new ButtonGroup[]{ //
		new ButtonGroup(new EditButtonDescription(),new DeleteButtonDescription(), new UnArchiveButtonDescription()),//
		new ButtonGroup(new AddButtonDescription(),	new RefreshButtonDescription())};
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
		
		
		public Label getTitle() {
			return title;
		}


		public void setTitle(Label title) {
			this.title = title;
		}


		public Button getBackBtn() {
			return backBtn;
		}


		public void setBackBtn(Button backBtn) {
			this.backBtn = backBtn;
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
		

		public void alterState(Set<Object> selected) {
			this.menu.onSelectionChange(selected);
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
		public G getGrid() {
			return grid;
		}
	}
	
	public void refreshAfterItemNumberChange() {
		getGrid().getdContainer().setItemAdded(true);
		getGrid().getdContainer().setDirty(true);
		getGrid().deselectAll();
		getGrid().getdContainer().notifyItemSetChanged();
	}
	
	public void refreshAfterItemContentChange() {
		getGrid().getdContainer().setDirty(true);
		getGrid().deselectAll();
		getGrid().getdContainer().notifyItemSetChanged();
	}
	
	public G getGrid() {
		return grid;
	}

	protected class UiEventListener {
		@Subscribe
		public void browserResized(final BrowserResizeEvent event) {
			// Some columns are collapsed when browser window width gets small
			// enough to make the table fit better.
//			BottomBlock bb = (BaseGridView<E, G, C>.BottomBlock) bottomBlock;
		}
	}

}
