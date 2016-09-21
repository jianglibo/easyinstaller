package com.jianglibo.vaadin.dashboard.unused;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase.HandMakeFieldsListener;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;

@SuppressWarnings("serial")
public class StepDefineForm extends FormBase<StepDefine> {
	
	private final StepDefineRepository repository;
	
	public StepDefineForm(PersonRepository personRepository, MessageSource messageSource, Domains domains, FieldFactories fieldFactories,StepDefineRepository repository, HandMakeFieldsListener handMakeFieldsListener) {
		super(StepDefine.class,personRepository, messageSource, domains, fieldFactories, handMakeFieldsListener);
		this.repository = repository;
		delayCreateContent();
	}
	
	@Override
	public boolean saveToRepo() {
        repository.save(getWrappedBean());
		return true;
	}
}
