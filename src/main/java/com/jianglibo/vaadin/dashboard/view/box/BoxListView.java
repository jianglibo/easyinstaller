package com.jianglibo.vaadin.dashboard.view.box;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;

import com.google.common.base.Joiner;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumnWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridWrapper;
import com.jianglibo.vaadin.dashboard.config.CommonMenuItemIds;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.event.view.PageMetaEvent;
import com.jianglibo.vaadin.dashboard.repositories.BoxRepository;
import com.jianglibo.vaadin.dashboard.repositories.RepositoryCommonCustom;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.UnArchiveButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.AddButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonGroup;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.DeleteButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.EditButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.grid.BaseGridView;
import com.jianglibo.vaadin.dashboard.util.NotificationUtil;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.UI;

@SpringView(name = BoxListView.VIEW_NAME)
public class BoxListView extends BaseGridView<Box, BoxGrid, FreeContainer<Box>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(BoxListView.class);

	public static final String VIEW_NAME = "box";
	
	private final BoxRepository repository;

	@Autowired
	public BoxListView(BoxRepository repository, Domains domains, MessageSource messageSource,
			ApplicationContext applicationContext) {
		super(applicationContext, messageSource, domains, Box.class, BoxGrid.class);
		this.repository = repository;
		delayCreateContent();
	}

	public void whenTotalPageChange(PageMetaEvent tpe) {
//		getTable().setColumnFooter("createdAt", String.valueOf(tpe.getTotalRecord()));
	}

	@Override
	public void onDynButtonClicked(ButtonDescription btnDesc) {
		List<Box> selected = getGrid().getSelectedRows().stream().map(o -> (Box)o).collect(Collectors.toList());
		switch (btnDesc.getItemId()) {
		case CommonMenuItemIds.DELETE:
			for (Box bItem : selected) {
				if (!bItem.getBoxGroups().isEmpty()) {
					NotificationUtil.tray(getMessageSource(), "deleteWhenHasRelations", bItem.getDisplayName(),Joiner.on(";").join(bItem.getBoxGroups().stream().map(bg -> bg.getDisplayName()).iterator()));
					return;
				}
			}
			selected.forEach(b -> {
				if (b.isArchived()) {
					repository.delete(b);
					NotificationUtil.tray(getMessageSource(), "deletedone", b.getDisplayName());
				} else {
					b.setArchived(true);
					NotificationUtil.tray(getMessageSource(), "archivedone", b.getDisplayName());
					repository.save(b);
				}
			});
			getGrid().getdContainer().fetchPage();
			getGrid().getdContainer().notifyItemSetChanged();
			break;
		case CommonMenuItemIds.REFRESH:
			getGrid().getdContainer().refresh();
			break;
		case CommonMenuItemIds.EDIT:
			UI.getCurrent().getNavigator().navigateTo(
					VIEW_NAME + "/edit/" + selected.iterator().next().getId() + "?pv=" + getLvfb().toNavigateString());
			break;
		case CommonMenuItemIds.ADD:
			UI.getCurrent().getNavigator().navigateTo(VIEW_NAME + "/edit");
			break;
		case CommonMenuItemIds.UN_ARCHIVE:
			selected.forEach(b -> {
				b.setArchived(false);
			});
			repository.save(selected);
			getGrid().getdContainer().fetchPage();
			getGrid().getdContainer().notifyItemSetChanged();
			break;
		default:
			LOGGER.error("unKnown menuName {}", btnDesc.getItemId());
		}
	}

	@Override
	public ButtonGroup[] getButtonGroups() {
		return new ButtonGroup[] { //
				new ButtonGroup(new EditButtonDescription(),new AddButtonDescription()), //
				new ButtonGroup(new DeleteButtonDescription(), new UnArchiveButtonDescription())};
	}
	
	@Override
	protected BoxGrid createGrid(MessageSource messageSource, Domains domains) {
		VaadinGridWrapper vgw = domains.getGrids().get(Box.class.getSimpleName());
		List<String> sortableContainerPropertyIds = vgw.getSortableColumnNames();
		
		List<String> columnNames = vgw.getColumns().stream().map(VaadinGridColumnWrapper::getName).collect(Collectors.toList());

		RepositoryCommonCustom<Box> rcc = domains.getRepositoryCommonCustom(Box.class.getSimpleName());
		Sort defaultSort = domains.getDefaultSort(Box.class);
		FreeContainer<Box> fc = new FreeContainer<>(rcc, defaultSort, Box.class, vgw.getVg().defaultPerPage(), sortableContainerPropertyIds);
		return new BoxGrid(fc, vgw , messageSource, sortableContainerPropertyIds, columnNames, vgw.getVg().messagePrefix());
	}
}
