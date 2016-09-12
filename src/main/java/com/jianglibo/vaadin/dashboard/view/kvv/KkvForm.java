package com.jianglibo.vaadin.dashboard.view.kvv;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Kkv;
import com.jianglibo.vaadin.dashboard.repositories.KkvRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;

@SuppressWarnings("serial")
public class KkvForm extends FormBase<Kkv> {
	
	private final KkvRepository repository;
	
	public KkvForm(MessageSource messageSource, Domains domains, FieldFactories fieldFactories, KkvRepository repository, HandMakeFieldsListener handMakeFieldsListener) {
		super(Kkv.class, messageSource, domains, fieldFactories, handMakeFieldsListener);
		this.repository = repository;
	}

	@Override
	public boolean saveToRepo() {
		Kkv in = getWrappedBean();
		repository.save(in);
		return true;
	}
}
