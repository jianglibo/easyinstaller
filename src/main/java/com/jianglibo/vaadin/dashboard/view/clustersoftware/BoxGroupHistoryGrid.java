package com.jianglibo.vaadin.dashboard.view.clustersoftware;

import org.springframework.context.MessageSource;

import com.google.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.domain.BoxGroupHistory;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.uicomponent.grid.BaseGridFree;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class BoxGroupHistoryGrid  extends BaseGridFree<BoxGroupHistory, BoxGroupHistoryContainer>{


	public BoxGroupHistoryGrid(MessageSource messageSource, Domains domains) {
		super(messageSource, domains, BoxGroupHistory.class, new String[]{"software", "success", "createdAt"});
		delayCreateContent();
	}

	@Override
	protected void setSummaryFooterCells(FooterRow footer) {
		
	}

	@Override
	protected void addGeneratedProperty(GeneratedPropertyContainer gpcontainer, String name) {
		
	}

	@Override
	protected void setupColumn(Column col, String cn) {
		
	}

	@Override
	protected void setColumnFiltering(Column col, String cn) {
		
	}

	@Override
	protected BoxGroupHistoryContainer createContainer() {
		return new BoxGroupHistoryContainer(getDomains(), 10, Lists.newArrayList());
	}

	@Override
	protected void setColumnFiltering() {
		
	}

	@Override
	protected void setupGrid() {
		HeaderRow groupingHeader = prependHeaderRow();
		HeaderCell namesCell = groupingHeader.join(
			    groupingHeader.getCell("software"),
			    groupingHeader.getCell("success"),
			    groupingHeader.getCell("createdAt"));
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSizeFull();
		Label lb = new Label(MsgUtil.getMsgWithSubsReturnKeyOnAbsent(getMessageSource(), "view.clustersoftware.history.redo"));
		lb.setWidthUndefined();
		StyleUtil.setMarginRightTwenty(lb);
		ComboBox cb = new ComboBox();
		cb.setSizeFull();
		cb.setEnabled(false);
		hl.addComponents(lb, cb);
		StyleUtil.setMarginBottomTwenty(hl);
		namesCell.setComponent(hl);
		hl.setExpandRatio(cb, 1);

		setSelectionMode(SelectionMode.SINGLE);
		
		cb.addValueChangeListener(event -> {
			event.getProperty().getValue();
		});
		
		addSelectionListener(event -> {
			if (event.getSelected().size() == 1) {
				cb.setEnabled(true);
			} else {
				cb.setEnabled(false);
			}
		});
	}

}
