package com.jianglibo.vaadin.dashboard.view.box;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.repositories.BoxRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;
import com.jianglibo.vaadin.dashboard.uifactory.HandMakeFieldsListener;

public class BoxForm extends FormBase<Box> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final BoxRepository repository;
	
	public BoxForm(MessageSource messageSource, Domains domains, FieldFactories fieldFactories, BoxRepository repository, HandMakeFieldsListener handMakeFieldsListener) {
		super(Box.class, messageSource, domains, fieldFactories, handMakeFieldsListener);
		this.repository = repository;
	}
	

	@Override
	public boolean saveToRepo() {
        repository.save(getWrappedBean());
		return true;
	}
}
