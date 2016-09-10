package com.jianglibo.vaadin.dashboard.view.steprun;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.StepRun;
import com.jianglibo.vaadin.dashboard.repositories.StepRunRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.table.TableBase;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.view.ListView;
import com.vaadin.data.Property;

@SuppressWarnings("serial")
public class StepRunTable extends TableBase<StepRun> {
	

	public StepRunTable(MessageSource messageSource, Domains domains,StepRunContainer container, StepRunRepository repository, ListView listview) {
		super(StepRun.class, domains,container, messageSource);
		container.setEnableSort(true);
	}

	@Override
	public void setFooter() {
		setColumnFooter("createdAt", "");
		setColumnFooter("ip", "Total");
	}
	
	@Override
	protected String formatPropertyValue(final Object rowId, final Object colId, final Property<?> property) {
		String result = super.formatPropertyValue(rowId, colId, property);
		if (colId.equals("createdAt")) {
			result = formatDate(DATEFORMAT, property);
		}
		return result;
	}

	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}
}
