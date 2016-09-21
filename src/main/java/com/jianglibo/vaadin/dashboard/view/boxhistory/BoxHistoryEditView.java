package com.jianglibo.vaadin.dashboard.view.boxhistory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.domain.BoxHistory;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.repositories.BoxHistoryRepository;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase.HandMakeFieldsListener;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;
import com.jianglibo.vaadin.dashboard.view.BaseEditView;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Field;

@SpringView(name = BoxHistoryEditView.VIEW_NAME)
public class BoxHistoryEditView
		extends BaseEditView<BoxHistory, FormBase<BoxHistory>, JpaRepository<BoxHistory, Long>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(BoxHistoryEditView.class);

	public static final String VIEW_NAME = BoxHistoryListView.VIEW_NAME + "/edit";

	private final PersonRepository personRepository;

	@Autowired
	public BoxHistoryEditView(PersonRepository personRepository, BoxHistoryRepository repository, MessageSource messageSource,
			Domains domains, FieldFactories fieldFactories, ApplicationContext applicationContext) {
		super(messageSource, BoxHistory.class, domains, fieldFactories, repository);
		this.personRepository = personRepository;
		delayCreateContent();
	}

	@Override
	public Field<?> createField(VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw) {
		return null;
	}

	@Override
	protected FormBase<BoxHistory> createForm(MessageSource messageSource, Domains domains,
			FieldFactories fieldFactories, JpaRepository<BoxHistory, Long> repository,
			HandMakeFieldsListener handMakeFieldsListener) {
		return new BoxHistoryForm(personRepository, getMessageSource(), getDomains(), fieldFactories,
				(BoxHistoryRepository) repository, handMakeFieldsListener);
	}

	@Override
	protected BoxHistory createNewBean() {
		return new BoxHistory();
	}

	@Override
	protected String getListViewName() {
		return VIEW_NAME;
	}
}
