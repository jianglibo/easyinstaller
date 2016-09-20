package com.jianglibo.vaadin.dashboard.view.software;

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
import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.repositories.SoftwareRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription.ButtonEnableType;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonGroup;
import com.jianglibo.vaadin.dashboard.view.BaseListView;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.UI;

@SpringView(name = SoftwareListView.VIEW_NAME)
public class SoftwareListView extends BaseListView<Software, SoftwareTable, SoftwareRepository>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SoftwareListView.class);
	

	public static final String VIEW_NAME = "software";

	public static final FontAwesome ICON_VALUE = FontAwesome.COGS;
	
	
	@Autowired
	public SoftwareListView(SoftwareRepository repository,Domains domains, MessageSource messageSource,
			ApplicationContext applicationContext) {
		super(applicationContext, messageSource, domains,repository, Software.class, SoftwareTable.class);
	}

	@Override
	public void handleException(Throwable exception, SubscriberExceptionContext context) {
		exception.printStackTrace();
	}

	@Override
	public void onDynButtonClicked(ButtonDescription btnDesc) {
		Collection<Software> selected;
		switch (btnDesc.getItemId()) {
		case CommonMenuItemIds.DELETE:
			selected = (Collection<Software>) getTable().getValue();
			selected.forEach(b -> {
				if (b.isArchived()) {
					getRepository().delete(b);
				} else {
					b.setArchived(true);
					getRepository().save(b);
				}
			});
			((SoftwareContainer)getTable().getContainerDataSource()).refresh();
			break;
		case CommonMenuItemIds.REFRESH:
			((SoftwareContainer)getTable().getContainerDataSource()).refresh();
			break;
		case CommonMenuItemIds.EDIT:
			selected = (Collection<Software>) getTable().getValue();
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
	public SoftwareTable createTable() {
		SoftwareContainer sfc = new SoftwareContainer(getRepository(), getDomains());
		return new SoftwareTable(getMessageSource(), getDomains(),sfc, getRepository());
	}

	@Override
	public ButtonGroup[] getButtonGroups() {
		return new ButtonGroup[]{ //
		new ButtonGroup(new ButtonDescription(CommonMenuItemIds.EDIT, FontAwesome.EDIT, ButtonEnableType.ONE), //
				new ButtonDescription(CommonMenuItemIds.DELETE, FontAwesome.TRASH, ButtonEnableType.MANY)),//
		new ButtonGroup(new ButtonDescription(CommonMenuItemIds.ADD, FontAwesome.PLUS, ButtonEnableType.ALWAYS)),//
		new ButtonGroup(new ButtonDescription(CommonMenuItemIds.REFRESH, FontAwesome.REFRESH, ButtonEnableType.ALWAYS))};
	}

	@Override
	public String getListViewName() {
		return VIEW_NAME;
	}
}
