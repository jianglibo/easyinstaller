package com.jianglibo.vaadin.dashboard.view.singleinstallation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.SingleInstallation;
import com.jianglibo.vaadin.dashboard.repositories.SingleInstallationRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.util.FormFieldsFactory;

@SuppressWarnings("serial")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SingleInstallationForm extends FormBase<SingleInstallation> {
	
	private final SingleInstallationRepository repository;
	
	@Autowired
	public SingleInstallationForm(MessageSource messageSource, Domains domains, FormFieldsFactory formFieldsFactory, SingleInstallationRepository repository) {
		super(SingleInstallation.class, messageSource, domains, formFieldsFactory);
		this.repository = repository;
	}
	
	public SingleInstallationForm afterInjection(EventBus eventBus) {
		defaultAfterInjection(eventBus);
		return this;
	}

	@Override
	public boolean saveToRepo() {
        repository.save(getWrappedBean());
		return true;
	}
}
