package com.jianglibo.vaadin.dashboard.view.boxhistory;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.config.CommonMenuItemIds;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.BoxHistory;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Kkv;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEventBus;
import com.jianglibo.vaadin.dashboard.repositories.BoxRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonGroup;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription.ButtonEnableType;
import com.jianglibo.vaadin.dashboard.uicomponent.grid.BaseGridView;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Component;
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
	
	private BoxRepository boxRepository;

	@Autowired
	public BoxHistoryListView(BoxRepository repository, Domains domains, MessageSource messageSource,
			ApplicationContext applicationContext) {
		super(applicationContext, messageSource, domains, BoxHistory.class, BoxHistoryGrid.class);
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
			((MiddleBlock)getMiddleBlock()).alterState(event.getSelected().size());
		});
		
		return bottomBlock;
	}
	
	
	@SuppressWarnings("serial")
	private class MyBottomBlock extends HorizontalLayout {
		public MyBottomBlock() {
			setSizeFull();
			addComponent(getGrid());
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		DashboardEventBus.register(getUel());
		setLvfb(new ListViewFragmentBuilder(event));
		// start alter state.
		long boxid = getLvfb().getLong("boxid");
		String title = MsgUtil.getListViewTitle(getMessageSource(), getClazz().getSimpleName());
		if ( boxid > 0) {
			Box box = boxRepository.findOne(boxid);
			title = box.getDisplayName() + "'s " + title;
			
		}
		((TopBlock) getTopBlock()).getTitle().setCaption(title);
//		alterState(getLvfb(),
//				MsgUtil.getListViewTitle(getMessageSource(), getClazz().getSimpleName()));
		((MiddleBlock) getMiddleBlock()).alterState(getLvfb());
		((BoxHistoryContainer)(getGrid().getOriginDataSource())).whenUriFragmentChange(getLvfb());
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
	protected BoxHistoryGrid createGrid(MessageSource messageSource, Domains domains, Class<BoxHistory> clazz) {
		return new BoxHistoryGrid(messageSource, domains, clazz);
	}
}
