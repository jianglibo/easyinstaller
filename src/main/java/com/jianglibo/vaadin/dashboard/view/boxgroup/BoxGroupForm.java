package com.jianglibo.vaadin.dashboard.view.boxgroup;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.BoxGroup;
import com.jianglibo.vaadin.dashboard.repositories.BoxGroupRepository;
import com.jianglibo.vaadin.dashboard.repositories.BoxRepository;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;

@SuppressWarnings("serial")
public class BoxGroupForm extends FormBase<BoxGroup> {
	
	private final BoxGroupRepository repository;
	
	private final BoxRepository boxRepository;
	
	public BoxGroupForm(PersonRepository personRepository, MessageSource messageSource, Domains domains, FieldFactories fieldFactories, BoxGroupRepository repository,BoxRepository boxRepository, HandMakeFieldsListener handMakeFieldsListener) {
		super(BoxGroup.class,personRepository, messageSource, domains, fieldFactories, handMakeFieldsListener);
		this.repository = repository;
		this.boxRepository = boxRepository;
		delayCreateContent();
	}

	@Override
	public boolean saveToRepo() {
		BoxGroup bg = getWrappedBean();
		if (bg.getCreator() == null) {
			bg.setCreator(getCurrentUser());
		}
		
		final BoxGroup fbg = repository.save(bg);
		fbg.getBoxes().forEach(b -> {
			b.getBoxGroups().add(fbg);
			boxRepository.save(b);
		});
		return true;
	}
}
