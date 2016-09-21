package com.jianglibo.vaadin.dashboard.unused;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;

@SuppressWarnings("serial")
public class StepRunForm extends FormBase<StepRun> {
	
	private final StepRunRepository repository;
	
	public StepRunForm(PersonRepository personRepository, MessageSource messageSource, Domains domains, FieldFactories fieldFactories, StepRunRepository repository, HandMakeFieldsListener handMakeFieldsListener) {
		super(StepRun.class,personRepository, messageSource, domains, fieldFactories, handMakeFieldsListener);
		this.repository = repository;
		delayCreateContent();
	}

	@Override
	public boolean saveToRepo() {
        repository.save(getWrappedBean());
		return true;
	}
}
