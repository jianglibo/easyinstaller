package com.jianglibo.vaadin.dashboard.view.software;

import java.util.List;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.annotation.VaadinGridWrapper;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.uicomponent.grid.BaseGrid;
import com.vaadin.data.util.GeneratedPropertyContainer;

@SuppressWarnings("serial")
public class SoftwareGrid extends BaseGrid<Software, FreeContainer<Software>> {

	public SoftwareGrid(FreeContainer<Software> dContainer,VaadinGridWrapper vgw, MessageSource messageSource, List<String> sortableContainerPropertyIds, List<String> columnNames, String messagePrefix) {
		super(vgw, dContainer, messageSource, sortableContainerPropertyIds, columnNames, messagePrefix);
		delayCreateContent();
	}

	@Override
	protected void addGeneratedProperty(GeneratedPropertyContainer gpcontainer, String name) {
	}

	@Override
	protected void setupGrid() {
		FooterRow footer = addFooterRowAt(0);
		FooterCell fc = footer.getCell("createdAt");
		
		if (fc == null) {
			fc = footer.getCell("updatedAt"); 
		}
		fc.setText("0");
		final FooterCell fcf = fc;
		getdContainer().addItemSetChangeListener(event -> {
			fcf.setText("" + event.getContainer().size());
		});
		
	}
}
