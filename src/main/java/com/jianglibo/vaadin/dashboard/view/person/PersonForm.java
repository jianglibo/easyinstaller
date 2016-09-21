package com.jianglibo.vaadin.dashboard.view.person;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Person;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;

@SuppressWarnings("serial")
public class PersonForm extends FormBase<Person> {
	
	
	public PersonForm(MessageSource messageSource, Domains domains, FieldFactories fieldFactories, PersonRepository repository, HandMakeFieldsListener handMakeFieldsListener) {
		super(Person.class, repository, messageSource, domains, fieldFactories, handMakeFieldsListener);
		delayCreateContent();
	}

	@Override
	public boolean saveToRepo() {
		Person in = getWrappedBean();
		getPersonRepository().save(in);
		return true;
	}
}
