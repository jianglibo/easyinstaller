package com.jianglibo.vaadin.dashboard.view.boxhistory;

import java.util.List;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.annotation.VaadinGridWrapper;
import com.jianglibo.vaadin.dashboard.domain.BoxHistory;
import com.jianglibo.vaadin.dashboard.repositories.BoxGroupHistoryRepository;
import com.jianglibo.vaadin.dashboard.repositories.BoxHistoryRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.grid.BaseGrid;
import com.jianglibo.vaadin.dashboard.util.ColumnUtil;
import com.vaadin.data.util.GeneratedPropertyContainer;

@SuppressWarnings("serial")
public class BoxHistoryGrid extends BaseGrid<BoxHistory, BoxHistoryContainer> {
	
	private final BoxHistoryRepository boxHistoryRepository;
	
	private final BoxGroupHistoryRepository boxGroupHistoryRepository;

	public BoxHistoryGrid(BoxHistoryContainer dContainer,VaadinGridWrapper vgw, BoxHistoryRepository boxHistoryRepository,BoxGroupHistoryRepository boxGroupHistoryRepository, MessageSource messageSource, List<String> sortableContainerPropertyIds, List<String> columnNames, String messagePrefix) {
		super(vgw, dContainer, messageSource, sortableContainerPropertyIds, columnNames, messagePrefix);
		this.boxGroupHistoryRepository = boxGroupHistoryRepository;
		this.boxHistoryRepository = boxHistoryRepository;
		delayCreateContent();
	}

	@Override
	protected void addGeneratedProperty(GeneratedPropertyContainer gpcontainer, String name) {
	}
	
	@Override
	protected void setupGrid() {
		FooterRow footer = addFooterRowAt(0);
		FooterCell fc = footer.getCell("createdAt");
		fc.setText("0");
		getdContainer().addItemSetChangeListener(event -> {
			fc.setText("" + event.getContainer().size());
		});
	}

}
