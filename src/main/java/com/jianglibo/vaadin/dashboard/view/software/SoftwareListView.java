package com.jianglibo.vaadin.dashboard.view.software;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;

import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumnWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridWrapper;
import com.jianglibo.vaadin.dashboard.config.CommonMenuItemIds;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.repositories.RepositoryCommonCustom;
import com.jianglibo.vaadin.dashboard.repositories.SoftwareRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.SimpleButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.UnArchiveButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.AddButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription.ButtonEnableType;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonGroup;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.DeleteButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.EditButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.grid.BaseGridView;
import com.jianglibo.vaadin.dashboard.util.NotificationUtil;
import com.jianglibo.vaadin.dashboard.view.importsoftware.ImportSoftwareView;
import com.jianglibo.vaadin.dashboard.view.textfile.TextFileListView;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.UI;

@SpringView(name = SoftwareListView.VIEW_NAME)
public class SoftwareListView extends BaseGridView<Software, SoftwareGrid, FreeContainer<Software>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SoftwareListView.class);
	

	public static final String VIEW_NAME = "software";

	public static final FontAwesome ICON_VALUE = FontAwesome.COGS;
	
	private final SoftwareRepository repository;
	
	@Autowired
	public SoftwareListView(SoftwareRepository repository,Domains domains, MessageSource messageSource,
			ApplicationContext applicationContext) {
		super(applicationContext, messageSource, domains, Software.class, SoftwareGrid.class);
		this.repository = repository;
		delayCreateContent();
	}

	@Override
	public void onDynButtonClicked(ButtonDescription btnDesc) {
		List<Software> selected = getGrid().getSelectedRows().stream().map(o -> (Software)o).collect(Collectors.toList());
		switch (btnDesc.getItemId()) {
		case CommonMenuItemIds.DELETE:
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
//			((SoftwareContainer)getTable().getContainerDataSource()).refresh();
			break;
		case CommonMenuItemIds.EDIT:
			UI.getCurrent().getNavigator().navigateTo(VIEW_NAME + "/edit/" + selected.iterator().next().getId() + "?pv=" + getLvfb().toNavigateString());
			break;
		case CommonMenuItemIds.ADD:
			UI.getCurrent().getNavigator().navigateTo(VIEW_NAME + "/edit");
			break;
		case "importSoftware":
			UI.getCurrent().getNavigator().navigateTo(ImportSoftwareView.VIEW_NAME + "/?pv=" + getLvfb().toNavigateString());
			break;
		case "softwaretxtfiles":
			UI.getCurrent().getNavigator().navigateTo(TextFileListView.VIEW_NAME + "/?software=" + selected.iterator().next().getId() + "&pv=" + getLvfb().toNavigateString());
			break;
		default:
			LOGGER.error("unKnown menuName {}", btnDesc.getItemId());
		}
	}
	

	@Override
	public void enter(final ViewChangeEvent event) {
		super.enter(event);
	}

	@Override
	public ButtonGroup[] getButtonGroups() {
		return new ButtonGroup[]{ //
		new ButtonGroup(new EditButtonDescription(),new DeleteButtonDescription(), new UnArchiveButtonDescription()),//
		new ButtonGroup(new AddButtonDescription()),//
		new ButtonGroup(new SimpleButtonDescription("importSoftware", null, ButtonEnableType.ALWAYS)),
		new ButtonGroup(new SimpleButtonDescription("softwaretxtfiles", null, ButtonEnableType.ONE))
		};
	}

	@Override
	protected SoftwareGrid createGrid(MessageSource messageSource, Domains domains) {
		VaadinGridWrapper vgw = domains.getGrids().get(Software.class.getSimpleName());
		List<String> sortableContainerPropertyIds = vgw.getSortableColumnNames();
		
		List<String> columnNames = vgw.getColumns().stream().map(VaadinGridColumnWrapper::getName).collect(Collectors.toList());

		RepositoryCommonCustom<Software> rcc = domains.getRepositoryCommonCustom(Software.class.getSimpleName());
		Sort defaultSort = domains.getDefaultSort(Software.class);
		FreeContainer<Software> fc = new FreeContainer<>(rcc, defaultSort, Software.class, vgw.getVg().defaultPerPage(), sortableContainerPropertyIds);
		return new SoftwareGrid(fc, vgw , messageSource, sortableContainerPropertyIds, columnNames, vgw.getVg().messagePrefix());
	}
}
