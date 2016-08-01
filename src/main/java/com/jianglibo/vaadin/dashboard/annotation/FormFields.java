package com.jianglibo.vaadin.dashboard.annotation;

import java.util.List;
import java.util.SortedMap;

import org.springframework.context.MessageSource;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.vaadin.ui.UI;

public class FormFields {

	private final SortedMap<Integer, VaadinFormField> vfs;
	
	public FormFields(SortedMap<Integer, VaadinFormField> vfs) {
		this.vfs = vfs;
	}
	
	public SortedMap<Integer, VaadinFormField> getVfs() {
		return vfs;
	}
	
//	public String[] getColumnHeaders(MessageSource messageSource) {
//		List<String> headers = Lists.newArrayList();
//		for(String s : getVisibleColumns()) {
//			headers.add(messageSource.getMessage("table.box.column." + s, null, UI.getCurrent().getLocale()));
//		}
//		return headers.toArray(new String[]{});
//	}

}
