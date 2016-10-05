package com.jianglibo.vaadin.dashboard.view.boxhistory;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.domain.BoxHistory;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.repositories.BoxHistoryRepository;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;

@SuppressWarnings("serial")
public class BoxHistoryForm extends FormBase<BoxHistory> {
	
	private final BoxHistoryRepository repository;
	
	public BoxHistoryForm(PersonRepository personRepository, MessageSource messageSource, Domains domains, FieldFactories fieldFactories, BoxHistoryRepository repository, HandMakeFieldsListener handMakeFieldsListener) {
		super(BoxHistory.class,personRepository, messageSource, domains, fieldFactories, handMakeFieldsListener);
		this.repository = repository;
		delayCreateContent();
	}

	@Override
	public boolean saveToRepo() {
//		BoxHistory bean = getWrappedBean();
//		repository.save(bean);
		return true;
	}
}
