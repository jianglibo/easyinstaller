package com.jianglibo.vaadin.dashboard.view.kvv;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Kkv;
import com.jianglibo.vaadin.dashboard.repositories.KkvRepository;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;

@SuppressWarnings("serial")
public class KkvForm extends FormBase<Kkv> {
	
	private final KkvRepository repository;
	
	public KkvForm(PersonRepository personRepository, MessageSource messageSource, Domains domains, FieldFactories fieldFactories, KkvRepository repository, HandMakeFieldsListener handMakeFieldsListener) {
		super(Kkv.class,personRepository, messageSource, domains, fieldFactories, handMakeFieldsListener);
		this.repository = repository;
		delayCreateContent();
	}

	@Override
	public boolean saveToRepo() {
		Kkv kkv = getWrappedBean();
		if (kkv.getOwner() == null) {
			kkv.setOwner(getCurrentUser());
		}
		repository.save(kkv);
		return true;
	}
}
