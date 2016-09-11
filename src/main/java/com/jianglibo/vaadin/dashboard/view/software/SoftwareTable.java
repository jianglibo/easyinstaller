package com.jianglibo.vaadin.dashboard.view.software;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.event.view.PageMetaEvent;
import com.jianglibo.vaadin.dashboard.repositories.SoftwareRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.table.TableBase;
import com.vaadin.data.Property;

@SuppressWarnings("serial")
public class SoftwareTable extends TableBase<Software> {
	
	public SoftwareTable(MessageSource messageSource, Domains domains,SoftwareContainer container, SoftwareRepository repository) {
		super(Software.class, domains,container, messageSource);
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

	@Override
	public void refresh() {
	}
}
