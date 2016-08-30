package com.jianglibo.vaadin.dashboard.view.box.install;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Install;
import com.jianglibo.vaadin.dashboard.repositories.InstallRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.util.FormFieldsFactory;

@SuppressWarnings("serial")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InstallForm extends FormBase<Install> {
	
	private final InstallRepository repository;
	
	@Autowired
	public InstallForm(MessageSource messageSource, Domains domains, FormFieldsFactory formFieldsFactory, InstallRepository repository) {
		super(Install.class, messageSource, domains, formFieldsFactory);
		this.repository = repository;
	}
	
	public InstallForm afterInjection(EventBus eventBus) {
		defaultAfterInjection(eventBus);
		return this;
	}

	@Override
	public boolean saveToRepo() {
        repository.save(getWrappedBean());
		return true;
	}
}
