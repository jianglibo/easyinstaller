package com.jianglibo.vaadin.dashboard.uicomponent.grid;

import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumns;
import com.jianglibo.vaadin.dashboard.config.CommonMenuItemIds;
import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription.ButtonEnableType;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonGroup;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.DynButtonComponent;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.DynButtonComponent.DynaMenuItemClickListener;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
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
	
	private VaadinTableColumns tableColumns;
	
	private ListViewFragmentBuilder lvfb;
	
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
		
		tableColumns = domains.getTableColumns().get(clazz.getSimpleName());

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

		mb.addTrashBtnClickListener(event -> {
			String styles = event.getButton().getStyleName();
			if (StyleUtil.hasStyleName(styles, ValoTheme.BUTTON_PRIMARY)) {
				trashBtnClicked(false);
			} else {
				trashBtnClicked(true);
			}
		});
		mb.addDynaMenuItemClickListener(btnDsc -> {
			onDynButtonClicked(btnDsc);
		});
		return mb;
	}
	
	private Component createBottomBlock() {
		G grid = createGrid(messageSource, domains, clazz);
		BottomBlock bottomBlock = new BottomBlock(grid);
		
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
	
	public void trashBtnClicked(boolean b) {
		String nvs = getLvfb().setFilterStr("").setCurrentPage(1)
				.setBoolean(ListViewFragmentBuilder.TRASHED_PARAM_NAME, b).toNavigateString();
		UI.getCurrent().getNavigator().navigateTo(nvs);
	}
	
	public ButtonGroup[] getButtonGroups() {
		return new ButtonGroup[]{ //
		new ButtonGroup(new ButtonDescription(CommonMenuItemIds.EDIT, FontAwesome.EDIT, ButtonEnableType.ONE), //
				new ButtonDescription(CommonMenuItemIds.DELETE, FontAwesome.TRASH, ButtonEnableType.MANY)),//
		new ButtonGroup(new ButtonDescription(CommonMenuItemIds.REFRESH, FontAwesome.REFRESH, ButtonEnableType.ALWAYS))};
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

		private Button trashBt;
		
		private DynButtonComponent menu;

		public MiddleBlock(DynButtonComponent menu) {
			this.menu = menu;
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

	}

	protected class BottomBlock extends HorizontalLayout {
		private G grid;

		public BottomBlock(G grid) {
			this.grid = grid;
			setWidth("100%");
			setHeight("100%");
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
}
