package com.jianglibo.vaadin.dashboard.annotation;

import java.util.Collection;
import java.util.List;

import org.springframework.context.MessageSource;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.vaadin.ui.UI;

public class VaadinTableColumns {

	private final Collection<VaadinTableColumnWrapper> columns;
	
	public VaadinTableColumns(Collection<VaadinTableColumnWrapper> columns) {
		this.columns = columns;
	}
	
	public String[] getVisibleColumns() {
		List<String> visibles = Lists.newArrayList();
		for(VaadinTableColumnWrapper tc : columns) {
			if (tc.getVtc().visible()) {
				visibles.add(tc.getName());
			}
		}
		return visibles.toArray(new String[]{});
	}
	
	public String[] getAutoCollapseColumns() {
		List<String> visibles = Lists.newArrayList();
		for(VaadinTableColumnWrapper tc : columns) {
			if (tc.getVtc().autoCollapsed()) {
				visibles.add(tc.getName());
			}
		}
		return visibles.toArray(new String[]{});
	}

	public Collection<VaadinTableColumnWrapper> getColumns() {
		return columns;
	}
	
	public String[] getColumnHeaders(MessageSource messageSource) {
		List<String> headers = Lists.newArrayList();
		for(String s : getVisibleColumns()) {
			headers.add(messageSource.getMessage("table.box.column." + s, null, UI.getCurrent().getLocale()));
		}
		return headers.toArray(new String[]{});
	}

}
