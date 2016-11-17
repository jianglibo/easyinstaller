package com.jianglibo.vaadin.dashboard.view.kvv;

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
import com.jianglibo.vaadin.dashboard.domain.Kkv;
import com.jianglibo.vaadin.dashboard.repositories.KkvRepository;
import com.jianglibo.vaadin.dashboard.repositories.RepositoryCommonCustom;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.grid.BaseGridView;
import com.jianglibo.vaadin.dashboard.util.NotificationUtil;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.UI;

@SpringView(name = KkvListView.VIEW_NAME)
public class KkvListView extends BaseGridView<Kkv, KkvGrid, FreeContainer<Kkv>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(KkvListView.class);

	public static final String VIEW_NAME = "kkv";

	public static final FontAwesome ICON_VALUE = FontAwesome.BOOK;
	
	private final KkvRepository repository;
	
	@Autowired
	public KkvListView(KkvRepository repository,Domains domains, MessageSource messageSource,
			ApplicationContext applicationContext) {
		super(applicationContext, messageSource, domains, Kkv.class, KkvGrid.class);
		this.repository = repository;
		delayCreateContent();
	}
	
	@Override
	public void onDynButtonClicked(ButtonDescription btnDesc) {
		List<Kkv> selected = getGrid().getSelectedRows().stream().map(o -> (Kkv)o).collect(Collectors.toList());
		switch (btnDesc.getItemId()) {
		case CommonMenuItemIds.DELETE:
			selected.forEach(b -> {
				if (b.isArchived()) {
					repository.delete(b.getId());
					NotificationUtil.tray(getMessageSource(), "deletedone", b.getDisplayName());
				} else {
					b.setArchived(true);
					NotificationUtil.tray(getMessageSource(), "archivedone", b.getDisplayName());
					repository.save(b);
				}
			});
			getGrid().getdContainer().setDirty(true);
			getGrid().deselectAll();
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
			getGrid().getdContainer().setDirty(true);
			getGrid().deselectAll();
			getGrid().getdContainer().notifyItemSetChanged();
			break;
		default:
			LOGGER.error("unKnown menuName {}", btnDesc.getItemId());
		}
	}

	@Override
	protected KkvGrid createGrid(MessageSource messageSource, Domains domains) {
		VaadinGridWrapper vgw = domains.getGrids().get(Kkv.class.getSimpleName());
		List<String> sortableContainerPropertyIds = vgw.getSortableColumnNames();
		List<String> columnNames = vgw.getColumns().stream().map(VaadinGridColumnWrapper::getName).collect(Collectors.toList());
		
		RepositoryCommonCustom<Kkv> rcc = domains.getRepositoryCommonCustom(Kkv.class.getSimpleName());
		Sort defaultSort = domains.getDefaultSort(Kkv.class);

		FreeContainer<Kkv> fc = new FreeContainer<>(rcc, defaultSort, Kkv.class, vgw.getVg().defaultPerPage(), sortableContainerPropertyIds);
		
		return new KkvGrid(fc, vgw, messageSource, sortableContainerPropertyIds, columnNames, vgw.getVg().messagePrefix());
	}
}
