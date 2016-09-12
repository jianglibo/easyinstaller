package com.jianglibo.vaadin.dashboard.view.kvv;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Kkv;
import com.jianglibo.vaadin.dashboard.uicomponent.grid.BaseGrid;
import com.vaadin.data.util.GeneratedPropertyContainer;

@SuppressWarnings("serial")
public class KkvGrid extends BaseGrid<Kkv> {

	public KkvGrid(MessageSource messageSource, Domains domains, Class<Kkv> clazz) {
		super(messageSource, domains, clazz);
	}

	@Override
	protected void setupColumn(Column col, String cn) {
		
	}

	@Override
	protected void addGeneratedProperty(GeneratedPropertyContainer gpcontainer, String name) {
	}

}
