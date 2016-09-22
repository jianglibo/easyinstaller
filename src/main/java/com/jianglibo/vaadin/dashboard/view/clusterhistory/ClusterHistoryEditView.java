package com.jianglibo.vaadin.dashboard.view.clusterhistory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.domain.ClusterHistory;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.repositories.ClusterHistoryRepository;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase.HandMakeFieldsListener;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;
import com.jianglibo.vaadin.dashboard.view.BaseEditView;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Field;

@SpringView(name = ClusterHistoryEditView.VIEW_NAME)
public class ClusterHistoryEditView
		extends BaseEditView<ClusterHistory, FormBase<ClusterHistory>, JpaRepository<ClusterHistory, Long>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ClusterHistoryEditView.class);

	public static final String VIEW_NAME = ClusterHistoryListView.VIEW_NAME + "/edit";

	private final PersonRepository personRepository;

	@Autowired
	public ClusterHistoryEditView(PersonRepository personRepository, ClusterHistoryRepository repository, MessageSource messageSource,
			Domains domains, FieldFactories fieldFactories, ApplicationContext applicationContext) {
		super(messageSource, ClusterHistory.class, domains, fieldFactories, repository);
		this.personRepository = personRepository;
		delayCreateContent();
	}

	@Override
	public Field<?> createField(VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw) {
		return null;
	}

	@Override
	protected FormBase<ClusterHistory> createForm(MessageSource messageSource, Domains domains,
			FieldFactories fieldFactories, JpaRepository<ClusterHistory, Long> repository,
			HandMakeFieldsListener handMakeFieldsListener) {
		return new ClusterHistoryForm(personRepository, getMessageSource(), getDomains(), fieldFactories,
				(ClusterHistoryRepository) repository, handMakeFieldsListener);
	}

	@Override
	protected ClusterHistory createNewBean() {
		return new ClusterHistory();
	}

	@Override
	protected String getListViewName() {
		return VIEW_NAME;
	}
}
