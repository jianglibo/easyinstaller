package com.jianglibo.vaadin.dashboard.annotation;

import java.util.List;
import java.util.SortedMap;

import org.springframework.context.MessageSource;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.vaadin.ui.UI;

public class TableColumns {

	private final SortedMap<Integer, TableColumn> tcmap;
	
	public TableColumns(SortedMap<Integer, TableColumn> tcmap) {
		this.tcmap = tcmap;
	}
	
	public String[] getVisibleColumns() {
		List<String> visibles = Lists.newArrayList();
		for(TableColumn tc : tcmap.values()) {
			if (tc.visible()) {
				visibles.add(tc.name());
			}
		}
		return visibles.toArray(new String[]{});
	}

	public SortedMap<Integer, TableColumn> getTcmap() {
		return tcmap;
	}
	
	public String[] getColumnHeaders(MessageSource messageSource) {
		List<String> headers = Lists.newArrayList();
		for(String s : getVisibleColumns()) {
			headers.add(messageSource.getMessage("table.box.column." + s, null, UI.getCurrent().getLocale()));
		}
		return headers.toArray(new String[]{});
	}

}
