package com.jianglibo.vaadin.dashboard.view.boxgroup;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.hash.Hashing;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumnWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridWrapper;
import com.jianglibo.vaadin.dashboard.config.ApplicationConfig;
import com.jianglibo.vaadin.dashboard.config.CommonMenuItemIds;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.PkSource;
import com.jianglibo.vaadin.dashboard.domain.BoxGroup;
import com.jianglibo.vaadin.dashboard.repositories.BoxGroupRepository;
import com.jianglibo.vaadin.dashboard.repositories.PkSourceRepository;
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
import com.jianglibo.vaadin.dashboard.uicomponent.upload.ImmediateUploader;
import com.jianglibo.vaadin.dashboard.uicomponent.upload.TextContentReceiver;
import com.jianglibo.vaadin.dashboard.uicomponent.upload.TextUploadResult;
import com.jianglibo.vaadin.dashboard.uicomponent.upload.SimplifiedUploadResultLinstener;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.jianglibo.vaadin.dashboard.util.NotificationUtil;
import com.jianglibo.vaadin.dashboard.util.StrUtil;
import com.jianglibo.vaadin.dashboard.view.clustersoftware.ClusterSoftwareView;
import com.jianglibo.vaadin.dashboard.view.envfixture.EnvFixtureCreator;
import com.jianglibo.vaadin.dashboard.view.pksource.PkSourceListView;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
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
	
	private final EnvFixtureCreator envFixtureCreator;
	
	private final ApplicationConfig applicationConfig;
	
	private final PkSourceRepository pkSourceRepository;

	
	@Autowired
	public BoxGroupListView(BoxGroupRepository repository,Domains domains, MessageSource messageSource,
			ApplicationContext applicationContext, EnvFixtureCreator envFixtureCreator, ApplicationConfig applicationConfig, PkSourceRepository pkSourceRepository) {
		super(applicationContext, messageSource, domains, BoxGroup.class, BoxGroupGrid.class);
		this.repository = repository;
		this.envFixtureCreator = envFixtureCreator;
		this.applicationConfig = applicationConfig;
		this.pkSourceRepository = pkSourceRepository;
		delayCreateContent();
	}

	public ButtonGroup[] getButtonGroups() {
		return new ButtonGroup[]{ //
		new ButtonGroup(new EditButtonDescription(),new AddButtonDescription()),//
		new ButtonGroup(new DeleteButtonDescription(), new UnArchiveButtonDescription()),
		new ButtonGroup( //
					new SimpleButtonDescription("manageClusterSoftware", null, ButtonEnableType.ONE)),
		new ButtonGroup( //
					new SimpleButtonDescription("createUpdateHostPs1", null, ButtonEnableType.ONE))};
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
			refreshAfterItemContentChange();
			break;
		case "manageClusterSoftware":
			UI.getCurrent().getNavigator().navigateTo(ClusterSoftwareView.VIEW_NAME + "/?boxgroup=" + selected.iterator().next().getId()  + "&pv=" + getLvfb().toNavigateString());
			break;
		case "createUpdateHostPs1":
			try {
				BoxGroup bg = selected.iterator().next();
				String pairs = StrUtil.doubleQuotation(bg.getBoxes().stream().filter(box -> !box.getIp().equals(box.getHostname())).map(box -> box.getIp() + " " + box.getHostname()).collect(Collectors.joining(",")));
				Resource rs = getApplicationContext().getResource("classpath:snippets/HostModifier.ps1");
				List<String> lines =  CharStreams.readLines(new InputStreamReader(rs.getInputStream(), Charsets.UTF_8));
				lines = lines.stream().map(l -> {
					if (l.contains("---insert-items-here---")) {
						return "$items = " + pairs;
					} else {
						return l;
					}
				}).collect(Collectors.toList());
				
				String uuid = UUID.randomUUID().toString();
				Path tmpPath = applicationConfig.getUploadDstPath().resolve(uuid);
				Files.write(Joiner.on(System.lineSeparator()).join(lines), tmpPath.toFile(), StandardCharsets.UTF_8);
				String md5 = com.google.common.io.Files.asByteSource(tmpPath.toFile()).hash(Hashing.md5()).toString();
				PkSource ps = pkSourceRepository.findByFileMd5(md5);
				if (ps == null) {
					File nf = new File(tmpPath.toFile().getParentFile(), md5 + ".ps1");
					if (!nf.exists()) {
						com.google.common.io.Files.move(tmpPath.toFile(), nf);
					}
					ps = new PkSource.PkSourceBuilder(md5, "HostModifier-" + md5 + ".ps1", nf.length(), "ps1", java.nio.file.Files.probeContentType(nf.toPath())).build();
					pkSourceRepository.save(ps);
				} else {
					ps.setUpdatedAt(Date.from(Instant.now()));
					pkSourceRepository.save(ps);
				}
				NotificationUtil.tray(getMessageSource(), "go to '" + MsgUtil.getViewMenuMsg(getMessageSource(), PkSourceListView.VIEW_NAME) + "', to download ps1 file to execute." );
			} catch (IOException e) {
				NotificationUtil.errorRaw(e.getMessage());
			}
			break;
		default:
			LOGGER.error("unKnown menuName {}", btnDesc.getItemId());
		}
	}
	
	@Override
	protected com.jianglibo.vaadin.dashboard.uicomponent.grid.BaseGridView.MiddleBlock createMiddleBlock() {
		return new MyMiddleBlock(super.createMiddleBlock());
	}
	
	@SuppressWarnings("serial")
	protected class MyMiddleBlock extends HorizontalLayout implements MiddleBlock, SimplifiedUploadResultLinstener<String, TextUploadResult> {
		
		private MiddleBlock mb;
		
		public MyMiddleBlock(MiddleBlock mb) {
			this.mb = mb;
			TextContentReceiver tcr = new TextContentReceiver(this);
			ImmediateUploader imd = new ImmediateUploader(getMessageSource(), tcr, MsgUtil.getDynaMenuMsg(getMessageSource(), "import"));
			imd.setMargin(true);
			addComponents((Component)mb, imd);
		}

		@Override
		public void alterState(ListViewFragmentBuilder lvfb) {
			mb.alterState(lvfb);
			
		}

		@Override
		public void alterState(Set<Object> selected) {
			mb.alterState(selected);
		}

		@Override
		public void onUploadResult(TextUploadResult tur) {
			if (tur.isSuccess()) {
				String ext = Files.getFileExtension(tur.getUploadMeta().getFilename()); 
				if ("yaml".equalsIgnoreCase(ext) || "yml".equalsIgnoreCase(ext)) {
					try {
						envFixtureCreator.importBoxGroup(tur.getResult());
						refreshAfterItemNumberChange();
					} catch (IOException e) {
						NotificationUtil.error(getMessageSource(), "wrongFormat", "YAML");
					}
				} else {
					NotificationUtil.error(getMessageSource(), "wrongExt", "yaml,yml");
				}
			} else {
				NotificationUtil.error(getMessageSource(), "uploadFail");
			}
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
