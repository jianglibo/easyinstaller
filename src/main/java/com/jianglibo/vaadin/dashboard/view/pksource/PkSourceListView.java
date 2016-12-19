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

import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumnWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridWrapper;
import com.jianglibo.vaadin.dashboard.config.ApplicationConfig;
import com.jianglibo.vaadin.dashboard.config.CommonMenuItemIds;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
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
import com.jianglibo.vaadin.dashboard.uicomponent.upload.PkSourceUploadResult;
import com.jianglibo.vaadin.dashboard.uicomponent.upload.PkSourceUploadReceiver;
import com.jianglibo.vaadin.dashboard.uicomponent.upload.SimplifiedUploadResultLinstener;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.NotificationUtil;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

@SpringView(name = PkSourceListView.VIEW_NAME)
public class PkSourceListView extends BaseGridView<PkSource, PkSourceGrid, FreeContainer<PkSource>>
		implements View, SubscriberExceptionHandler, SimplifiedUploadResultLinstener<PkSource, PkSourceUploadResult> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PkSourceListView.class);

	public static final String VIEW_NAME = "pksource";

	public static final FontAwesome ICON_VALUE = FontAwesome.FILE_ARCHIVE_O;

	private final ApplicationConfig applicationConfig;

	private PkSourceRepository repository;

	@Autowired
	public PkSourceListView(PkSourceRepository repository, Domains domains, MessageSource messageSource,
			ApplicationContext applicationContext, ApplicationConfig applicationConfig) {
		super(applicationContext, messageSource, domains, PkSource.class, PkSourceGrid.class);
		this.repository = repository;
		this.applicationConfig = applicationConfig;
		delayCreateContent();
	}
	
	@Override
	protected MiddleBlock createMiddleBlock() {
		return new PksourceMiddleBlock(super.createMiddleBlock());
	}
	
	private String getUrlHtml(PkSource pks) {
		return String.format("<a href=\"/download/%s.%s\" target=\"_blank\">%s</a>", pks.getFileMd5(), pks.getExtNoDot(), pks.getPkname());
	}
	
	@SuppressWarnings("serial")
	protected class PksourceMiddleBlock extends HorizontalLayout implements MiddleBlock {
		
		private MiddleBlock mb;
		
		private Label downloadLabel;
		
		private HorizontalLayout hl;
		
		public PksourceMiddleBlock(MiddleBlock mb) {
			this.mb = mb;
			PkSourceUploadReceiver pkur = new PkSourceUploadReceiver(getMessageSource(), applicationConfig.getUploadDstPath(), repository, PkSourceListView.this);
			ImmediateUploader imd = new ImmediateUploader(getMessageSource(), pkur, "");
			imd.setMargin(true);
			hl = new HorizontalLayout();
			hl.setMargin(true);
			downloadLabel = new Label();
			downloadLabel.setCaptionAsHtml(true);
			hl.addComponent(downloadLabel);
			addComponents((Component) mb, imd, hl);
			StyleUtil.hide(hl);
		}

		@Override
		public void alterState(ListViewFragmentBuilder lvfb) {
			mb.alterState(lvfb);
		}

		@Override
		public void alterState(Set<Object> selected) {
			if (selected.size() == 1) {
				downloadLabel.setCaption(getUrlHtml((PkSource) selected.iterator().next()));	
				StyleUtil.show(hl);
			} else {
				StyleUtil.hide(hl);
			}
			mb.alterState(selected);
		}
		
	}

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
	public void onUploadResult(PkSourceUploadResult ufe) {
		NotificationUtil.tray(getMessageSource(), ufe.getResult().getDisplayName());
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
