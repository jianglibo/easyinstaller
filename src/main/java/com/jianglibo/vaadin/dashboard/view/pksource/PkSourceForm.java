package com.jianglibo.vaadin.dashboard.view.pksource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.PkSource;
import com.jianglibo.vaadin.dashboard.repositories.PkSourceRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;

@SuppressWarnings("serial")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PkSourceForm extends FormBase<PkSource>{

	private final PkSourceRepository repository;
	
	@Autowired
	public PkSourceForm(MessageSource messageSource, Domains domains, FieldFactories fieldFactories, PkSourceRepository repository) {
		super(PkSource.class, messageSource, domains, fieldFactories, null);
		this.repository = repository;
	}
	
	@Override
	public boolean saveToRepo() {
		repository.save(getWrappedBean());
		return true;
	}
}
