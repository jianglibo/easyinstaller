package com.jianglibo.vaadin.dashboard.view.stepdefine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.StepDefine;
import com.jianglibo.vaadin.dashboard.repositories.StepDefineRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;

@SuppressWarnings("serial")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StepDefineForm extends FormBase<StepDefine> {
	
	private final StepDefineRepository repository;
	
	@Autowired
	public StepDefineForm(MessageSource messageSource, Domains domains, FieldFactories fieldFactories,StepDefineRepository repository) {
		super(StepDefine.class, messageSource, domains, fieldFactories);
		this.repository = repository;
	}
	
	@Override
	public boolean saveToRepo() {
        repository.save(getWrappedBean());
		return true;
	}
}
