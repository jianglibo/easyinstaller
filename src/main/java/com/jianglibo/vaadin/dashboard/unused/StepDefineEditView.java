package com.jianglibo.vaadin.dashboard.unused;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase.HandMakeFieldsListener;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;
import com.jianglibo.vaadin.dashboard.view.BaseEditView;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Field;


//@SpringView(name = StepDefineEditView.VIEW_NAME)
public class StepDefineEditView  extends BaseEditView<StepDefine, FormBase<StepDefine>, JpaRepository<StepDefine,Long>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(StepDefineEditView.class);
	
	public static final String VIEW_NAME = StepDefineListView.VIEW_NAME + "/edit";

	public static final FontAwesome ICON_VALUE = FontAwesome.FILE_ARCHIVE_O;

    
	@Autowired
	public StepDefineEditView(StepDefineRepository repository, MessageSource messageSource,Domains domains,FieldFactories fieldFactories,
			ApplicationContext applicationContext) {
		super(messageSource,StepDefine.class, domains, fieldFactories, repository);
//		this.messageSource = messageSource;
//		this.repository= repository;
//		this.eventBus = new EventBus(this.getClass().getName());
//		eventBus.register(this);
//		setSizeFull();
//		addStyleName("transactions");
//		StyleUtil.setOverflowAuto(this, true);
//		setMargin(true);
//		
////		header = applicationContext.getBean(HeaderLayout.class).afterInjection(eventBus,false, true, "");
//		
//		addComponent(header);
////		form = (StepDefineForm) applicationContext.getBean(StepDefineForm.class).afterInjection(eventBus, this);
//		addComponent(form);
//		Component ft = buildFooter();
//		addComponent(ft);
//		setComponentAlignment(form, Alignment.TOP_LEFT);
//		setExpandRatio(form, 1);
	}
	
//    @SuppressWarnings("serial")
//	private Component buildFooter() {
//        HorizontalLayout footer = new HorizontalLayout();
//        footer.setWidth(100.0f, Unit.PERCENTAGE);
//
//        Button ok = new Button(messageSource.getMessage("shared.btn.save", null, UI.getCurrent().getLocale()));
//        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
//        ok.addClickListener(new ClickListener() {
//            @Override
//            public void buttonClick(ClickEvent event) {
//            	form.save();
//            }
//        });
//        ok.focus();
//        footer.addComponent(ok);
//        footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);
//        return footer;
//    }
    


	
//	@Subscribe
//	public void onBackBtnClicked(HistoryBackEvent hbe) {
//		String bu = ifb.getPreviousView();
//		if (Strings.isNullOrEmpty(bu)) {
//			bu = StepDefineListView.VIEW_NAME;
//		}
//		UI.getCurrent().getNavigator().navigateTo(bu);
//	}

//	@Override
//	public void enter(ViewChangeEvent event) {
//		LOGGER.info("parameter string is: {}", event.getParameters());
//		ifb = new ItemViewFragmentBuilder(event);
//		long bid = ifb.getBeanId();
//		if (bid == 0) {
//			bean = new StepDefine();
//			header.setLabelTxt(MsgUtil.getViewMsg(messageSource, StepDefine.class.getSimpleName() + ".newtitle"));
//		} else {
//			bean = repository.findOne(bid);
//			header.setLabelTxt(bean.getName());
//		}
//        form.setItemDataSource(bean);
//	}

	@Override
	public Field<?> createField(VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected FormBase<StepDefine> createForm(MessageSource messageSource, Domains domains,
			FieldFactories fieldFactories, JpaRepository<StepDefine, Long> repository, HandMakeFieldsListener handMakeFieldsListener) {
		return new StepDefineForm(getMessageSource(), getDomains(), fieldFactories, (StepDefineRepository) repository,handMakeFieldsListener);
	}

	@Override
	protected StepDefine createNewBean() {
		return new StepDefine();
	}

	@Override
	protected String getListViewName() {
		return VIEW_NAME;
	}
}
