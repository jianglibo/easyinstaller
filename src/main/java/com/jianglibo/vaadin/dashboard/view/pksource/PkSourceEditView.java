package com.jianglibo.vaadin.dashboard.view.pksource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.PkSource;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.repositories.PkSourceRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase.HandMakeFieldsListener;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;
import com.jianglibo.vaadin.dashboard.view.BaseEditView;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Field;


@SpringView(name = PkSourceEditView.VIEW_NAME)
public class PkSourceEditView  extends BaseEditView<PkSource, FormBase<PkSource>, JpaRepository<PkSource,Long>>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PkSourceEditView.class);
	
	

	public static final String VIEW_NAME = PkSourceListView.VIEW_NAME + "/edit";

	public static final FontAwesome ICON_VALUE = FontAwesome.FILE_ARCHIVE_O;
	
	private final PersonRepository personRepository;
    
	@Autowired
	public PkSourceEditView(PersonRepository personRepository, PkSourceRepository repository, MessageSource messageSource,Domains domains,FieldFactories fieldFactories,
			ApplicationContext applicationContext) {
		super(messageSource,PkSource.class, domains, fieldFactories, repository);
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
	public void enter(ViewChangeEvent event) {
	}

	@Override
	public Field<?> createField(VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw) {
		return null;
	}

	@Override
	protected FormBase<PkSource> createForm(MessageSource messageSource, Domains domains, FieldFactories fieldFactories,
			JpaRepository<PkSource, Long> repository, HandMakeFieldsListener handMakeFieldsListener) {
		return new PkSourceForm(personRepository, getMessageSource(), getDomains(), getFieldFactories(), (PkSourceRepository) repository, handMakeFieldsListener);
	}

	@Override
	protected PkSource createNewBean() {
		return null;
	}

	@Override
	protected String getListViewName() {
		return VIEW_NAME;
	}
}
