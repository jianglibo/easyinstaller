package com.jianglibo.vaadin.dashboard.view.boxgrouphistory;

import java.util.List;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumnWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridWrapper;
import com.jianglibo.vaadin.dashboard.domain.BoxGroupHistory;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.repositories.BoxGroupHistoryRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.grid.BaseGrid;
import com.vaadin.data.util.GeneratedPropertyContainer;

@SuppressWarnings("serial")
public class BoxGroupHistoryGrid extends BaseGrid<BoxGroupHistory, BoxGroupHistoryContainer> {
	
	private final BoxGroupHistoryRepository boxGroupHistoryRepository;

	public BoxGroupHistoryGrid(BoxGroupHistoryContainer dContainer, BoxGroupHistoryRepository boxGroupHistoryRepository, MessageSource messageSource, Domains domains, List<?> sortableContainerPropertyIds) {
		super(dContainer, messageSource, domains, BoxGroupHistory.class, sortableContainerPropertyIds);
		this.boxGroupHistoryRepository = boxGroupHistoryRepository;
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
