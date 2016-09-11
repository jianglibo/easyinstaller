package com.jianglibo.vaadin.dashboard.view.software;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.repositories.SoftwareRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase.HandMakeFieldsListener;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;

@SuppressWarnings("serial")
public class SoftwareForm extends FormBase<Software> {
	
	private final SoftwareRepository repository;
	
	public SoftwareForm(MessageSource messageSource, Domains domains, FieldFactories fieldFactories, SoftwareRepository repository, HandMakeFieldsListener handMakeFieldsListener) {
		super(Software.class, messageSource, domains, fieldFactories,handMakeFieldsListener);
		this.repository = repository;
	}
	
	@Override
	public boolean saveToRepo() {
        repository.save(getWrappedBean());
		return true;
	}
}
