package com.jianglibo.vaadin.dashboard.unused;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.event.view.PageMetaEvent;
import com.jianglibo.vaadin.dashboard.uicomponent.table.TableBase;
import com.vaadin.data.Property;

@SuppressWarnings("serial")
public class InstallTable extends TableBase<Install> {
	
	public InstallTable(MessageSource messageSource, Domains domains,InstallContainer container, InstallRepository repository) {
		super(Install.class, domains,container, messageSource);
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
