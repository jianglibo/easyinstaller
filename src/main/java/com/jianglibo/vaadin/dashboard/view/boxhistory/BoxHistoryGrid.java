package com.jianglibo.vaadin.dashboard.view.boxhistory;

import java.util.List;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumnWrapper;
import com.jianglibo.vaadin.dashboard.domain.BoxHistory;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.repositories.BoxGroupHistoryRepository;
import com.jianglibo.vaadin.dashboard.repositories.BoxHistoryRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.grid.BaseGrid;
import com.vaadin.data.util.GeneratedPropertyContainer;

@SuppressWarnings("serial")
public class BoxHistoryGrid extends BaseGrid<BoxHistory, BoxHistoryContainer> {
	
	private final BoxHistoryRepository boxHistoryRepository;
	
	private final BoxGroupHistoryRepository boxGroupHistoryRepository;

	public BoxHistoryGrid(BoxHistoryContainer dContainer, BoxHistoryRepository boxHistoryRepository,BoxGroupHistoryRepository boxGroupHistoryRepository, MessageSource messageSource, Domains domains, List<?> sortableContainerPropertyIds) {
		super(dContainer, messageSource, domains, BoxHistory.class, sortableContainerPropertyIds);
		this.boxGroupHistoryRepository = boxGroupHistoryRepository;
		this.boxHistoryRepository = boxHistoryRepository;
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
