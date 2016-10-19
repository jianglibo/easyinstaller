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
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
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
	
	private final PersonRepository personRepository;
	

	@Autowired
	public SoftwareEditView(PersonRepository personRepository, SoftwareRepository repository, MessageSource messageSource,Domains domains,FieldFactories fieldFactories,
			ApplicationContext applicationContext) {
		super(messageSource, Software.class, domains, fieldFactories, repository);
		this.personRepository = personRepository;
		delayCreateContent();
	}

    

	@Override
	public void detach() {
		super.detach();
		// A new instance of TransactionsView is created every time it's
		// navigated to so we'll need to clean up references to it on detach.
		// DashboardEventBus.unregister(this);
	}


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
		return new SoftwareForm(personRepository, getMessageSource(), getDomains(), fieldFactories, (SoftwareRepository) repository,handMakeFieldsListener);
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
