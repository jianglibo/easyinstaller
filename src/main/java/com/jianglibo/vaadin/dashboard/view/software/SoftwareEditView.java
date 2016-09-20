package com.jianglibo.vaadin.dashboard.view.software;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.repositories.SoftwareRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase.HandMakeFieldsListener;
import com.jianglibo.vaadin.dashboard.uicomponent.gridfield.FilesToUploadScalarGridField;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;
import com.jianglibo.vaadin.dashboard.view.BaseEditView;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Field;


@SpringView(name = SoftwareEditView.VIEW_NAME)
public class SoftwareEditView  extends BaseEditView<Software, FormBase<Software>, JpaRepository<Software,Long>>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SoftwareEditView.class);
	
	

	public static final String VIEW_NAME = SoftwareListView.VIEW_NAME + "/edit";

	public static final FontAwesome ICON_VALUE = FontAwesome.FILE_ARCHIVE_O;
	

	@Autowired
	public SoftwareEditView(SoftwareRepository repository, MessageSource messageSource,Domains domains,FieldFactories fieldFactories,
			ApplicationContext applicationContext) {
		super(messageSource, Software.class, domains, fieldFactories, repository);
//		this.messageSource = messageSource;
//		this.repository= repository;
//		this.eventBus = new EventBus(this.getClass().getName());
//		this.applicationContext = applicationContext;
//		eventBus.register(this);
//		setSizeFull();
//		addStyleName("transactions");
//		StyleUtil.setOverflowAuto(this, true);
//		setMargin(true);
//		
////		header = applicationContext.getBean(HeaderLayout.class).afterInjection(eventBus,false, true, "");
//		
//		addComponent(header);
//		
////		form = (SoftwareForm) applicationContext.getBean(SoftwareForm.class).afterInjection(eventBus, this);
//		
//		addComponent(form);
//		Component ft = buildFooter();
//		addComponent(ft);
//		setComponentAlignment(form, Alignment.TOP_LEFT);
//		setExpandRatio(form, 1);
	}
	
//	@Override
//	public Field<?> createField(VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw) {
//		return applicationContext.getBean(OrderedStepDefineTwinGrid.class).afterInjection(vtw, vffw);
//	}
//
//	
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
    

	@Override
	public void detach() {
		super.detach();
		// A new instance of TransactionsView is created every time it's
		// navigated to so we'll need to clean up references to it on detach.
		// DashboardEventBus.unregister(this);
	}
	
//	@Subscribe
//	public void onBackBtnClicked(HistoryBackEvent hbe) {
//		String bu = ifb.getPreviousView();
//		if (Strings.isNullOrEmpty(bu)) {
//			bu = SoftwareListView.VIEW_NAME;
//		}
//		UI.getCurrent().getNavigator().navigateTo(bu);
//	}

//	@Override
//	public void enter(ViewChangeEvent event) {
//		LOGGER.info("parameter string is: {}", event.getParameters());
//		ifb = new ItemViewFragmentBuilder(event);
//		long bid = ifb.getBeanId();
//		if (bid == 0) {
//			bean = new Software();
//			header.setLabelTxt(MsgUtil.getViewMsg(messageSource, Software.class.getSimpleName() + ".newtitle"));
//		} else {
//			bean = repository.findOne(bid);
//			header.setLabelTxt(bean.getName());
//		}
//        form.setItemDataSource(bean);
//	}

	@Override
	protected Field<?> createField(VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw) {
		switch (vffw.getName()) {
		case "filesToUpload":
			return new FilesToUploadScalarGridField(getDomains(), String.class, getMessageSource(), vtw, vffw);
		default:
			break;
		}
		return null;
	}

	@Override
	protected FormBase<Software> createForm(MessageSource messageSource, Domains domains, FieldFactories fieldFactories,
			JpaRepository<Software, Long> repository, HandMakeFieldsListener handMakeFieldsListener) {
		return new SoftwareForm(getMessageSource(), getDomains(), fieldFactories, (SoftwareRepository) repository,handMakeFieldsListener);
	}

	@Override
	protected Software createNewBean() {
		return new Software();
	}

	@Override
	protected String getListViewName() {
		return VIEW_NAME;
	}

}
