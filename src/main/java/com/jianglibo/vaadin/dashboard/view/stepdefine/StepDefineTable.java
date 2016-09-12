package com.jianglibo.vaadin.dashboard.view.stepdefine;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.StepDefine;
import com.jianglibo.vaadin.dashboard.event.view.PageMetaEvent;
import com.jianglibo.vaadin.dashboard.repositories.StepDefineRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.table.TableBase;
import com.vaadin.data.Property;

@SuppressWarnings("serial")
public class StepDefineTable extends TableBase<StepDefine> {
	
	public StepDefineTable(MessageSource messageSource, Domains domains,StepDefineContainer container, StepDefineRepository repository) {
		super(StepDefine.class, domains,container, messageSource);
		container.setEnableSort(true);
	}
	
	@Override
	public void setFooter(PageMetaEvent pme) {
		if (pme == null) {
			setColumnFooter("createdAt", "");
			setColumnFooter("ip", "Total");
		} else {
			setColumnFooter("createdAt", pme.getTotalRecordString());
			setColumnFooter("ip", "Total");
		}
	}
	
	@Override
	protected String formatPropertyValue(final Object rowId, final Object colId, final Property<?> property) {
		String result = super.formatPropertyValue(rowId, colId, property);
		if (colId.equals("createdAt")) {
			result = formatDate(DATEFORMAT, property);
		}
		return result;
	}
}
