package com.jianglibo.vaadin.dashboard.view.pksource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.PkSource;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.repositories.PkSourceRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;

@SuppressWarnings("serial")
public class PkSourceForm extends FormBase<PkSource>{

	private final PkSourceRepository repository;
	
	@Autowired
	public PkSourceForm(PersonRepository personRepository, MessageSource messageSource, Domains domains, FieldFactories fieldFactories, PkSourceRepository repository, HandMakeFieldsListener handMakeFieldsListener) {
		super(PkSource.class,personRepository, messageSource, domains, fieldFactories, handMakeFieldsListener);
		this.repository = repository;
		delayCreateContent();
	}
	
	@Override
	public boolean saveToRepo() {
		repository.save(getWrappedBean());
		return true;
	}
}
