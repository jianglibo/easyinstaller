package com.jianglibo.vaadin.dashboard.view.pksource;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumnWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridWrapper;
import com.jianglibo.vaadin.dashboard.config.ApplicationConfig;
import com.jianglibo.vaadin.dashboard.config.CommonMenuItemIds;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.BoxGroupHistory;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.PkSource;
import com.jianglibo.vaadin.dashboard.repositories.PkSourceRepository;
import com.jianglibo.vaadin.dashboard.repositories.RepositoryCommonCustom;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonGroup;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.DeleteButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.RefreshButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.grid.BaseGridView;
import com.jianglibo.vaadin.dashboard.uicomponent.upload.ImmediateUploader;
import com.jianglibo.vaadin.dashboard.uicomponent.upload.PkSourceUploadFinishResult;
import com.jianglibo.vaadin.dashboard.uicomponent.upload.PkSourceUploadReceiver;
import com.jianglibo.vaadin.dashboard.uicomponent.upload.UploadSuccessEventLinstener;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.NotificationUtil;
import com.jianglibo.vaadin.dashboard.view.boxhistory.BoxHistoryListView;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

@SpringView(name = PkSourceListView.VIEW_NAME)
public class PkSourceListView extends BaseGridView<PkSource, PkSourceGrid, FreeContainer<PkSource>>
		implements View, SubscriberExceptionHandler, UploadSuccessEventLinstener<PkSourceUploadFinishResult> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PkSourceListView.class);

	public static final String VIEW_NAME = "pksource";

	public static final FontAwesome ICON_VALUE = FontAwesome.FILE_ARCHIVE_O;

	private EventBus eventBus;
	
	private final ApplicationConfig applicationConfig;

	private PkSourceRepository repository;

	@Autowired
	public PkSourceListView(PkSourceRepository repository, Domains domains, MessageSource messageSource,
			ApplicationContext applicationContext, ApplicationConfig applicationConfig) {
		super(applicationContext, messageSource, domains, PkSource.class, PkSourceGrid.class);
		this.eventBus = new EventBus(this);
		this.repository = repository;
		this.applicationConfig = applicationConfig;
		delayCreateContent();
	}
	
	@Override
	protected MiddleBlock createMiddleBlock() {
		return new PksourceMiddleBlock(super.createMiddleBlock());
	}
	
	@SuppressWarnings("serial")
	protected class PksourceMiddleBlock extends HorizontalLayout implements MiddleBlock {
		
		private MiddleBlock mb;
		
		public PksourceMiddleBlock(MiddleBlock mb) {
			this.mb = mb;
			PkSourceUploadReceiver pkur = new PkSourceUploadReceiver(getMessageSource(), applicationConfig.getUploadDstPath(), repository, PkSourceListView.this);
			ImmediateUploader imd = new ImmediateUploader(getMessageSource(), pkur);
			imd.setMargin(true);
			addComponents((Component) mb, imd);
		}

		@Override
		public void alterState(ListViewFragmentBuilder lvfb) {
			mb.alterState(lvfb);
		}

		@Override
		public void alterState(Set<Object> selected) {
			mb.alterState(selected);
		}
		
	}

//	@Subscribe
//	public void whenFilterStrChange(FilterStrEvent fse) {
//		String nvs = lvfb.setFilterStr(fse.getFilterStr()).toNavigateString();
//		UI.getCurrent().getNavigator().navigateTo(nvs);
//	}
//
//	@Subscribe
//	public void whenSortChanged(TableSortEvent tse) {
//		SortUtil.setUrlObSort(tse.getSort(), domains.getTables().get(PkSource.class.getSimpleName()), lvfb);
//		UI.getCurrent().getNavigator().navigateTo(lvfb.toNavigateString());
//	}

//	@Subscribe
//	public void whenTrashedCheckboxChange(TrashedCheckBoxEvent tce) {
//		String nvs = lvfb.setFilterStr("").setCurrentPage(1)
//				.setBoolean(ListViewFragmentBuilder.TRASHED_PARAM_NAME, tce.isChecked()).toNavigateString();
//		UI.getCurrent().getNavigator().navigateTo(nvs);
//	}

	@Override
	public ButtonGroup[] getButtonGroups() {
		return new ButtonGroup[] { //
				new ButtonGroup(new RefreshButtonDescription()), //
				new ButtonGroup( //
						new DeleteButtonDescription())};
	}

	@Override
	public void handleException(Throwable exception, SubscriberExceptionContext context) {
		exception.printStackTrace();
		LOGGER.info(exception.getMessage());

	}

	@Override
	public void onUploadSuccess(PkSourceUploadFinishResult ufe) {
		NotificationUtil.tray(getMessageSource(), ufe.getPkSource().getDisplayName());
		refreshAfterItemNumberChange();
	}

	@Override
	protected PkSourceGrid createGrid(MessageSource messageSource, Domains domains) {
		VaadinGridWrapper vgw = domains.getGrids().get(PkSource.class.getSimpleName());
		List<String> sortableContainerPropertyIds = vgw.getSortableColumnNames();
		List<String> columnNames = vgw.getColumns().stream().map(VaadinGridColumnWrapper::getName).collect(Collectors.toList());

		RepositoryCommonCustom<PkSource> rcc = domains.getRepositoryCommonCustom(PkSource.class.getSimpleName());
		Sort defaultSort = domains.getDefaultSort(PkSource.class);
		FreeContainer<PkSource> fc = new FreeContainer<>(rcc, defaultSort, PkSource.class, vgw.getVg().defaultPerPage(), sortableContainerPropertyIds);
		return new PkSourceGrid(fc, vgw, messageSource, sortableContainerPropertyIds, columnNames, vgw.getVg().messagePrefix());
	}

	@Override
	protected void onDynButtonClicked(ButtonDescription btnDesc) {
		List<PkSource> selected = getGrid().getSelectedRows().stream().map(o -> (PkSource) o).collect(Collectors.toList());
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
			refreshAfterItemNumberChange();
			break;
		case CommonMenuItemIds.REFRESH:
			refreshAfterItemNumberChange();
			break;
		default:
			LOGGER.error("unKnown menuName {}", btnDesc.getItemId());
		}
	}
}
