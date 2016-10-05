package com.jianglibo.vaadin.dashboard.view.boxgrouphistory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.domain.BoxGroupHistory;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.repositories.BoxGroupHistoryRepository;
import com.jianglibo.vaadin.dashboard.repositories.BoxHistoryRepository;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase.HandMakeFieldsListener;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;
import com.jianglibo.vaadin.dashboard.view.BaseEditView;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Field;

@SpringView(name = BoxGroupHistoryEditView.VIEW_NAME)
public class BoxGroupHistoryEditView
		extends BaseEditView<BoxGroupHistory, FormBase<BoxGroupHistory>, JpaRepository<BoxGroupHistory, Long>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(BoxGroupHistoryEditView.class);

	public static final String VIEW_NAME = BoxGroupHistoryListView.VIEW_NAME + "/edit";

	private final PersonRepository personRepository;

	@Autowired
	public BoxGroupHistoryEditView(PersonRepository personRepository, BoxGroupHistoryRepository repository, MessageSource messageSource,
			Domains domains, FieldFactories fieldFactories, ApplicationContext applicationContext) {
		super(messageSource, BoxGroupHistory.class, domains, fieldFactories, repository);
		this.personRepository = personRepository;
		delayCreateContent();
	}

	@Override
	public Field<?> createField(VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw) {
		return null;
	}


	@Override
	protected BoxGroupHistory createNewBean() {
		return new BoxGroupHistory();
	}

	@Override
	protected String getListViewName() {
		return VIEW_NAME;
	}

	@Override
	protected FormBase<BoxGroupHistory> createForm(MessageSource messageSource, Domains domains,
			FieldFactories fieldFactories, JpaRepository<BoxGroupHistory, Long> repository,
			HandMakeFieldsListener handMakeFieldsListener) {
			return new BoxGroupHistoryForm(personRepository, getMessageSource(), getDomains(), fieldFactories,
					(BoxGroupHistoryRepository) repository, handMakeFieldsListener);
	}
}
