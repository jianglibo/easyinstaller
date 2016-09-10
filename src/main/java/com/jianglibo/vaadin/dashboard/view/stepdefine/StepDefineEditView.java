package com.jianglibo.vaadin.dashboard.view.stepdefine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

import com.google.common.base.Strings;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.domain.StepDefine;
import com.jianglibo.vaadin.dashboard.event.view.HistoryBackEvent;
import com.jianglibo.vaadin.dashboard.repositories.StepDefineRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.viewheader.HeaderLayout;
import com.jianglibo.vaadin.dashboard.uifactory.HandMakeFieldsListener;
import com.jianglibo.vaadin.dashboard.util.ItemViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
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


@SpringView(name = StepDefineEditView.VIEW_NAME)
public class StepDefineEditView  extends VerticalLayout implements View, HandMakeFieldsListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(StepDefineEditView.class);
	
	private final MessageSource messageSource;
	
	private final StepDefineRepository repository;

	public static final String VIEW_NAME = StepDefineListView.VIEW_NAME + "/edit";

	public static final FontAwesome ICON_VALUE = FontAwesome.FILE_ARCHIVE_O;

	private EventBus eventBus;
	
	private StepDefine bean;
    
    private HeaderLayout header;
    
    private ItemViewFragmentBuilder ifb;
    
    private StepDefineForm form;
    
	@Autowired
	public StepDefineEditView(StepDefineRepository repository, MessageSource messageSource,
			ApplicationContext applicationContext) {
		this.messageSource = messageSource;
		this.repository= repository;
		this.eventBus = new EventBus(this.getClass().getName());
		eventBus.register(this);
		setSizeFull();
		addStyleName("transactions");
		StyleUtil.setOverflowAuto(this, true);
		setMargin(true);
		
//		header = applicationContext.getBean(HeaderLayout.class).afterInjection(eventBus,false, true, "");
		
		addComponent(header);
		form = (StepDefineForm) applicationContext.getBean(StepDefineForm.class).afterInjection(eventBus, this);
		addComponent(form);
		Component ft = buildFooter();
		addComponent(ft);
		setComponentAlignment(form, Alignment.TOP_LEFT);
		setExpandRatio(form, 1);
	}
	
    @SuppressWarnings("serial")
	private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
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
		String bu = ifb.getPreviousView();
		if (Strings.isNullOrEmpty(bu)) {
			bu = StepDefineListView.VIEW_NAME;
		}
		UI.getCurrent().getNavigator().navigateTo(bu);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		LOGGER.info("parameter string is: {}", event.getParameters());
		ifb = new ItemViewFragmentBuilder(event);
		long bid = ifb.getBeanId();
		if (bid == 0) {
			bean = new StepDefine();
			header.setLabelTxt(MsgUtil.getViewMsg(messageSource, StepDefine.class.getSimpleName() + ".newtitle"));
		} else {
			bean = repository.findOne(bid);
			header.setLabelTxt(bean.getName());
		}
        form.setItemDataSource(bean);
	}

	@Override
	public Field<?> createField(VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw) {
		// TODO Auto-generated method stub
		return null;
	}
}
