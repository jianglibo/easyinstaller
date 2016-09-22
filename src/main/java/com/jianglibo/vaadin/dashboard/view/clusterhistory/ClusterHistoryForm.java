package com.jianglibo.vaadin.dashboard.view.clusterhistory;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.domain.ClusterHistory;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.repositories.ClusterHistoryRepository;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;

@SuppressWarnings("serial")
public class ClusterHistoryForm extends FormBase<ClusterHistory> {
	
	private final ClusterHistoryRepository repository;
	
	public ClusterHistoryForm(PersonRepository personRepository, MessageSource messageSource, Domains domains, FieldFactories fieldFactories, ClusterHistoryRepository repository, HandMakeFieldsListener handMakeFieldsListener) {
		super(ClusterHistory.class,personRepository, messageSource, domains, fieldFactories, handMakeFieldsListener);
		this.repository = repository;
		delayCreateContent();
	}

	@Override
	public boolean saveToRepo() {
		ClusterHistory bean = getWrappedBean();
		if (bean.getRunner() == null) {
			bean.setRunner(getCurrentUser());
		}
		repository.save(bean);
		return true;
	}
}
