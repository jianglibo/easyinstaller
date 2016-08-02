package com.jianglibo.vaadin.dashboard.view.boxes;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jianglibo.vaadin.dashboard.annotation.FormFields;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEvent.ProfileUpdatedEvent;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEventBus;
import com.jianglibo.vaadin.dashboard.event.view.HistoryBackEvent;
import com.jianglibo.vaadin.dashboard.repositories.BoxRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.viewheader.HeaderLayout;
import com.jianglibo.vaadin.dashboard.util.ComboBoxFieldFactory;
import com.jianglibo.vaadin.dashboard.util.FormFieldsFactory;
import com.jianglibo.vaadin.dashboard.util.ItemViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.jianglibo.vaadin.dashboard.util.FormFieldsFactory.PropertyIdAndField;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;


@SpringView(name = BoxEditView.VIEW_NAME)
public class BoxEditView  extends VerticalLayout implements View {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(BoxEditView.class);
	
	private final MessageSource messageSource;
	
	private final BoxRepository boxRepository;

	public static final String VIEW_NAME = "box/edit";

	public static final FontAwesome ICON_VALUE = FontAwesome.FILE_ARCHIVE_O;

	private ApplicationContext applicationContext;
		
	private EventBus eventBus;
	
    private BeanFieldGroup<Box> fieldGroup;
	
	private final Domains domains;
	
	private Box box;
    
    private HeaderLayout header;
    
    private ItemViewFragmentBuilder ifb;
    
    private final ComboBoxFieldFactory comboBoxFieldFactory;
    
    private final FormFieldsFactory formFieldsFactory;
	
	@Autowired
	public BoxEditView(BoxRepository boxRepository,Domains domains,FormFieldsFactory formFieldsFactory, MessageSource messageSource,ComboBoxFieldFactory comboBoxFieldFactory,
			ApplicationContext applicationContext) {
		this.messageSource = messageSource;
		this.domains = domains;
		this.comboBoxFieldFactory = comboBoxFieldFactory;
		this.formFieldsFactory = formFieldsFactory;
		this.applicationContext = applicationContext;
		this.boxRepository= boxRepository;
		this.eventBus = new EventBus(this.getClass().getName());
		eventBus.register(this);
		setSizeFull();
		addStyleName("transactions");
		// DashboardEventBus.register(this);
		setMargin(true);
		
		header = applicationContext.getBean(HeaderLayout.class).afterInjectionWithBackBtn(eventBus, "");
		
		addComponent(header);
		Component fl = buildForm();
//		fl.setWidth(80f, Unit.PERCENTAGE);
		addComponent(fl);
		addComponent(buildFooter());
//		setComponentAlignment(fl, Alignment.MIDDLE_CENTER);
		setExpandRatio(fl, 1);
	}
	
    @SuppressWarnings("serial")
	private Component buildForm() {

        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        
        details.addShortcutListener(new ShortcutListener("submit", null, KeyCode.ENTER) {
			@Override
			public void handleAction(Object sender, Object target) {
				save();
			}
		});
        
        details.addShortcutListener(new ShortcutListener("submit", null, KeyCode.ESCAPE) {
			@Override
			public void handleAction(Object sender, Object target) {
				eventBus.post(new HistoryBackEvent());
			}
		});
        
        fieldGroup = new BeanFieldGroup<Box>(Box.class);
        
        VaadinTable vt = domains.getTables().get(Box.VAADIN_TABLE_NAME);
        FormFields ffs = domains.getFormFields().get(Box.VAADIN_TABLE_NAME);
        
        List<PropertyIdAndField> fields = formFieldsFactory.buildFields(vt, ffs);
        
        for(PropertyIdAndField paf : fields) {
			fieldGroup.bind(paf.getField(), paf.getPropertyId());
			details.addComponent(paf.getField());
        }
        StyleUtil.setMarginTopTwenty(details);
        return details;
    }	
	private void save() {
        try {
            fieldGroup.commit();
            boxRepository.save(box);
            Notification success = new Notification(messageSource.getMessage("shared.msg.savesuccess", null, UI.getCurrent().getLocale()));
            success.setDelayMsec(2000);
            success.setStyleName("bar success small");
            success.setPosition(Position.BOTTOM_CENTER);
            success.show(Page.getCurrent());
            DashboardEventBus.post(new ProfileUpdatedEvent());
        } catch (CommitException e) {
            Notification.show(messageSource.getMessage("shared.msg.savefailed",null, UI.getCurrent().getLocale()),
                    Type.ERROR_MESSAGE);
        }
	}
	
    @SuppressWarnings("serial")
	private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        Button ok = new Button(messageSource.getMessage("shared.btn.save", null, UI.getCurrent().getLocale()));
        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	save();
            }
        });
        ok.focus();
        footer.addComponent(ok);
        footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);
        return footer;
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
		if (bid == 0) {
			box = new Box();
		} else {
			box = boxRepository.findOne(bid);
			header.setLabelTxt(box.getName());
		}
        fieldGroup.setItemDataSource(box);
	}
}
