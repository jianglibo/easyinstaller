package com.jianglibo.vaadin.dashboard.view.textfile;

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
import com.jianglibo.vaadin.dashboard.domain.TextFile;
import com.jianglibo.vaadin.dashboard.repositories.BoxRepository;
import com.jianglibo.vaadin.dashboard.repositories.RepositoryCommonCustom;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.grid.BaseGridView;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.UI;

@SpringView(name = TextFileListView.VIEW_NAME)
public class TextFileListView extends BaseGridView<TextFile, TextFileGrid, FreeContainer<TextFile>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(TextFileListView.class);

	public static final String VIEW_NAME = "textfile";

	public static final FontAwesome ICON_VALUE = FontAwesome.BOOK;

	
	@Autowired
	public TextFileListView(BoxRepository repository,Domains domains, MessageSource messageSource,
			ApplicationContext applicationContext) {
		super(applicationContext, messageSource, domains, TextFile.class, TextFileGrid.class);
		delayCreateContent();
	}

	@Override
	public void onDynButtonClicked(ButtonDescription btnDesc) {
		List<TextFile> selected = getGrid().getSelectedRows().stream().map(o -> (TextFile)o).collect(Collectors.toList());
		switch (btnDesc.getItemId()) {
		case CommonMenuItemIds.DELETE:
			selected.forEach(b -> {
				if (b.isArchived()) {
//					getGrid().getContainerDataSource()
//					getRepository().delete(b);
				} else {
					b.setArchived(true);
//					getRepository().save(b);
				}
			});
//			((BoxContainer)getTable().getContainerDataSource()).refresh();
			break;
		case CommonMenuItemIds.REFRESH:
//			((BoxContainer)getTable().getContainerDataSource()).refresh();
			break;
		case CommonMenuItemIds.EDIT:
			UI.getCurrent().getNavigator().navigateTo(VIEW_NAME + "/edit/" + selected.iterator().next().getId() + "?pv=" + getLvfb().toNavigateString());
			break;
		case CommonMenuItemIds.ADD:
			UI.getCurrent().getNavigator().navigateTo(VIEW_NAME + "/edit/?pv=" + getLvfb().toNavigateString());
			break;
		default:
			LOGGER.error("unKnown menuName {}", btnDesc.getItemId());
		}
	}

	@Override
	protected TextFileGrid createGrid(MessageSource messageSource, Domains domains) {
		VaadinGridWrapper vgw = domains.getGrids().get(TextFile.class.getSimpleName());
		List<String> sortableContainerPropertyIds = vgw.getSortableColumnNames();
		List<String> columnNames = vgw.getColumns().stream().map(VaadinGridColumnWrapper::getName).collect(Collectors.toList());
		columnNames.add("!edit");
		
		RepositoryCommonCustom<TextFile> rcc = domains.getRepositoryCommonCustom(TextFile.class.getSimpleName());
		Sort defaultSort = domains.getDefaultSort(TextFile.class);

		FreeContainer<TextFile> fc = new FreeContainer<>(rcc, defaultSort, TextFile.class, vgw.getVg().defaultPerPage(), sortableContainerPropertyIds);
		
		return new TextFileGrid(fc, vgw, messageSource, sortableContainerPropertyIds, columnNames, vgw.getVg().messagePrefix());
	}
}
