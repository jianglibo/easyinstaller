package com.jianglibo.vaadin.dashboard.view.clusterhistory;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.config.CommonMenuItemIds;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.ClusterHistory;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Kkv;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEventBus;
import com.jianglibo.vaadin.dashboard.repositories.BoxRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonGroup;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.DynButtonComponent;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription.ButtonEnableType;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.DynButtonComponent.DynaMenuItemClickListener;
import com.jianglibo.vaadin.dashboard.uicomponent.grid.BaseGridView;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.jianglibo.vaadin.dashboard.view.boxhistory.BoxHistoryListView;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

@SpringView(name = ClusterHistoryListView.VIEW_NAME)
public class ClusterHistoryListView extends BaseGridView<ClusterHistory, ClusterHistoryGrid, ClusterHistoryContainer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ClusterHistoryListView.class);

	public static final String VIEW_NAME = "clusterhistory";

	public static final FontAwesome ICON_VALUE = FontAwesome.HISTORY;

	private BoxRepository boxRepository;

	@Autowired
	public ClusterHistoryListView(BoxRepository repository, Domains domains, MessageSource messageSource,
			ApplicationContext applicationContext) {
		super(applicationContext, messageSource, domains, ClusterHistory.class, ClusterHistoryGrid.class);
		this.boxRepository = repository;
		delayCreateContent();
	}

	@Override
	public ButtonGroup[] getButtonGroups() {
		return new ButtonGroup[] { //
				new ButtonGroup( //
						new ButtonDescription(CommonMenuItemIds.EDIT, FontAwesome.EDIT, ButtonEnableType.ONE), //
						new ButtonDescription(CommonMenuItemIds.DELETE, FontAwesome.TRASH, ButtonEnableType.MANY)), //
				new ButtonGroup( //
						new ButtonDescription(CommonMenuItemIds.REFRESH, FontAwesome.REFRESH,
								ButtonEnableType.ALWAYS)) };
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
		setGrid(createGrid(getMessageSource(), getDomains(), getClazz()));
		MyBottomBlock bottomBlock = new MyBottomBlock();

		getGrid().addSelectionListener(event -> {
			((MyMiddleBlock) getMiddleBlock()).alterState(event.getSelected().size());
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

		private CheckBox checkboxServer;
		private CheckBox checkboxCluster;

		public MyMiddleBlock(DynButtonComponent menu) {
			this.menu = menu;
			addStyleName("table-controller");
			setWidth("100%");
			addComponent(menu);

			setComponentAlignment(menu, Alignment.MIDDLE_LEFT);

			checkboxCluster = new CheckBox("Cluster Histories");
			checkboxCluster.setValue(true);
			checkboxCluster.setEnabled(false);

			checkboxServer = new CheckBox("Server Histories");
			checkboxServer.setValue(false);

			checkboxServer.addValueChangeListener(event -> {
				UI.getCurrent().getNavigator().navigateTo(BoxHistoryListView.VIEW_NAME);
			});
			

			HorizontalLayout hl = new HorizontalLayout();
			hl.setSpacing(true);
			hl.addComponents(checkboxServer, checkboxCluster);
			StyleUtil.setMarginRightTwenty(hl);
			addComponent(hl);
			setComponentAlignment(hl, Alignment.MIDDLE_RIGHT);
		}

		public void alterState(ListViewFragmentBuilder lvfb) {
			if (lvfb.toNavigateString().contains("ClusterHistory")) {
				checkboxServer.setValue(true);
			} else {
				checkboxCluster.setValue(true);
			}
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

	@Override
	public void enter(ViewChangeEvent event) {
		DashboardEventBus.register(getUel());
		setLvfb(new ListViewFragmentBuilder(event));
		// start alter state.
		long boxid = getLvfb().getLong("boxid");
		String title = MsgUtil.getListViewTitle(getMessageSource(), getClazz().getSimpleName());
		if (boxid > 0) {
			Box box = boxRepository.findOne(boxid);
			title = box.getDisplayName() + "'s " + title;

		}
		((TopBlock) getTopBlock()).getTitle().setValue(title);
		// alterState(getLvfb(),
		// MsgUtil.getListViewTitle(getMessageSource(),
		// getClazz().getSimpleName()));
		((MyMiddleBlock) getMiddleBlock()).alterState(getLvfb());
		((ClusterHistoryContainer) (getGrid().getOriginDataSource())).whenUriFragmentChange(getLvfb());
	}

	@Override
	public void onDynButtonClicked(ButtonDescription btnDesc) {
		List<Kkv> selected = getGrid().getSelectedRows().stream().map(o -> (Kkv) o).collect(Collectors.toList());
		switch (btnDesc.getItemId()) {
		case CommonMenuItemIds.DELETE:
			selected.forEach(b -> {
				if (b.isArchived()) {
					// getGrid().getContainerDataSource()
					// getRepository().delete(b);
				} else {
					b.setArchived(true);
					// getRepository().save(b);
				}
			});
			// ((BoxContainer)getTable().getContainerDataSource()).refresh();
			break;
		case CommonMenuItemIds.REFRESH:
			// ((BoxContainer)getTable().getContainerDataSource()).refresh();
			break;
		case CommonMenuItemIds.EDIT:
			UI.getCurrent().getNavigator().navigateTo(
					VIEW_NAME + "/edit/" + selected.iterator().next().getId() + "?pv=" + getLvfb().toNavigateString());
			break;
		case CommonMenuItemIds.ADD:
			UI.getCurrent().getNavigator().navigateTo(VIEW_NAME + "/edit/?pv=" + getLvfb().toNavigateString());
			break;
		default:
			LOGGER.error("unKnown menuName {}", btnDesc.getItemId());
		}
	}

	@Override
	protected ClusterHistoryGrid createGrid(MessageSource messageSource, Domains domains, Class<ClusterHistory> clazz) {
		return new ClusterHistoryGrid(messageSource, domains, clazz);
	}
}
