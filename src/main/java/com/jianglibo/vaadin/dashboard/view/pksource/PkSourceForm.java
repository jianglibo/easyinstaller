package com.jianglibo.vaadin.dashboard.view.pksource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.PkSource;
import com.jianglibo.vaadin.dashboard.repositories.PkSourceRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uifactory.FormFieldsFactory;

@SuppressWarnings("serial")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PkSourceForm extends FormBase<PkSource>{

	private final PkSourceRepository repository;
	
	@Autowired
	public PkSourceForm(MessageSource messageSource, Domains domains, FormFieldsFactory formFieldsFactory, PkSourceRepository repository) {
		super(PkSource.class, messageSource, domains, formFieldsFactory);
		this.repository = repository;
	}
	
	public PkSourceForm afterInjection(EventBus eventBus, boolean attachFields) {
		defaultAfterInjection(eventBus,attachFields);
		return this;
	}

	@Override
	public boolean saveToRepo() {
		repository.save(getWrappedBean());
		return true;
	}
}
