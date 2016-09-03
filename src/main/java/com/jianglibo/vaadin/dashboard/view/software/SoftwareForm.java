package com.jianglibo.vaadin.dashboard.view.software;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.repositories.SoftwareRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;

@SuppressWarnings("serial")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SoftwareForm extends FormBase<Software> {
	
	private final SoftwareRepository repository;
	
	@Autowired
	public SoftwareForm(MessageSource messageSource, Domains domains, FieldFactories fieldFactories, SoftwareRepository repository) {
		super(Software.class, messageSource, domains, fieldFactories);
		this.repository = repository;
	}
	
	public SoftwareForm afterInjection(EventBus eventBus, boolean attachFields) {
		defaultAfterInjection(eventBus, attachFields);
		return this;
	}
	
	@Override
	public boolean saveToRepo() {
        repository.save(getWrappedBean());
		return true;
	}

	@Override
	public FormBase<Software> done() {
		defaultDone();
		return this;
	}
}
