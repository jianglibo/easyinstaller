package com.jianglibo.vaadin.dashboard.view.pksource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.domain.PkSource;
import com.jianglibo.vaadin.dashboard.event.view.HistoryBackEvent;
import com.jianglibo.vaadin.dashboard.repositories.PkSourceRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.viewheader.HeaderLayout;
import com.jianglibo.vaadin.dashboard.uifactory.HandMakeFieldsListener;
import com.jianglibo.vaadin.dashboard.util.ItemViewFragmentBuilder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;


@SpringView(name = PkSourceEditView.VIEW_NAME)
public class PkSourceEditView  extends VerticalLayout implements View, HandMakeFieldsListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PkSourceEditView.class);
	
	private final MessageSource messageSource;
	
	private final PkSourceRepository pkSourceRepository;

	public static final String VIEW_NAME = PkSourceView.VIEW_NAME + "/edit";

	public static final FontAwesome ICON_VALUE = FontAwesome.FILE_ARCHIVE_O;

	private EventBus eventBus;
	
	private	PkSource pkSource;
    
    private HeaderLayout header;
    
    private ItemViewFragmentBuilder ifb;
    
    private PkSourceForm form;
	
	@Autowired
	public PkSourceEditView(PkSourceRepository pkSourceRepository, MessageSource messageSource,
			ApplicationContext applicationContext) {
		this.messageSource = messageSource;
		this.pkSourceRepository = pkSourceRepository;
		this.eventBus = new EventBus(this.getClass().getName());
		eventBus.register(this);
		setSizeFull();
		addStyleName("transactions");
		// DashboardEventBus.register(this);
		setMargin(true);
		
//		header = applicationContext.getBean(HeaderLayout.class).afterInjection(eventBus,false, true, "");
		
		addComponent(header);
		form = (PkSourceForm) applicationContext.getBean(PkSourceForm.class).afterInjection(eventBus, this);
		addComponent(form);
		addComponent(buildFooter());
		setExpandRatio(form, 1);
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
            	form.save();
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
			pkSource = new PkSource();
		} else {
			pkSource = pkSourceRepository.findOne(bid);
			header.setLabelTxt(pkSource.getPkname());
		}
        form.setItemDataSource(pkSource);
	}

	@Override
	public Field<?> createField(VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw) {
		// TODO Auto-generated method stub
		return null;
	}
}
