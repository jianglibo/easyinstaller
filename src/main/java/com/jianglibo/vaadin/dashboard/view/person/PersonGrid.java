package com.jianglibo.vaadin.dashboard.view.person;

import java.util.List;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumnWrapper;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Person;
import com.jianglibo.vaadin.dashboard.uicomponent.grid.BaseGrid;
import com.vaadin.data.util.GeneratedPropertyContainer;

@SuppressWarnings("serial")
public class PersonGrid extends BaseGrid<Person, FreeContainer<Person>> {

	public PersonGrid(FreeContainer<Person> dContainer, MessageSource messageSource, Domains domains, List<?> sortableContainerPropertyIds) {
		super(dContainer, messageSource, domains, Person.class, sortableContainerPropertyIds);
		delayCreateContent();
	}

	@Override
	protected void addGeneratedProperty(GeneratedPropertyContainer gpcontainer, String name) {
	}

	@Override
	protected void setSummaryFooterCells(FooterRow footer) {
		
	}

	@Override
	protected void setupColumn(Column col, VaadinGridColumnWrapper vgcw) {
		
	}

}
