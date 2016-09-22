package com.jianglibo.vaadin.dashboard.view.box;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;

import com.jianglibo.vaadin.dashboard.config.CommonMenuItemIds;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.event.view.PageMetaEvent;
import com.jianglibo.vaadin.dashboard.repositories.BoxRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription.ButtonEnableType;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonGroup;
import com.jianglibo.vaadin.dashboard.view.BaseListView;
import com.jianglibo.vaadin.dashboard.view.boxhistory.BoxHistoryListView;
import com.jianglibo.vaadin.dashboard.view.boxsoftware.BoxSoftwareView;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.UI;

@SpringView(name = BoxListView.VIEW_NAME)
public class BoxListView extends BaseListView<Box, BoxTable, BoxRepository> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(BoxListView.class);

	public static final String VIEW_NAME = "box";

	@Autowired
	public BoxListView(BoxRepository repository, Domains domains, MessageSource messageSource,
			ApplicationContext applicationContext) {
		super(applicationContext, messageSource, domains, repository, Box.class, BoxTable.class);
		delayCreateContent();
	}

	public void whenTotalPageChange(PageMetaEvent tpe) {
		getTable().setColumnFooter("createdAt", String.valueOf(tpe.getTotalRecord()));
	}

	@Override
	public void onDynButtonClicked(ButtonDescription btnDesc) {
		Collection<Box> selected = (Collection<Box>) getTable().getValue();
		switch (btnDesc.getItemId()) {
		case CommonMenuItemIds.DELETE:

			selected.forEach(b -> {
				if (b.isArchived()) {
					getRepository().delete(b);
				} else {
					b.setArchived(true);
					getRepository().save(b);
				}
			});
			((BoxContainer) getTable().getContainerDataSource()).refresh();
			break;
		case CommonMenuItemIds.REFRESH:
			((BoxContainer) getTable().getContainerDataSource()).refresh();
			break;
		case CommonMenuItemIds.EDIT:
			UI.getCurrent().getNavigator().navigateTo(
					VIEW_NAME + "/edit/" + selected.iterator().next().getId() + "?pv=" + getLvfb().toNavigateString());
			break;
		case CommonMenuItemIds.ADD:
			UI.getCurrent().getNavigator().navigateTo(VIEW_NAME + "/edit");
			break;
		case "manageBoxSoftware":
			UI.getCurrent().getNavigator().navigateTo(BoxSoftwareView.VIEW_NAME + "/?boxid="
					+ selected.iterator().next().getId() + "&pv=" + getLvfb().toNavigateString());
			break;
		default:
			LOGGER.error("unKnown menuName {}", btnDesc.getItemId());
		}
	}

	@Override
	public ButtonGroup[] getButtonGroups() {
		return new ButtonGroup[] { //
				new ButtonGroup(new ButtonDescription(CommonMenuItemIds.EDIT, FontAwesome.EDIT, ButtonEnableType.ONE), //
						new ButtonDescription(CommonMenuItemIds.ADD, FontAwesome.PLUS, ButtonEnableType.ALWAYS)), //
				new ButtonGroup(
						new ButtonDescription(CommonMenuItemIds.DELETE, FontAwesome.TRASH, ButtonEnableType.MANY)), //
				new ButtonGroup(new ButtonDescription("manageBoxSoftware", null, ButtonEnableType.ONE)) };
	}
	
	public void notifySort(Sort sort) {

	}

	@Override
	public BoxTable createTable() {
		BoxContainer bc = new BoxContainer(getRepository(), getDomains());
		return new BoxTable(getMessageSource(), getDomains(), bc, getRepository());
	}

	@Override
	public String getListViewName() {
		return VIEW_NAME;
	}
}
