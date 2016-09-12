package com.jianglibo.vaadin.dashboard.view.pksource;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.PkSource;
import com.jianglibo.vaadin.dashboard.event.view.PageMetaEvent;
import com.jianglibo.vaadin.dashboard.formatter.FileLengthFormat;
import com.jianglibo.vaadin.dashboard.repositories.PkSourceRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.table.TableBase;
import com.vaadin.data.Property;

@SuppressWarnings("serial")
public class PkSourceTable extends TableBase<PkSource> {
	
	public PkSourceTable(MessageSource messageSource, Domains domains,PkSourceContainer container, PkSourceRepository repository) {
		super(PkSource.class, domains,container, messageSource);
		container.setEnableSort(true);
	}


	@Override
	public void setFooter(PageMetaEvent pme) {
		if (pme == null) {
			setColumnFooter("createdAt", "");
		} else {
			setColumnFooter("createdAt", pme.getTotalRecordString());
		}
		
	}
	
	@Override
	protected String formatPropertyValue(Object rowId, Object colId, Property<?> property) {
		String result =  super.formatPropertyValue(rowId, colId, property);
		if (colId.equals("createdAt")) {
			result = formatDate(DATEFORMAT, property);
		} else if (colId.equals("length")){
			return FileLengthFormat.format((Long) property.getValue());
		}
		return result;
	}
}
