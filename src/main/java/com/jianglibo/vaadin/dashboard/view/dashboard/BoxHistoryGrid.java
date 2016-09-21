package com.jianglibo.vaadin.dashboard.view.dashboard;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumnWrapper;
import com.jianglibo.vaadin.dashboard.domain.BoxHistory;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.uicomponent.grid.BaseGrid;
import com.jianglibo.vaadin.dashboard.view.boxhistory.BoxHistoryContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;

@SuppressWarnings("serial")
public class BoxHistoryGrid extends BaseGrid<BoxHistory, BoxHistoryContainer>{
	
	public BoxHistoryGrid(MessageSource messageSource, Domains domains, Class<BoxHistory> clazz) {
		super(messageSource, domains, clazz);
		delayCreateContent();
	}

	@Override
	protected void setSummaryFooterCells(FooterRow footer) {
		
	}

	@Override
	protected void setupColumn(Column col, VaadinGridColumnWrapper vgcw) {
		
	}

	@Override
	protected void addGeneratedProperty(GeneratedPropertyContainer gpcontainer, String name) {
		
	}

}
