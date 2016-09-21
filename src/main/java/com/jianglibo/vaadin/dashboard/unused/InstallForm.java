package com.jianglibo.vaadin.dashboard.unused;

import java.util.List;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase.HandMakeFieldsListener;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;

@SuppressWarnings("serial")
public class InstallForm extends FormBase<Install> {
	
	private final InstallRepository repository;
	
	private final StepRunRepository stepRunRepository;
	
	public InstallForm(PersonRepository personRepository, MessageSource messageSource, Domains domains, FieldFactories fieldFactories, InstallRepository repository, StepRunRepository stepRunRepository, HandMakeFieldsListener handMakeFieldsListener) {
		super(Install.class,personRepository, messageSource, domains, fieldFactories, handMakeFieldsListener);
		this.repository = repository;
		this.stepRunRepository = stepRunRepository;
		delayCreateContent();
	}

	@Override
	public boolean saveToRepo() {
		Install in = getWrappedBean();
		List<StepRun> stepRuns = in.getStepRuns();
		Install inf = repository.save(in);
        stepRuns.stream().forEach(st -> {
        	st.setInstall(inf);
        	stepRunRepository.save(st);
        });
		return true;
	}
}
