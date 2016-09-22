package com.jianglibo.vaadin.dashboard.view.clusterhistory;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumnWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridWrapper;
import com.jianglibo.vaadin.dashboard.domain.ClusterHistory;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.uicomponent.grid.BaseGrid;
import com.vaadin.data.util.GeneratedPropertyContainer;

@SuppressWarnings("serial")
public class ClusterHistoryGrid extends BaseGrid<ClusterHistory, ClusterHistoryContainer> {

	public ClusterHistoryGrid(MessageSource messageSource, Domains domains, Class<ClusterHistory> clazz) {
		super(messageSource, domains, clazz);
		delayCreateContent();
	}
	
	@Override
	protected ClusterHistoryContainer createContainer() {
		VaadinGridWrapper vgw = getDomains().getGrids().get(ClusterHistory.class.getSimpleName());
		return new ClusterHistoryContainer(getDomains(), vgw.getVg().defaultPerPage(), vgw.getSortableColumnNames());
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
