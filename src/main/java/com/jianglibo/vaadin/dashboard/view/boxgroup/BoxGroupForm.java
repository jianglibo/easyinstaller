package com.jianglibo.vaadin.dashboard.view.boxgroup;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.BoxGroup;
import com.jianglibo.vaadin.dashboard.repositories.BoxGroupRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;

@SuppressWarnings("serial")
public class BoxGroupForm extends FormBase<BoxGroup> {
	
	private final BoxGroupRepository repository;
	
	public BoxGroupForm(MessageSource messageSource, Domains domains, FieldFactories fieldFactories, BoxGroupRepository repository, HandMakeFieldsListener handMakeFieldsListener) {
		super(BoxGroup.class, messageSource, domains, fieldFactories, handMakeFieldsListener);
		this.repository = repository;
	}

	@Override
	public boolean saveToRepo() {
		BoxGroup in = getWrappedBean();
		repository.save(in);
		return true;
	}
}
