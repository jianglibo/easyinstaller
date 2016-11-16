package com.jianglibo.vaadin.dashboard.view.boxgroup;

import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.google.common.collect.Sets;
import com.jianglibo.vaadin.dashboard.domain.Box;
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
		// because all entity maybe detached, they are not the same object.
		Set<Box> newBoxes = bg.getBoxes();
		
		Set<Box> originBoxes = boxRepository.findByBoxGroup(bg);
		
		for (Box b : originBoxes) {
			// if origin box not in newBoxes, must remove.
			if (!newBoxes.stream().anyMatch(bItem -> bItem.getId() == b.getId())) {
				Set<BoxGroup> newbgs = Sets.newHashSet(b.getBoxGroups()).stream().filter(bgItem -> bgItem.getId() != bg.getId()).collect(Collectors.toSet());
				b.setBoxGroups(newbgs);
				boxRepository.save(b);
			}
		}
		bg.setBoxes(newBoxes);
		repository.save(bg);
		
		for (Box b: bg.getBoxes()) {
			if (!b.getBoxGroups().stream().anyMatch(bgItem -> bgItem.getId() == bg.getId())) {
				Set<BoxGroup> newbgs = Sets.newHashSet(b.getBoxGroups());
				newbgs.add(bg);
				b.setBoxGroups(newbgs);
				boxRepository.save(b);
			}
		}
		return true;
	}
}
