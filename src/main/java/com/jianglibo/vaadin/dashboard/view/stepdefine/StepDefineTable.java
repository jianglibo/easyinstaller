package com.jianglibo.vaadin.dashboard.view.stepdefine;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.StepDefine;
import com.jianglibo.vaadin.dashboard.repositories.StepDefineRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.table.TableBase;
import com.jianglibo.vaadin.dashboard.view.ListView;
import com.vaadin.data.Property;

@SuppressWarnings("serial")
public class StepDefineTable extends TableBase<StepDefine> {
	
	private StepDefineContainer container;

	public StepDefineTable(MessageSource messageSource, Domains domains,StepDefineRepository repository, ListView listview) {
		super(StepDefine.class, domains, messageSource);
		container = new StepDefineContainer(repository, domains, listview, this);
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
