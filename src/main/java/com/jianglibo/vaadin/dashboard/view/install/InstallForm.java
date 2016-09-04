package com.jianglibo.vaadin.dashboard.view.install;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Install;
import com.jianglibo.vaadin.dashboard.domain.StepRun;
import com.jianglibo.vaadin.dashboard.repositories.InstallRepository;
import com.jianglibo.vaadin.dashboard.repositories.StepRunRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;

@SuppressWarnings("serial")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InstallForm extends FormBase<Install> {
	
	private final InstallRepository repository;
	
	private final StepRunRepository stepRunRepository;
	
	@Autowired
	public InstallForm(MessageSource messageSource, Domains domains, FieldFactories fieldFactories, InstallRepository repository, StepRunRepository stepRunRepository) {
		super(Install.class, messageSource, domains, fieldFactories);
		this.repository = repository;
		this.stepRunRepository = stepRunRepository;
	}
	
	public InstallForm afterInjection(EventBus eventBus, boolean attachFields) {
		defaultAfterInjection(eventBus, attachFields);
		return this;
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

	@Override
	public InstallForm done() {
		defaultDone();
		return this;
	}
}
