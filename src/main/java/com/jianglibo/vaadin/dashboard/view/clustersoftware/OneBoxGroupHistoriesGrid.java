package com.jianglibo.vaadin.dashboard.view.clustersoftware;

import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.context.MessageSource;

import com.google.common.base.Charsets;
import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.jianglibo.vaadin.dashboard.DashboardUI;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridWrapper;
import com.jianglibo.vaadin.dashboard.config.ApplicationConfig;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.BoxGroupHistory;
import com.jianglibo.vaadin.dashboard.security.PersonAuthenticationToken;
import com.jianglibo.vaadin.dashboard.taskrunner.TaskDesc;
import com.jianglibo.vaadin.dashboard.taskrunner.TaskRunner;
import com.jianglibo.vaadin.dashboard.uicomponent.grid.BaseGrid;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.jianglibo.vaadin.dashboard.util.NotificationUtil;
import com.jianglibo.vaadin.dashboard.view.boxhistory.BoxHistoryListView;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class OneBoxGroupHistoriesGrid extends BaseGrid<BoxGroupHistory, FreeContainer<BoxGroupHistory>>{
	

	private final TaskRunner taskRunner;
	
	private final ApplicationConfig applicationConfig;
	
	public OneBoxGroupHistoriesGrid(ApplicationConfig applicationConfig, TaskRunner taskRunner, OneBoxGroupHistoriesDc dContainer,VaadinGridWrapper vgw, MessageSource messageSource, List<String> sortableContainerPropertyIds, List<String> columnNames, String messagePrefix) {
		super(vgw, dContainer, messageSource, sortableContainerPropertyIds, columnNames, messagePrefix);
		this.taskRunner = taskRunner;
		this.applicationConfig = applicationConfig;
		delayCreateContent();
	}

	@Override
	protected void addGeneratedProperty(GeneratedPropertyContainer gpcontainer, String name) {
	}
	
	private Button redoBt;
	
	private Button dspBt;

	@Override
	protected void setupGrid() {
		HeaderRow hr = addHeaderRowAt(0);
		HeaderCell namesCell = hr.join(
				hr.getCell("software"),
				hr.getCell("boxGroup"));
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		
		redoBt = new Button(MsgUtil.getMsgWithSubsReturnKeyOnAbsent(getMessageSource(), "view.clustersoftware.button.redo"));
		hl.addComponent(redoBt);
		redoBt.setEnabled(false);
		
		redoBt.addClickListener(event -> {
			// start to submit tasks;
			PersonAuthenticationToken ac = VaadinSession.getCurrent().getAttribute(PersonAuthenticationToken.class);
			DashboardUI dui = (DashboardUI) UI.getCurrent();
			
			BoxGroupHistory bgh = (BoxGroupHistory) getSelectedRow();
			
			if (bgh != null) {
				TaskDesc td = new TaskDesc(dui.getUniqueUiID(), ac.getPrincipal(), bgh);
				for(Box box : td.getBoxes()) {
					if (Strings.isNullOrEmpty(box.getKeyFilePath())) {
						NotificationUtil.error(getMessageSource(), "noKeyFilePath", box.getHostname());
						return;
					}
				}
				
				for(Box box : td.getBoxes()) {
					Path kp = applicationConfig.getSshKeyFolderPath().resolve(box.getKeyFilePath()); 
					if (!Files.exists(kp)) {
						NotificationUtil.error(getMessageSource(), "keyFilePathNotExists", box.getHostname(), kp.toAbsolutePath().toString());
						return;
					}
				}
				taskRunner.submitTasks(td);
			}
		});
		
		dspBt = new Button(MsgUtil.getMsgWithSubsReturnKeyOnAbsent(getMessageSource(), "view.clustersoftware.button.boxhistories"));
		hl.addComponent(dspBt);
		dspBt.setEnabled(false);
		
		dspBt.addClickListener(event -> {
			BoxGroupHistory bgh = (BoxGroupHistory) getSelectedRow();
			if (bgh != null) {
				String fg = UI.getCurrent().getPage().getUriFragment();
				if (fg.startsWith("!")) {
					fg = fg.substring(1);
				}
				
				try {
					fg = URLEncoder.encode(fg, Charsets.UTF_8.name());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				UI.getCurrent().getNavigator().navigateTo(BoxHistoryListView.VIEW_NAME + "/?boxGroupHistoryId=" + bgh.getId() + "&pv=" + fg);
			}
		});
		
		namesCell.setComponent(hl);	
		
		addSelectionListener(event -> {
			if (event.getSelected().size() > 0) {
				redoBt.setEnabled(true);
				dspBt.setEnabled(true);
			} else {
				redoBt.setEnabled(false);
				dspBt.setEnabled(false);
			}
		});
	}
}
