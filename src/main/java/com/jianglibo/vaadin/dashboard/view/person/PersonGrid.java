package com.jianglibo.vaadin.dashboard.view.person;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumnWrapper;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Person;
import com.jianglibo.vaadin.dashboard.uicomponent.grid.BaseGrid;
import com.vaadin.data.util.GeneratedPropertyContainer;

@SuppressWarnings("serial")
public class PersonGrid extends BaseGrid<Person> {

	public PersonGrid(MessageSource messageSource, Domains domains, Class<Person> clazz) {
		super(messageSource, domains, clazz);
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
