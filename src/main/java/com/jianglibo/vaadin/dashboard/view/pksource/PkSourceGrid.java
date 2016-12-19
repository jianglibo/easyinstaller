package com.jianglibo.vaadin.dashboard.view.pksource;

import java.util.List;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.annotation.VaadinGridWrapper;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.PkSource;
import com.jianglibo.vaadin.dashboard.uicomponent.grid.BaseGrid;
import com.jianglibo.vaadin.dashboard.util.ColumnUtil;
import com.vaadin.data.util.GeneratedPropertyContainer;

@SuppressWarnings("serial")
public class PkSourceGrid extends BaseGrid<PkSource, FreeContainer<PkSource>> {

	public PkSourceGrid(FreeContainer<PkSource> dContainer,VaadinGridWrapper vgw, MessageSource messageSource, List<String> sortableContainerPropertyIds, List<String> columnNames, String messagePrefix) {
		super(vgw, dContainer, messageSource, sortableContainerPropertyIds, columnNames, messagePrefix);
		delayCreateContent();
	}

	@Override
	protected void addGeneratedProperty(GeneratedPropertyContainer gpcontainer, String name) {
	}
	
	@Override
	protected void setupColumn(Column col, String cn) {
		super.setupColumn(col, cn);
		if ("length".equals(cn)) {
			ColumnUtil.setFileLengthRender(col);
		}
	}

	@Override
	protected void setupGrid() {
		FooterRow footer = addFooterRowAt(0);
		FooterCell fc = footer.getCell("updatedAt");
		fc.setText("0");
		getdContainer().addItemSetChangeListener(event -> {
			fc.setText("" + event.getContainer().size());
		});
	}

}
