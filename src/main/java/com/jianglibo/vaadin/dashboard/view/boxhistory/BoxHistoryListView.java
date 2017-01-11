package com.jianglibo.vaadin.dashboard.view.boxhistory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumnWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridWrapper;
import com.jianglibo.vaadin.dashboard.config.CommonMenuItemIds;
import com.jianglibo.vaadin.dashboard.domain.BoxGroupHistory;
import com.jianglibo.vaadin.dashboard.domain.BoxHistory;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEventBus;
import com.jianglibo.vaadin.dashboard.repositories.BoxGroupHistoryRepository;
import com.jianglibo.vaadin.dashboard.repositories.BoxGroupRepository;
import com.jianglibo.vaadin.dashboard.repositories.BoxHistoryRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.SimpleButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonGroup;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.DynButtonComponent;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription.ButtonEnableType;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.DynButtonComponent.DynaMenuItemClickListener;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.RefreshButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.grid.BaseGridView;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.jianglibo.vaadin.dashboard.util.NotificationUtil;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

@SpringView(name = BoxHistoryListView.VIEW_NAME)
public class BoxHistoryListView extends BaseGridView<BoxHistory, BoxHistoryGrid, BoxHistoryContainer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(BoxHistoryListView.class);

	public static final String VIEW_NAME = "boxhistory";

	public static final FontAwesome ICON_VALUE = FontAwesome.HISTORY;

	private final BoxHistoryRepository boxHistoryRepository;
	
	
	private final BoxGroupHistoryRepository boxGroupHistoryRepository;
	
	private BoxGroupHistory boxGroupHistory;
	
	private final BoxHistoryRepository repository;

	@Autowired
	public BoxHistoryListView(BoxGroupHistoryRepository boxGroupHistoryRepository,BoxHistoryRepository boxHistoryRepository, BoxHistoryRepository repository,BoxGroupRepository boxGroupRepository, Domains domains, MessageSource messageSource,
			ApplicationContext applicationContext) {
		super(applicationContext, messageSource, domains, BoxHistory.class, BoxHistoryGrid.class);
		this.boxGroupHistoryRepository = boxGroupHistoryRepository;
		this.boxHistoryRepository = boxHistoryRepository;
		this.repository = repository;
		delayCreateContent();
	}

	@Override
	public ButtonGroup[] getButtonGroups() {
		return new ButtonGroup[] { //
				new ButtonGroup( //
						new SimpleButtonDescription(CommonMenuItemIds.DETAIL, FontAwesome.EDIT, ButtonEnableType.ONE)), //
				new ButtonGroup(new RefreshButtonDescription()) };
	}

//	@Override
//	public void delayCreateContent() {
//		setSizeFull();
//		addStyleName("transactions");
//		setTopBlock(createTopBlock());
//		addComponent(getTopBlock());
//		setMiddleBlock(createMiddleBlock());
//		addComponent(getMiddleBlock());
//		setBottomBlock(createBottomBlock());
//		addComponent(getBottomBlock());
//		setExpandRatio(getBottomBlock(), 1);
//	}

	@Override
	protected BottomBlock createBottomBlock() {
		setGrid(createGrid(getMessageSource(), getDomains()));
		MyBottomBlock bottomBlock = new MyBottomBlock();

		getGrid().addSelectionListener(event -> {
			((MyMiddleBlock) getMiddleBlock()).alterState(event.getSelected());
		});

		return bottomBlock;
	}

	@Override
	protected MiddleBlock createMiddleBlock() {
		DynButtonComponent dynMenu = new DynButtonComponent(getMessageSource(), getButtonGroups());
		MyMiddleBlock mb = new MyMiddleBlock(dynMenu);

		mb.addDynaMenuItemClickListener(btnDsc -> {
			onDynButtonClicked(btnDsc);
		});
		return mb;
	}

	@SuppressWarnings("serial")
	private class MyBottomBlock extends HorizontalLayout implements BottomBlock {
		public MyBottomBlock() {
			setSizeFull();
			addComponent(getGrid());
		}
	}

	@SuppressWarnings("serial")
	private class MyMiddleBlock extends HorizontalLayout implements MiddleBlock {

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

		public DynButtonComponent getMenu() {
			return menu;
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		DashboardEventBus.register(getUel());
		setLvfb(new ListViewFragmentBuilder(event));
		// start alter state.
		long boxGroupHistoryId = getLvfb().getLong("boxGroupHistoryId");
		String title = MsgUtil.getListViewTitle(getMessageSource(), getClazz().getSimpleName());
		if (boxGroupHistoryId > 0) {
			boxGroupHistory = boxGroupHistoryRepository.findOne(boxGroupHistoryId);
			title = boxGroupHistory.getDisplayName() + "'s " + title;
		}
		((TopBlockInList) getTopBlock()).alterState(getLvfb(), title);
		((MyMiddleBlock) getMiddleBlock()).alterState(getLvfb());
		((BoxHistoryContainer) (getGrid().getdContainer())).whenUriFragmentChange(getLvfb());
	}

	@Override
	public void onDynButtonClicked(ButtonDescription btnDesc) {
		List<BoxHistory> selected = getGrid().getSelectedRows().stream().map(o -> (BoxHistory) o).collect(Collectors.toList());
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
			refreshAfterItemNumberChange();
			break;
		case CommonMenuItemIds.REFRESH:
			refreshAfterItemNumberChange();
			break;
		case CommonMenuItemIds.DETAIL:
			UI.getCurrent().getNavigator().navigateTo(
					VIEW_NAME + "/detail/" + selected.iterator().next().getId() + "?pv=" + getLvfb().toNavigateString());
			break;
		case CommonMenuItemIds.ADD:
			UI.getCurrent().getNavigator().navigateTo(VIEW_NAME + "/edit/?pv=" + getLvfb().toNavigateString());
			break;
		case CommonMenuItemIds.UN_ARCHIVE:
			selected.forEach(bg -> {
				bg.setArchived(false);
			});
			repository.save(selected);
			refreshAfterItemNumberChange();
			break;
		default:
			LOGGER.error("unKnown menuName {}", btnDesc.getItemId());
		}
	}

	@Override
	protected BoxHistoryGrid createGrid(MessageSource messageSource, Domains domains) {
		VaadinGridWrapper vgw = getDomains().getGrids().get(BoxHistory.class.getSimpleName());
		List<String> sortableContainerPropertyIds = vgw.getSortableColumnNames();
		BoxHistoryContainer dContainer =  new BoxHistoryContainer(boxGroupHistoryRepository, boxHistoryRepository, getDomains(), vgw.getVg().defaultPerPage(), sortableContainerPropertyIds);
		
		List<String> columnNames = vgw.getColumns().stream().map(VaadinGridColumnWrapper::getName).collect(Collectors.toList());
		return new BoxHistoryGrid(dContainer,vgw, boxHistoryRepository,boxGroupHistoryRepository,  messageSource, sortableContainerPropertyIds, columnNames, vgw.getVg().messagePrefix());
	}
}
