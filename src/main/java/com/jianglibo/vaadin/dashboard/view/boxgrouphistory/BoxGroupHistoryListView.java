package com.jianglibo.vaadin.dashboard.view.boxgrouphistory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.DashboardUI;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumnWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridWrapper;
import com.jianglibo.vaadin.dashboard.config.CommonMenuItemIds;
import com.jianglibo.vaadin.dashboard.domain.BoxGroup;
import com.jianglibo.vaadin.dashboard.domain.BoxGroupHistory;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEventBus;
import com.jianglibo.vaadin.dashboard.repositories.BoxGroupHistoryRepository;
import com.jianglibo.vaadin.dashboard.repositories.BoxGroupRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.SimpleButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonGroup;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.DeleteButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.DynButtonComponent;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription.ButtonEnableType;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.DynButtonComponent.DynaMenuItemClickListener;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.EditButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.grid.BaseGridView;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.jianglibo.vaadin.dashboard.util.NotificationUtil;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.jianglibo.vaadin.dashboard.view.boxhistory.BoxHistoryListView;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

@SpringView(name = BoxGroupHistoryListView.VIEW_NAME)
public class BoxGroupHistoryListView extends BaseGridView<BoxGroupHistory, BoxGroupHistoryGrid, BoxGroupHistoryContainer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(BoxGroupHistoryListView.class);

	public static final String VIEW_NAME = "clusterhistory";

	public static final FontAwesome ICON_VALUE = FontAwesome.HISTORY;

	private BoxGroupRepository boxGroupRepository;
	
	private final BoxGroupHistoryRepository repository;
	
	private BoxGroup boxGroup;

	@Autowired
	public BoxGroupHistoryListView(BoxGroupHistoryRepository repository, BoxGroupRepository boxGroupRepository, Domains domains, MessageSource messageSource,
			ApplicationContext applicationContext) {
		super(applicationContext, messageSource, domains, BoxGroupHistory.class, BoxGroupHistoryGrid.class);
		this.boxGroupRepository = boxGroupRepository;
		this.repository = repository;
		delayCreateContent();
	}

	@Override
	public ButtonGroup[] getButtonGroups() {
		return new ButtonGroup[] { //
				new ButtonGroup(new EditButtonDescription(), new DeleteButtonDescription()), //
				new ButtonGroup( //
						new SimpleButtonDescription("boxhistories", null, ButtonEnableType.ONE)) };
	}

	@Override
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
	protected Component createBottomBlock() {
		setGrid(createGrid(getMessageSource(), getDomains()));
		MyBottomBlock bottomBlock = new MyBottomBlock();

		getGrid().addSelectionListener(event -> {
			((MyMiddleBlock) getMiddleBlock()).alterState(event.getSelected());
		});

		return bottomBlock;
	}

	@Override
	protected Component createMiddleBlock() {
		DynButtonComponent dynMenu = new DynButtonComponent(getMessageSource(), getButtonGroups());
		MyMiddleBlock mb = new MyMiddleBlock(dynMenu);

		mb.addDynaMenuItemClickListener(btnDsc -> {
			onDynButtonClicked(btnDsc);
		});
		return mb;
	}

	@SuppressWarnings("serial")
	private class MyBottomBlock extends HorizontalLayout {
		public MyBottomBlock() {
			setSizeFull();
			addComponent(getGrid());
		}
	}

	@SuppressWarnings("serial")
	private class MyMiddleBlock extends HorizontalLayout {

		private DynButtonComponent menu;

		public MyMiddleBlock(DynButtonComponent menu) {
			this.menu = menu;
			addStyleName("table-controller");
			setWidth("100%");
			addComponent(menu);

			setComponentAlignment(menu, Alignment.MIDDLE_LEFT);


			HorizontalLayout hl = new HorizontalLayout();
			hl.setSpacing(true);
			StyleUtil.setMarginRightTwenty(hl);
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
	}

	@Override
	public void enter(ViewChangeEvent event) {
		DashboardEventBus.register(getUel());
		setLvfb(new ListViewFragmentBuilder(event));
		// start alter state.
		long boxGroupId = getLvfb().getLong("boxGroupId");
		String title = MsgUtil.getListViewTitle(getMessageSource(), getClazz().getSimpleName());
		if (boxGroupId > 0) {
			boxGroup = boxGroupRepository.findOne(boxGroupId);
			title = boxGroup.getDisplayName() + "'s " + title;
		} else {
			
		}
		((TopBlock) getTopBlock()).alterState(getLvfb(), title);
		((MyMiddleBlock) getMiddleBlock()).alterState(getLvfb());
		((BoxGroupHistoryContainer) (getGrid().getdContainer())).whenUriFragmentChange(getLvfb());
		BoxGroupHistoryViewMenuItem svmi = (BoxGroupHistoryViewMenuItem)((DashboardUI)UI.getCurrent()).getDm().getMmis().getMenuMap().get(BoxGroupHistoryViewMenuItem.class.getName());
		svmi.updateNotificationsCount(0);
	}

	@Override
	public void onDynButtonClicked(ButtonDescription btnDesc) {
		List<BoxGroupHistory> selected = getGrid().getSelectedRows().stream().map(o -> (BoxGroupHistory) o).collect(Collectors.toList());
		switch (btnDesc.getItemId()) {
		case CommonMenuItemIds.DELETE:
			selected.forEach(b -> {
				if (b.isArchived()) {
					repository.delete(b.getId());
					NotificationUtil.tray(getMessageSource(), "deletedone", b.getDisplayName());
				} else {
					b.setArchived(true);
					NotificationUtil.tray(getMessageSource(), "archivedone", b.getDisplayName());
					repository.save(b);
				}
			});
			getGrid().getdContainer().fetchPageAfterModify();
			break;
		case CommonMenuItemIds.REFRESH:
			getGrid().getdContainer().refresh();
			break;
		case CommonMenuItemIds.EDIT:
			UI.getCurrent().getNavigator().navigateTo(
					VIEW_NAME + "/edit/" + selected.iterator().next().getId() + "?pv=" + getLvfb().toNavigateString());
			break;
		case CommonMenuItemIds.ADD:
			UI.getCurrent().getNavigator().navigateTo(VIEW_NAME + "/edit/?pv=" + getLvfb().toNavigateString());
			break;
		case CommonMenuItemIds.UN_ARCHIVE:
			selected.forEach(bg -> {
				bg.setArchived(false);
			});
			repository.save(selected);
			getGrid().getdContainer().setDirty(true);
			getGrid().deselectAll();
			getGrid().getdContainer().notifyItemSetChanged();
			break;
		case "boxhistories":
			UI.getCurrent().getNavigator().navigateTo(BoxHistoryListView.VIEW_NAME + "/?boxGroupHistoryId=" + selected.get(0).getId() + "&pv=" + getLvfb().toNavigateString());
			break;
		default:
			LOGGER.error("unKnown menuName {}", btnDesc.getItemId());
		}
	}

	@Override
	protected BoxGroupHistoryGrid createGrid(MessageSource messageSource, Domains domains) {
		VaadinGridWrapper vgw = getDomains().getGrids().get(BoxGroupHistory.class.getSimpleName());
		BoxGroupHistoryContainer dContainer =  new BoxGroupHistoryContainer(repository, getDomains(), vgw.getVg().defaultPerPage(), vgw.getSortableColumnNames());
		List<String> sortableContainerPropertyIds = vgw.getSortableColumnNames();
		List<String> columnNames = vgw.getColumns().stream().map(VaadinGridColumnWrapper::getName).collect(Collectors.toList());
		return new BoxGroupHistoryGrid(dContainer,vgw, repository, messageSource, sortableContainerPropertyIds, columnNames, vgw.getVg().messagePrefix());
	}
}
