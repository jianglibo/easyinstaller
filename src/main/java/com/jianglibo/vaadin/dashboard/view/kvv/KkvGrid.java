package com.jianglibo.vaadin.dashboard.view.kvv;

import java.util.List;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.annotation.VaadinGridWrapper;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.Kkv;
import com.jianglibo.vaadin.dashboard.uicomponent.grid.BaseGrid;
import com.vaadin.data.util.GeneratedPropertyContainer;

@SuppressWarnings("serial")
public class KkvGrid extends BaseGrid<Kkv, FreeContainer<Kkv>> {

	public KkvGrid(FreeContainer<Kkv> dContainer,VaadinGridWrapper vgw, MessageSource messageSource, List<String> sortableContainerPropertyIds, List<String> columnNames, String messagePrefix) {
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
		if (fc != null) {
			fc.setText("0");
			getdContainer().addItemSetChangeListener(event -> {
				fc.setText("" + event.getContainer().size());
			});
		}
	}

}
