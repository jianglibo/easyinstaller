package com.jianglibo.vaadin.dashboard.view.boxgrouphistory;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.domain.BoxGroupHistory;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.repositories.BoxGroupHistoryRepository;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;

@SuppressWarnings("serial")
public class BoxGroupHistoryForm extends FormBase<BoxGroupHistory> {
	
	private final BoxGroupHistoryRepository repository;
	
	public BoxGroupHistoryForm(PersonRepository personRepository, MessageSource messageSource, Domains domains, FieldFactories fieldFactories, BoxGroupHistoryRepository repository, HandMakeFieldsListener handMakeFieldsListener) {
		super(BoxGroupHistory.class,personRepository, messageSource, domains, fieldFactories, handMakeFieldsListener);
		this.repository = repository;
		delayCreateContent();
	}

	@Override
	public boolean saveToRepo() {
		BoxGroupHistory bean = getWrappedBean();
//		if (bean.getRunner() == null) {
//			bean.setRunner(getCurrentUser());
//		}
		repository.save(bean);
		return true;
	}
}
