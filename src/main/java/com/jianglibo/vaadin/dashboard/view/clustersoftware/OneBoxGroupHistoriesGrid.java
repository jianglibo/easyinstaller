package com.jianglibo.vaadin.dashboard.view.clustersoftware;

import java.util.List;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.annotation.VaadinGridWrapper;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.BoxGroupHistory;
import com.jianglibo.vaadin.dashboard.uicomponent.grid.BaseGrid;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class OneBoxGroupHistoriesGrid extends BaseGrid<BoxGroupHistory, FreeContainer<BoxGroupHistory>>{
	
	public OneBoxGroupHistoriesGrid(OneBoxGroupHistoriesDc dContainer,VaadinGridWrapper vgw, MessageSource messageSource, List<String> sortableContainerPropertyIds, List<String> columnNames, String messagePrefix) {
		super(vgw, dContainer, messageSource, sortableContainerPropertyIds, columnNames, messagePrefix);
		delayCreateContent();
	}

	@Override
	protected void addGeneratedProperty(GeneratedPropertyContainer gpcontainer, String name) {
	}

	@Override
	protected void setupGrid() {
		HeaderRow hr = addHeaderRowAt(0);
		HeaderCell namesCell = hr.join(
				hr.getCell("software"),
				hr.getCell("boxGroup"));
		namesCell.setComponent(new Label("hello"));	
	}
}
