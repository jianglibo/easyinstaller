package com.jianglibo.vaadin.dashboard.view.clustersoftware;

import java.util.List;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumnWrapper;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.BoxGroupHistory;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.uicomponent.grid.BaseGrid;
import com.vaadin.data.util.GeneratedPropertyContainer;

@SuppressWarnings("serial")
public class OneBoxGroupHistoriesGrid extends BaseGrid<BoxGroupHistory, FreeContainer<BoxGroupHistory>>{
	
	public OneBoxGroupHistoriesGrid(OneBoxGroupHistoriesDc dContainer, MessageSource messageSource, Domains domains, List<?> sortableContainerPropertyIds) {
		super(dContainer, messageSource, domains, BoxGroupHistory.class, sortableContainerPropertyIds);
		delayCreateContent();
	}

	@Override
	protected void setSummaryFooterCells(FooterRow footer) {
		HeaderRow hr = addHeaderRowAt(0);
		HeaderCell namesCell = hr.join(
				hr.getCell("firstname"),
				hr.getCell("lastname"));
		
	}

	@Override
	protected void setupColumn(Column col, VaadinGridColumnWrapper vgcw) {
		
	}

	@Override
	protected void addGeneratedProperty(GeneratedPropertyContainer gpcontainer, String name) {
	}
}
