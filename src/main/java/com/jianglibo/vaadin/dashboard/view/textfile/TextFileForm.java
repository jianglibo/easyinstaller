package com.jianglibo.vaadin.dashboard.view.textfile;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.TextFile;
import com.jianglibo.vaadin.dashboard.repositories.TextFileRepository;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;

@SuppressWarnings("serial")
public class TextFileForm extends FormBase<TextFile> {
	
	private final TextFileRepository repository;
	
	public TextFileForm(PersonRepository personRepository, MessageSource messageSource, Domains domains, FieldFactories fieldFactories, TextFileRepository repository, HandMakeFieldsListener handMakeFieldsListener) {
		super(TextFile.class,personRepository, messageSource, domains, fieldFactories, handMakeFieldsListener);
		this.repository = repository;
		delayCreateContent();
	}

	@Override
	public boolean saveToRepo() {
		TextFile kkv = getWrappedBean();
		repository.save(kkv);
		return true;
	}
}
