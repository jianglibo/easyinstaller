package com.jianglibo.vaadin.dashboard.view.person;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Person;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase.HandMakeFieldsListener;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;
import com.jianglibo.vaadin.dashboard.view.BaseEditView;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Field;


@SpringView(name = PersonEditView.VIEW_NAME)
public class PersonEditView  extends BaseEditView<Person, FormBase<Person>, JpaRepository<Person,Long>>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PersonEditView.class);

	public static final String VIEW_NAME = PersonListView.VIEW_NAME + "/edit";

	@Autowired
	public PersonEditView(PersonRepository repository,MessageSource messageSource,Domains domains,FieldFactories fieldFactories,
			ApplicationContext applicationContext) {
		super(messageSource,Person.class, domains, fieldFactories, repository);
		delayCreateContent();
	}


	@Override
	public Field<?> createField(VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw) {
		return null;
	}

	@Override
	protected FormBase<Person> createForm(MessageSource messageSource, Domains domains, FieldFactories fieldFactories,
			JpaRepository<Person, Long> repository,HandMakeFieldsListener handMakeFieldsListener) {
		return new PersonForm(getMessageSource(), getDomains(), fieldFactories, (PersonRepository) repository, handMakeFieldsListener);
	}

	@Override
	protected Person createNewBean() {
		return new Person();
	}

	@Override
	protected String getListViewName() {
		return VIEW_NAME;
	}
}
