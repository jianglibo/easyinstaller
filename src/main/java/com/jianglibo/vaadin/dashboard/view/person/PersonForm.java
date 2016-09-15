package com.jianglibo.vaadin.dashboard.view.person;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Person;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;

@SuppressWarnings("serial")
public class PersonForm extends FormBase<Person> {
	
	private final PersonRepository repository;
	
	public PersonForm(MessageSource messageSource, Domains domains, FieldFactories fieldFactories, PersonRepository repository, HandMakeFieldsListener handMakeFieldsListener) {
		super(Person.class, messageSource, domains, fieldFactories, handMakeFieldsListener);
		this.repository = repository;
	}

	@Override
	public boolean saveToRepo() {
		Person in = getWrappedBean();
		repository.save(in);
		return true;
	}
}
