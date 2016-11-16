package com.jianglibo.vaadin.dashboard.unused;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;

import com.google.common.eventbus.SubscriberExceptionContext;
import com.jianglibo.vaadin.dashboard.config.CommonMenuItemIds;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.SimpleButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription.ButtonEnableType;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonGroup;
import com.jianglibo.vaadin.dashboard.view.BaseListView;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.UI;

//@SpringView(name = StepRunListView.VIEW_NAME)
public class StepRunListView extends BaseListView<StepRun, StepRunTable, StepRunRepository>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(StepRunListView.class);
	

	public static final String VIEW_NAME = "steprun";

	public static final FontAwesome ICON_VALUE = FontAwesome.APPLE;
	
	
	@Autowired
	public StepRunListView(StepRunRepository repository,Domains domains, MessageSource messageSource,
			ApplicationContext applicationContext) {
		super(applicationContext, messageSource, domains,repository, StepRun.class, StepRunTable.class);
	}

	@Override
	public void handleException(Throwable exception, SubscriberExceptionContext context) {
		exception.printStackTrace();
		
	}

	@Override
	public void onDynButtonClicked(ButtonDescription btnDesc) {
		Collection<StepRun> selected;
		switch (btnDesc.getItemId()) {
		case CommonMenuItemIds.DELETE:
			selected = (Collection<StepRun>) getTable().getValue();
			selected.forEach(b -> {
				if (b.isArchived()) {
					getRepository().delete(b);
				} else {
					b.setArchived(true);
					getRepository().save(b);
				}
			});
			((StepRunContainer)getTable().getContainerDataSource()).refresh();
			break;
		case CommonMenuItemIds.REFRESH:
			((StepRunContainer)getTable().getContainerDataSource()).refresh();
			break;
		case CommonMenuItemIds.EDIT:
			selected = (Collection<StepRun>) getTable().getValue();
			UI.getCurrent().getNavigator().navigateTo(VIEW_NAME + "/edit/" + selected.iterator().next().getId() + "?pv=" + getLvfb().toNavigateString());
			break;
		case CommonMenuItemIds.ADD:
			UI.getCurrent().getNavigator().navigateTo(VIEW_NAME + "/edit");
			break;
		default:
			LOGGER.error("unKnown menuName {}", btnDesc.getItemId());
		}
		
	}

	public void notifySort(Sort sort) {
	}

	@Override
	public StepRunTable createTable() {
		StepRunContainer src = new StepRunContainer(getRepository(), getDomains());
		return new StepRunTable(getMessageSource(), getDomains(), src, getRepository());
	}

	@Override
	public ButtonGroup[] getButtonGroups() {
		return new ButtonGroup[]{ //
		new ButtonGroup(new SimpleButtonDescription(CommonMenuItemIds.EDIT, FontAwesome.EDIT, ButtonEnableType.ONE), //
				new SimpleButtonDescription(CommonMenuItemIds.DELETE, FontAwesome.TRASH, ButtonEnableType.MANY)),//
		new ButtonGroup(new SimpleButtonDescription(CommonMenuItemIds.REFRESH, FontAwesome.REFRESH, ButtonEnableType.ALWAYS))};
	}

	@Override
	public String getListViewName() {
		return VIEW_NAME;
	}
}
