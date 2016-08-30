package com.jianglibo.vaadin.dashboard.view.installstep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.InstallStepDefine;
import com.jianglibo.vaadin.dashboard.repositories.InstallStepRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.util.FormFieldsFactory;

@SuppressWarnings("serial")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InstallStepForm extends FormBase<InstallStepDefine> {
	
	private final InstallStepRepository repository;
	
	@Autowired
	public InstallStepForm(MessageSource messageSource, Domains domains, FormFieldsFactory formFieldsFactory, InstallStepRepository repository) {
		super(InstallStepDefine.class, messageSource, domains, formFieldsFactory);
		this.repository = repository;
	}
	
	public InstallStepForm afterInjection(EventBus eventBus) {
		defaultAfterInjection(eventBus);
		return this;
	}

	@Override
	public boolean saveToRepo() {
        repository.save(getWrappedBean());
		return true;
	}
}
