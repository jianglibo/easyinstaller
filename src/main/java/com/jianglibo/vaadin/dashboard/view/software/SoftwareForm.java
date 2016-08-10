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
import com.jianglibo.vaadin.dashboard.util.FormFieldsFactory;

@SuppressWarnings("serial")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SoftwareForm extends FormBase<Software> {
	
	private final SoftwareRepository repository;
	
	@Autowired
	public SoftwareForm(MessageSource messageSource, Domains domains, FormFieldsFactory formFieldsFactory, SoftwareRepository repository) {
		super(Software.class, messageSource, domains, formFieldsFactory);
		this.repository = repository;
	}
	
	public SoftwareForm afterInjection(EventBus eventBus) {
		defaultAfterInjection(eventBus);
		return this;
	}

	@Override
	public boolean saveToRepo() {
        repository.save(getWrappedBean());
		return true;
	}
}
