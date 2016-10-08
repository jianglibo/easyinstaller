package com.jianglibo.vaadin.dashboard.view.kvv;

import java.util.List;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumnWrapper;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Kkv;
import com.jianglibo.vaadin.dashboard.uicomponent.grid.BaseGrid;
import com.vaadin.data.util.GeneratedPropertyContainer;

@SuppressWarnings("serial")
public class KkvGrid extends BaseGrid<Kkv, FreeContainer<Kkv>> {

	public KkvGrid(FreeContainer<Kkv> dContainer, MessageSource messageSource, Domains domains, List<?> sortableContainerPropertyIds) {
		super(dContainer, messageSource, domains, Kkv.class, sortableContainerPropertyIds);
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
