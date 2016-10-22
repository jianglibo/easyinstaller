package com.jianglibo.vaadin.dashboard.view.importsoftware;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.google.common.collect.Lists;
import com.jcraft.jsch.ConfigRepository.Config;
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
import com.jianglibo.vaadin.dashboard.repositories.PkSourceRepository;
import com.jianglibo.vaadin.dashboard.security.PersonAuthenticationToken;
import com.jianglibo.vaadin.dashboard.taskrunner.TaskDesc;
import com.jianglibo.vaadin.dashboard.taskrunner.TaskRunner;
import com.jianglibo.vaadin.dashboard.uicomponent.twingrid2.BoxContainerInRc;
import com.jianglibo.vaadin.dashboard.uicomponent.twingrid2.BoxTwinGridFieldFree;
import com.jianglibo.vaadin.dashboard.uicomponent.upload.ImmediateUploader;
import com.jianglibo.vaadin.dashboard.uicomponent.upload.PkSourceUploadFinishResult;
import com.jianglibo.vaadin.dashboard.uicomponent.upload.PkSourceUploadReceiver;
import com.jianglibo.vaadin.dashboard.uicomponent.upload.UploadSuccessEventLinstener;
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
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;


@SuppressWarnings("serial")
@SpringView(name = ImportSoftwareView.VIEW_NAME)
public class ImportSoftwareView extends VerticalLayout implements View, UploadSuccessEventLinstener<PkSourceUploadFinishResult> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ImportSoftwareView.class); 
	
	private final MessageSource messageSource;
	
	public final static String VIEW_NAME = "importsoftware";
	
	private ListViewFragmentBuilder lvfb;
	
	private Button backBtn;
	
	private Label title;
	
	private final BoxGroupRepository boxGroupRepository;
	
	private final BoxRepository boxRepository;
	
	private final PersonRepository personRepository;
	
	private final FieldFactories fieldFactories;
	
	private final TaskRunner taskRunner;
	
	private final ApplicationConfig applicationConfig;
	
	private PkSourceRepository pkSourceRepository;
	
	private BoxGroup boxGroup;
	
	private BoxContainerInRc bcInRc;
	
	private final Domains domains;
	
	@Autowired
	public ImportSoftwareView(BoxGroupHistoryRepository boxGroupHistoryRepository, MessageSource messageSource,ApplicationConfig applicationConfig, BoxGroupRepository boxGroupRepository, BoxRepository boxRepository, Domains domains, PersonRepository personRepository, FieldFactories fieldFactories, TaskRunner taskRunner, PkSourceRepository pkSourceRepository) {
		this.messageSource = messageSource;
		this.boxGroupRepository = boxGroupRepository;
		this.personRepository = personRepository;
		this.boxRepository = boxRepository;
		this.fieldFactories = fieldFactories;
		this.applicationConfig = applicationConfig;
		this.pkSourceRepository = pkSourceRepository;
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
		
		addComponent(vl);
		setExpandRatio(vl, 1);
	}
	

	private Component toolbars() {
		CssLayout vl = new CssLayout();
		vl.setSizeFull();
		Responsive.makeResponsive(vl);
		StyleUtil.setMarginTwenty(vl);
		
		TextField urlField = new TextField();

		Button urlBtn = new Button(MsgUtil.getMsgFallbackToSelf(messageSource, "view.importsoftware.", "urlBtn"), event -> {
			NotificationUtil.tray(messageSource, urlField.getValue());
		});
		
		
		StyleUtil.setMarginLeftTen(urlBtn);
		
		Label uploadLabel = new Label(MsgUtil.getMsgFallbackToSelf(messageSource, "view.importsoftware.", "uploadLabel"));
		
		Label remoteLabel = new Label(MsgUtil.getMsgFallbackToSelf(messageSource, "view.importsoftware.", "remoteLabel"));
		
		PkSourceUploadReceiver receiver = new PkSourceUploadReceiver(messageSource, applicationConfig.getUploadDstPath(),pkSourceRepository,this);
		Component uploader = new ImmediateUploader(messageSource, receiver);
		StyleUtil.setMarginRightTen(uploader);
		
		uploader.setEnabled(false);
		
		Label descriptionLabel = new Label();
		descriptionLabel.setContentMode(ContentMode.HTML);
		descriptionLabel.setValue(MsgUtil.getMsgFallbackToSelf(messageSource, "view.importsoftware.", "descriptionLabel"));
		vl.addComponents(uploadLabel, uploader, remoteLabel, urlField,urlBtn, descriptionLabel);
		return vl;
	}


	@Override
	public void enter(ViewChangeEvent event) {
		setLvfb(new ListViewFragmentBuilder(event));
		if (getLvfb().getPreviousView().isPresent()) {
			StyleUtil.show(backBtn);
		}
	}
	
	private Component createTop() {
		HorizontalLayout hl = new HorizontalLayout();
		
		hl.addStyleName("viewheader");
		hl.setSpacing(true);
		Responsive.makeResponsive(hl);

		title = new Label(MsgUtil.getMsgFallbackToSelf(messageSource, "view.importsoftware.", "title"));
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
		return hl;
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


	@Override
	public void onUploadSuccess(PkSourceUploadFinishResult ufe) {
		
	}

}
