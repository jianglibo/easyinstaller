package com.jianglibo.vaadin.dashboard.view.box;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.repositories.BoxRepository;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;

public class BoxForm extends FormBase<Box> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final BoxRepository repository;
	
	public BoxForm(PersonRepository personRepository, MessageSource messageSource, Domains domains, FieldFactories fieldFactories, BoxRepository repository, HandMakeFieldsListener handMakeFieldsListener) {
		super(Box.class,personRepository, messageSource, domains, fieldFactories, handMakeFieldsListener);
		this.repository = repository;
		delayCreateContent();
	}
	

	@Override
	public boolean saveToRepo() {
		Box box = getWrappedBean();
		box.setCreator(getCurrentUser());
        repository.save(box);
		return true;
	}
}
