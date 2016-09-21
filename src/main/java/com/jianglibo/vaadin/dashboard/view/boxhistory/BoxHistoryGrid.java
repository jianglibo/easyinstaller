package com.jianglibo.vaadin.dashboard.view.boxhistory;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumnWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridWrapper;
import com.jianglibo.vaadin.dashboard.domain.BoxHistory;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.uicomponent.grid.BaseGrid;
import com.vaadin.data.util.GeneratedPropertyContainer;

@SuppressWarnings("serial")
public class BoxHistoryGrid extends BaseGrid<BoxHistory, BoxHistoryContainer> {

	public BoxHistoryGrid(MessageSource messageSource, Domains domains, Class<BoxHistory> clazz) {
		super(messageSource, domains, clazz);
		delayCreateContent();
	}
	
	@Override
	protected BoxHistoryContainer createContainer() {
		VaadinGridWrapper vgw = getDomains().getGrids().get(BoxHistory.class.getSimpleName());
		return new BoxHistoryContainer(getDomains(), vgw.getVg().defaultPerPage(), vgw.getSortableColumnNames());
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
