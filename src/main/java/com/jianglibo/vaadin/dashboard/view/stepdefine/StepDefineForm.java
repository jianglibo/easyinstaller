package com.jianglibo.vaadin.dashboard.view.stepdefine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.StepDefine;
import com.jianglibo.vaadin.dashboard.repositories.StepDefineRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uifactory.FormFieldsFactory;

@SuppressWarnings("serial")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StepDefineForm extends FormBase<StepDefine> {
	
	private final StepDefineRepository repository;
	
	@Autowired
	public StepDefineForm(MessageSource messageSource, Domains domains, FormFieldsFactory formFieldsFactory, StepDefineRepository repository) {
		super(StepDefine.class, messageSource, domains, formFieldsFactory);
		this.repository = repository;
	}
	
	public StepDefineForm afterInjection(EventBus eventBus, boolean attachFields) {
		defaultAfterInjection(eventBus, attachFields);
		return this;
	}

	@Override
	public boolean saveToRepo() {
        repository.save(getWrappedBean());
		return true;
	}

	@Override
	public StepDefineForm done() {
		defaultDone();
		return this;
	}
}
