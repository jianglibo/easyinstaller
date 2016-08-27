package com.jianglibo.vaadin.dashboard.view.box;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

import com.google.common.base.Strings;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.event.view.HistoryBackEvent;
import com.jianglibo.vaadin.dashboard.repositories.BoxRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.twingrid.TwinGridField;
import com.jianglibo.vaadin.dashboard.uicomponent.viewheader.HeaderLayout;
import com.jianglibo.vaadin.dashboard.util.ItemViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
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
	
	private final BoxRepository repository;

	public static final String VIEW_NAME = BoxView.VIEW_NAME + "/edit";

	public static final FontAwesome ICON_VALUE = FontAwesome.FILE_ARCHIVE_O;

	private EventBus eventBus;
	
	private Box bean;
    
    private HeaderLayout header;
    
    private ItemViewFragmentBuilder ifb;
    
    private BoxForm form;
    
    
	@Autowired
	public BoxEditView(BoxRepository repository, MessageSource messageSource,Domains domains,
			ApplicationContext applicationContext) {
		this.messageSource = messageSource;
		this.repository= repository;
		this.eventBus = new EventBus(this.getClass().getName());
		eventBus.register(this);
		setSizeFull();
		addStyleName("transactions");
//		StyleUtil.setOverflowAuto(this, true);
		setMargin(new MarginInfo(true, true, false, true));
		
		header = applicationContext.getBean(HeaderLayout.class).afterInjection(eventBus, false, true, "");
		
		addComponent(header);
		form = applicationContext.getBean(BoxForm.class).afterInjection(eventBus);
		
//		BeanFieldGroup<Box> bfg = form.getFieldGroup();
//		
//		TwinGridField<List<Box>> tl = applicationContext.getBean(TwinGridField.class).afterInjection(Box.class, 10);
//		
//		bfg.bind(tl, "");
//		tl.setCaption("box");
//		form.addComponent(tl);
		
		StyleUtil.setOverflowAuto(form, true);
		
		addComponent(form);
		
		form.setSizeFull();
		
		Component ft = buildFooter();
		
//		addComponent(ft);
//		ft.setHeight(30, Unit.PIXELS);
		form.addComponent(ft);
//		setComponentAlignment(form, Alignment.TOP_LEFT);
		setExpandRatio(form, 1);
//		setExpandRatio(ft, 1);
	}
	
    @SuppressWarnings("serial")
	private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.setMargin(true);
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
			bu = BoxView.VIEW_NAME;
		}
		UI.getCurrent().getNavigator().navigateTo(bu);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		LOGGER.info("parameter string is: {}", event.getParameters());
		ifb = new ItemViewFragmentBuilder(event);
		long bid = ifb.getBeanId();
		if (bid == 0) {
			bean = new Box();
			header.setLabelTxt(MsgUtil.getViewMsg(messageSource, Box.class.getSimpleName() + ".newtitle"));
		} else {
			bean = repository.findOne(bid);
			header.setLabelTxt(bean.getName());
		}
        form.setItemDataSource(bean);
	}
}
