package com.jianglibo.vaadin.dashboard.view.clustersoftware;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.google.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.DashboardUI;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumnWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridWrapper;
import com.jianglibo.vaadin.dashboard.config.ApplicationConfig;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.BoxGroup;
import com.jianglibo.vaadin.dashboard.domain.BoxGroupHistory;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.repositories.BoxGroupHistoryRepository;
import com.jianglibo.vaadin.dashboard.repositories.BoxGroupRepository;
import com.jianglibo.vaadin.dashboard.repositories.BoxRepository;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.security.PersonAuthenticationToken;
import com.jianglibo.vaadin.dashboard.taskrunner.TaskDesc;
import com.jianglibo.vaadin.dashboard.taskrunner.TaskRunner;
import com.jianglibo.vaadin.dashboard.uicomponent.twingrid2.BoxContainerInRc;
import com.jianglibo.vaadin.dashboard.uicomponent.twingrid2.BoxTwinGridFieldFree;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.jianglibo.vaadin.dashboard.util.NotificationUtil;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.jianglibo.vaadin.dashboard.view.boxgroup.BoxGroupListView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;


@SuppressWarnings("serial")
@SpringView(name = ClusterSoftwareView.VIEW_NAME)
public class ClusterSoftwareView extends VerticalLayout implements View {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ClusterSoftwareView.class); 
	
	private final MessageSource messageSource;
	
	public final static String VIEW_NAME = "clustersoftware";
	
	private ListViewFragmentBuilder lvfb;
	
	private Button backBtn;
	
	private Label title;
	
	private final BoxGroupRepository boxGroupRepository;
	
	private final BoxRepository boxRepository;
	
	private final PersonRepository personRepository;
	
	private final FieldFactories fieldFactories;
	
	private final TaskRunner taskRunner;
	
	private final ApplicationConfig applicationConfig;
	
	private BoxGroup boxGroup;
	
	private OneBoxGroupHistoriesDc obghdc;
	
	private BoxContainerInRc bcInRc;
	
	private final Domains domains;
	
	@Autowired
	public ClusterSoftwareView(BoxGroupHistoryRepository boxGroupHistoryRepository, MessageSource messageSource,ApplicationConfig applicationConfig, BoxGroupRepository boxGroupRepository, BoxRepository boxRepository, Domains domains, PersonRepository personRepository, FieldFactories fieldFactories, TaskRunner taskRunner) {
		this.messageSource = messageSource;
		this.boxGroupRepository = boxGroupRepository;
		this.personRepository = personRepository;
		this.boxRepository = boxRepository;
		this.fieldFactories = fieldFactories;
		this.applicationConfig = applicationConfig;
		this.domains = domains;
		this.taskRunner = taskRunner;
		setSizeFull();
		addStyleName("transactions");
		addComponent(createTop());
		
		VerticalLayout vl = new VerticalLayout();
		vl.setSpacing(true);
		vl.setSizeFull();
		Component tb = toolbars();
		vl.addComponent(tb);
		
		VaadinGridWrapper vgw = domains.getGrids().get(BoxGroupHistory.class.getSimpleName());
		List<String> columnNames = vgw.getColumns().stream().map(VaadinGridColumnWrapper::getName).collect(Collectors.toList());
		
		columnNames.add("!boxRuned");
		List<String> sortableContainerPropertyIds = vgw.getSortableColumnNames();
		
		obghdc  = new OneBoxGroupHistoriesDc(boxGroupHistoryRepository, null, domains, 10, sortableContainerPropertyIds);
		
		Component cib = new OneBoxGroupHistoriesGrid(taskRunner, obghdc,vgw, messageSource, sortableContainerPropertyIds, columnNames, vgw.getVg().messagePrefix());
		vl.addComponent(cib);
		vl.setExpandRatio(tb, 1);
		vl.setExpandRatio(cib, 2);
		addComponent(vl);
		setExpandRatio(vl, 1);
	}
	

	private Component toolbars() {
		GridLayout gl = new GridLayout(10, 1);
		gl.setSizeFull();
		Responsive.makeResponsive(gl);
		
		InstallNewSoftwareForm insf =  new InstallNewSoftwareForm(personRepository, messageSource, domains, fieldFactories);
		bcInRc = new BoxContainerInRc(null, domains, 10 , Lists.newArrayList());
		
		VaadinGridWrapper vgw = domains.getGrids().get(Box.class.getSimpleName());
		String mpx = vgw.getVg().messagePrefix();
		BoxTwinGridFieldFree boxesToRun = new BoxTwinGridFieldFree(bcInRc, domains, messageSource, boxRepository, 3 , 3, mpx, mpx);
		
		boxesToRun.setCaption(MsgUtil.getMsgWithSubsReturnKeyOnAbsent(messageSource, "view.clustersoftware.selectboxes"));
		boxesToRun.setSizeFull();
		Button ok = new Button(MsgUtil.getMsgWithSubsReturnKeyOnAbsent(messageSource, "shared.btn.execute"));
		ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
		ok.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				PersonAuthenticationToken ac = VaadinSession.getCurrent().getAttribute(PersonAuthenticationToken.class);
				if (insf.getSelectedSoftware().isPresent() && insf.getSelectedAction().isPresent()) {
					// start to submit tasks;
					LOGGER.info("{}, {}", ac.getClass().getName(), ac.getName());
					DashboardUI dui = (DashboardUI) UI.getCurrent();
					TaskDesc td = new TaskDesc(dui.getUniqueUiID(), ac.getPrincipal(), boxGroup,boxesToRun.getValue(), insf.getSelectedSoftware().get(), insf.getSelectedAction().get());
					taskRunner.submitTasks(td);
					NotificationUtil.tray(messageSource, "tasksent");
				} else {
					NotificationUtil.humanized(messageSource, "actionabsent");
				}
			}
		});
		gl.addComponent(insf, 0, 0, 1, 0);
		gl.addComponent(boxesToRun, 2, 0, 8, 0);
		gl.addComponent(ok, 9, 0);
		gl.setComponentAlignment(ok, Alignment.MIDDLE_CENTER);
		return gl;
	}


	@Override
	public void enter(ViewChangeEvent event) {
		setLvfb(new ListViewFragmentBuilder(event));
		if (getLvfb().getPreviousView().isPresent()) {
			StyleUtil.show(backBtn);
		}
		Long bgid = getLvfb().getLong("boxgroup"); 
		if ( bgid > 0) {
			boxGroup = boxGroupRepository.findOne(bgid);
			if (boxGroup != null) {
				title.setValue(MsgUtil.getMsgWithSubsReturnKeyOnAbsent(messageSource, "view.clustersoftware.title", boxGroup.getDisplayName()));
				obghdc.setBoxGroup(boxGroup);
				bcInRc.setBoxGroup(boxGroup);
			}
		} else {
			UI.getCurrent().getNavigator().navigateTo(BoxGroupListView.VIEW_NAME);
		}
	}
	
	private Component createTop() {
		HorizontalLayout hl = new HorizontalLayout();
		
		hl.addStyleName("viewheader");
		hl.setSpacing(true);
		Responsive.makeResponsive(hl);

		title = new Label("");
		title.setSizeUndefined();
		title.addStyleName(ValoTheme.LABEL_H1);
		title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		hl.addComponent(title);
		
		
		HorizontalLayout tools = new HorizontalLayout();
		tools.addStyleName("toolbar");
		hl.addComponent(tools);

		backBtn = new Button(FontAwesome.MAIL_REPLY);
		StyleUtil.hide(backBtn);

		backBtn.setDescription(MsgUtil.getMsgWithSubsReturnKeyOnAbsent( messageSource ,"shared.btn.return"));
		
		backBtn.addClickListener(event -> {
			this.backward();
		});


		tools.addComponent(backBtn);
		
		Button helpBtn = new Button(FontAwesome.QUESTION);
		tools.addComponent(helpBtn);
		return hl;
	}
	
	public OneBoxGroupHistoriesDc getObghdc() {
		return obghdc;
	}


	public void backward() {
		UI.getCurrent().getNavigator().navigateTo(getLvfb().getPreviousView().get());
	}
	
	public ListViewFragmentBuilder getLvfb() {
		return lvfb;
	}


	public void setLvfb(ListViewFragmentBuilder lvfb) {
		this.lvfb = lvfb;
	}

}
