package com.jianglibo.vaadin.dashboard.view.stepdefine;

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
import com.jianglibo.vaadin.dashboard.domain.StepDefine;
import com.jianglibo.vaadin.dashboard.repositories.StepDefineRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription;
import com.jianglibo.vaadin.dashboard.view.BaseListView;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.UI;

@SpringView(name = StepDefineListView.VIEW_NAME)
public class StepDefineListView extends BaseListView<StepDefine, StepDefineTable, StepDefineRepository>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(StepDefineListView.class);
	

	public static final String VIEW_NAME = "stepdefine";

	public static final FontAwesome ICON_VALUE = FontAwesome.CUBE;
	
	
	@Autowired
	public StepDefineListView(StepDefineRepository repository,Domains domains, MessageSource messageSource,
			ApplicationContext applicationContext) {
		super(applicationContext, messageSource, domains, repository, StepDefine.class, StepDefineTable.class);
	}


	


	@Override
	public void handleException(Throwable exception, SubscriberExceptionContext context) {
		exception.printStackTrace();
		
	}

	@Override
	public void onDynButtonClicked(ButtonDescription btnDesc) {
		Collection<StepDefine> selected;
		switch (btnDesc.getItemId()) {
		case CommonMenuItemIds.DELETE:
			selected = (Collection<StepDefine>) getTable().getValue();
			selected.forEach(b -> {
				if (b.isArchived()) {
					getRepository().delete(b);
				} else {
					b.setArchived(true);
					getRepository().save(b);
				}
			});
			((StepDefineContainer)getTable().getContainerDataSource()).refresh();
			break;
		case CommonMenuItemIds.REFRESH:
			((StepDefineContainer)getTable().getContainerDataSource()).refresh();
			break;
		case CommonMenuItemIds.EDIT:
			selected = (Collection<StepDefine>) getTable().getValue();
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
	public StepDefineTable createTable() {
		StepDefineContainer sdc = new StepDefineContainer(getRepository(), getDomains());
		return new StepDefineTable(getMessageSource(), getDomains(), sdc, getRepository());
	}

	@Override
	public String getListViewName() {
		return VIEW_NAME;
	}
}
