package com.jianglibo.vaadin.dashboard.view.boxgroup;

import java.util.List;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumnWrapper;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.BoxGroup;
import com.jianglibo.vaadin.dashboard.uicomponent.grid.BaseGrid;
import com.vaadin.data.util.GeneratedPropertyContainer;

@SuppressWarnings("serial")
public class BoxGroupGrid extends BaseGrid<BoxGroup, FreeContainer<BoxGroup>> {

	public BoxGroupGrid(FreeContainer<BoxGroup> dContainer, MessageSource messageSource, Domains domains, List<?> sortableContainerPropertyIds) {
		super(dContainer, messageSource, domains, BoxGroup.class, sortableContainerPropertyIds);
		delayCreateContent();
	}

	@Override
	protected void addGeneratedProperty(GeneratedPropertyContainer gpcontainer, String name) {
	}

	@Override
	protected void setSummaryFooterCells(FooterRow footer) {
		FooterCell fc = footer.getCell("createdAt");
		fc.setText("100");
	}

	@Override
	protected void setupColumn(Column col, VaadinGridColumnWrapper vgcw) {
		
	}

}
