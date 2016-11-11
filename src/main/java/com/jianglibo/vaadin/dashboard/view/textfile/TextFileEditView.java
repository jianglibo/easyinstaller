package com.jianglibo.vaadin.dashboard.view.textfile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.TextFile;
import com.jianglibo.vaadin.dashboard.repositories.TextFileRepository;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase.HandMakeFieldsListener;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;
import com.jianglibo.vaadin.dashboard.view.BaseEditView;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Field;


@SpringView(name = TextFileEditView.VIEW_NAME)
public class TextFileEditView  extends BaseEditView<TextFile, FormBase<TextFile>, JpaRepository<TextFile,Long>>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(TextFileEditView.class);

	public static final String VIEW_NAME = TextFileListView.VIEW_NAME + "/edit";
	
	private final PersonRepository personRepository;

	@Autowired
	public TextFileEditView(PersonRepository personRepository, TextFileRepository repository,MessageSource messageSource,Domains domains,FieldFactories fieldFactories,
			ApplicationContext applicationContext) {
		super(messageSource,TextFile.class, domains, fieldFactories, repository);
		this.personRepository = personRepository;
		delayCreateContent();
	}


	@Override
	public Field<?> createField(VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw) {
		return null;
	}

	@Override
	protected FormBase<TextFile> createForm(MessageSource messageSource, Domains domains, FieldFactories fieldFactories,
			JpaRepository<TextFile, Long> repository,HandMakeFieldsListener handMakeFieldsListener) {
		return new TextFileForm(personRepository, getMessageSource(), getDomains(), fieldFactories, (TextFileRepository) repository, handMakeFieldsListener);
	}

	@Override
	protected TextFile createNewBean() {
		return new TextFile();
	}

	@Override
	protected String getListViewName() {
		return VIEW_NAME;
	}
}
