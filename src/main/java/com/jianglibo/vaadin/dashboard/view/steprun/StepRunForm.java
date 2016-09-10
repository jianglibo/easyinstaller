package com.jianglibo.vaadin.dashboard.view.steprun;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.StepRun;
import com.jianglibo.vaadin.dashboard.repositories.StepRunRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;

@SuppressWarnings("serial")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StepRunForm extends FormBase<StepRun> {
	
	private final StepRunRepository repository;
	
	@Autowired
	public StepRunForm(MessageSource messageSource, Domains domains, FieldFactories fieldFactories, StepRunRepository repository) {
		super(StepRun.class, messageSource, domains, fieldFactories, null);
		this.repository = repository;
	}

	@Override
	public boolean saveToRepo() {
        repository.save(getWrappedBean());
		return true;
	}
}
