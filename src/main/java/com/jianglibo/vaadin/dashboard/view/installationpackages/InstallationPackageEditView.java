package com.jianglibo.vaadin.dashboard.view.installationpackages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jianglibo.vaadin.dashboard.domain.PkSource;
import com.jianglibo.vaadin.dashboard.event.view.HistoryBackEvent;
import com.jianglibo.vaadin.dashboard.repositories.PkSourceRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.viewheader.HeaderLayout;
import com.jianglibo.vaadin.dashboard.util.ItemViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;


@SpringView(name = InstallationPackageEditView.VIEW_NAME)
public class InstallationPackageEditView  extends VerticalLayout implements View {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(InstallationPackageEditView.class);
	
	private final MessageSource messageSource;
	
	private final PkSourceRepository pkSourceRepository;

	public static final String VIEW_NAME = "installationPackage/edit";

	public static final FontAwesome ICON_VALUE = FontAwesome.FILE_ARCHIVE_O;

	private ApplicationContext applicationContext;
		
	private EventBus eventBus;
	
    private BeanFieldGroup<PkSource> fieldGroup;
	
	@PropertyId("fileMd5")
	private TextField fileMd5Field;

	@PropertyId("pkname")
    private TextField pknameField;
    
	@PropertyId("originFrom")
    private TextField originFromField;
    
	@PropertyId("mimeType")
    private ComboBox mimeTypeField;
	
    private Component buildProfileTab() {

        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);

        fileMd5Field = new TextField("First Name");
        details.addComponent(fileMd5Field);
        
        pknameField = new TextField("Last Name");
        details.addComponent(pknameField);

        originFromField = new TextField("Last Name");
        details.addComponent(originFromField);

        mimeTypeField = new ComboBox("Title");
        mimeTypeField.setInputPrompt("Please specify");
        mimeTypeField.addItem("Mr.");
        mimeTypeField.addItem("Mrs.");
        mimeTypeField.addItem("Ms.");
        mimeTypeField.setNewItemsAllowed(true);
        details.addComponent(mimeTypeField);
        
        fieldGroup = new BeanFieldGroup<PkSource>(PkSource.class);
        fieldGroup.bindMemberFields(this);

//        sexField = new OptionGroup("Sex");
//        sexField.addItem(Boolean.FALSE);
//        sexField.setItemCaption(Boolean.FALSE, "Female");
//        sexField.addItem(Boolean.TRUE);
//        sexField.setItemCaption(Boolean.TRUE, "Male");
//        sexField.addStyleName("horizontal");
//        details.addComponent(sexField);
//
//        Label section = new Label("Contact Info");
//        section.addStyleName(ValoTheme.LABEL_H4);
//        section.addStyleName(ValoTheme.LABEL_COLORED);
//        details.addComponent(section);
//
//        emailField = new TextField("Email");
//        emailField.setWidth("100%");
//        emailField.setRequired(true);
//        emailField.setNullRepresentation("");
//        details.addComponent(emailField);
//
//        locationField = new TextField("Location");
//        locationField.setWidth("100%");
//        locationField.setNullRepresentation("");
//        locationField.setComponentError(new UserError(
//                "This address doesn't exist"));
//        details.addComponent(locationField);
//
//        phoneField = new TextField("Phone");
//        phoneField.setWidth("100%");
//        phoneField.setNullRepresentation("");
//        details.addComponent(phoneField);
//
//        newsletterField = new OptionalSelect<Integer>();
//        newsletterField.addOption(0, "Daily");
//        newsletterField.addOption(1, "Weekly");
//        newsletterField.addOption(2, "Monthly");
//        details.addComponent(newsletterField);
//
//        section = new Label("Additional Info");
//        section.addStyleName(ValoTheme.LABEL_H4);
//        section.addStyleName(ValoTheme.LABEL_COLORED);
//        details.addComponent(section);
//
//        websiteField = new TextField("Website");
//        websiteField.setInputPrompt("http://");
//        websiteField.setWidth("100%");
//        websiteField.setNullRepresentation("");
//        details.addComponent(websiteField);
//
//        bioField = new TextArea("Bio");
//        bioField.setWidth("100%");
//        bioField.setRows(4);
//        bioField.setNullRepresentation("");
//        details.addComponent(bioField);
        StyleUtil.setMarginTopTwenty(details);
        return details;
    }
    
    private HeaderLayout header;
    
    private ItemViewFragmentBuilder ifb;
	
	@Autowired
	public InstallationPackageEditView(PkSourceRepository pkSourceRepository, MessageSource messageSource,
			ApplicationContext applicationContext) {
		this.messageSource = messageSource;
		this.applicationContext = applicationContext;
		this.pkSourceRepository = pkSourceRepository;
		this.eventBus = new EventBus(this.getClass().getName());
		eventBus.register(this);
		setSizeFull();
		addStyleName("transactions");
		// DashboardEventBus.register(this);
		setMargin(true);
		
		header = applicationContext.getBean(HeaderLayout.class).afterInjectionWithBackBtn(eventBus, "");
		
		addComponent(header);
		Component fl = buildProfileTab();
		fl.setWidth(80f, Unit.PERCENTAGE);
		addComponent(fl);
		
//		setComponentAlignment(fl, Alignment.MIDDLE_CENTER);
		setExpandRatio(fl, 1);
	}

	@Override
	public void detach() {
		super.detach();
		// A new instance of TransactionsView is created every time it's
		// navigated to so we'll need to clean up references to it on detach.
		// DashboardEventBus.unregister(this);
	}
	
	@Subscribe
	public void onBackBtnClicked(HistoryBackEvent hbe) {
		UI.getCurrent().getNavigator().navigateTo(ifb.getPreviousView());
	}
	

	@Override
	public void enter(ViewChangeEvent event) {
		LOGGER.info("parameter string is: {}", event.getParameters());
		ifb = new ItemViewFragmentBuilder(event);
		long bid = ifb.getBeanId();
		PkSource pkSource;
		if (bid == 0) {
			pkSource = new PkSource();
		} else {
			pkSource = pkSourceRepository.findOne(bid);
			header.setLabelTxt(pkSource.getPkname());
		}
        fieldGroup.setItemDataSource(pkSource);
	}
}
