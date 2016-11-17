package com.jianglibo.vaadin.dashboard.view.boxgroup;

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
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.BoxGroup;
import com.jianglibo.vaadin.dashboard.repositories.BoxGroupRepository;
import com.jianglibo.vaadin.dashboard.repositories.RepositoryCommonCustom;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.SimpleButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.UnArchiveButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonGroup;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.DeleteButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.EditButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.AddButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription.ButtonEnableType;
import com.jianglibo.vaadin.dashboard.uicomponent.grid.BaseGridView;
import com.jianglibo.vaadin.dashboard.util.NotificationUtil;
import com.jianglibo.vaadin.dashboard.view.clustersoftware.ClusterSoftwareView;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.UI;

@SpringView(name = BoxGroupListView.VIEW_NAME)
public class BoxGroupListView extends BaseGridView<BoxGroup, BoxGroupGrid, FreeContainer<BoxGroup>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(BoxGroupListView.class);

	public static final String VIEW_NAME = "boxgroups";
	
	private final BoxGroupRepository repository;

	
	@Autowired
	public BoxGroupListView(BoxGroupRepository repository,Domains domains, MessageSource messageSource,
			ApplicationContext applicationContext) {
		super(applicationContext, messageSource, domains, BoxGroup.class, BoxGroupGrid.class);
		this.repository = repository;
		delayCreateContent();
	}

	public ButtonGroup[] getButtonGroups() {
		return new ButtonGroup[]{ //
		new ButtonGroup(new EditButtonDescription(),new AddButtonDescription()),//
		new ButtonGroup(new DeleteButtonDescription(), new UnArchiveButtonDescription()),
		new ButtonGroup( //
					new SimpleButtonDescription("manageClusterSoftware", null, ButtonEnableType.ONE))};
	}
	
	@Override
	public void onDynButtonClicked(ButtonDescription btnDesc) {
		List<BoxGroup> selected = getGrid().getSelectedRows().stream().map(o -> (BoxGroup)o).collect(Collectors.toList());
		switch (btnDesc.getItemId()) {
		case CommonMenuItemIds.DELETE:
			for (BoxGroup bg : selected) {
				if (!bg.getBoxes().isEmpty()) {
					NotificationUtil.tray(getMessageSource(), "deleteWhenHasRelations", bg.getDisplayName(),Joiner.on(";").join(bg.getBoxes().stream().map(b -> b.getDisplayName()).iterator()));
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
			UI.getCurrent().getNavigator().navigateTo(VIEW_NAME + "/edit/" + selected.iterator().next().getId() + "?pv=" + getLvfb().toNavigateString());
			break;
		case CommonMenuItemIds.ADD:
			UI.getCurrent().getNavigator().navigateTo(VIEW_NAME + "/edit/?pv=" + getLvfb().toNavigateString());
			break;
		case CommonMenuItemIds.UN_ARCHIVE:
			selected.forEach(bg -> {
				bg.setArchived(false);
			});
			repository.save(selected);
			getGrid().getdContainer().fetchPage();
			getGrid().getdContainer().notifyItemSetChanged();
			break;
		case "manageClusterSoftware":
			UI.getCurrent().getNavigator().navigateTo(ClusterSoftwareView.VIEW_NAME + "/?boxgroup=" + selected.iterator().next().getId()  + "&pv=" + getLvfb().toNavigateString());
			break;
		default:
			LOGGER.error("unKnown menuName {}", btnDesc.getItemId());
		}
	}

	@Override
	protected BoxGroupGrid createGrid(MessageSource messageSource, Domains domains) {
		VaadinGridWrapper vgw = domains.getGrids().get(BoxGroup.class.getSimpleName());
		List<String> sortableContainerPropertyIds = vgw.getSortableColumnNames();
		
		List<String> columnNames = vgw.getColumns().stream().map(VaadinGridColumnWrapper::getName).collect(Collectors.toList());

		RepositoryCommonCustom<BoxGroup> rcc = domains.getRepositoryCommonCustom(BoxGroup.class.getSimpleName());
		Sort defaultSort = domains.getDefaultSort(BoxGroup.class);
		FreeContainer<BoxGroup> fc = new FreeContainer<>(rcc, defaultSort, BoxGroup.class, vgw.getVg().defaultPerPage(), sortableContainerPropertyIds);
		return new BoxGroupGrid(fc, vgw , messageSource, sortableContainerPropertyIds, columnNames, vgw.getVg().messagePrefix());
	}
}
