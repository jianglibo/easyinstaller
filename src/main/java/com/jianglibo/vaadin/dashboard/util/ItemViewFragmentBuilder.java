package com.jianglibo.vaadin.dashboard.util;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class ItemViewFragmentBuilder extends ViewFragmentBuilder {
	
	
	

	
	public ItemViewFragmentBuilder(String pstr, String viewName) {
		super(pstr, viewName);
	}
	
	public ItemViewFragmentBuilder(ViewChangeEvent vce) {
		super(vce.getParameters(), vce.getViewName());
	}

	public long getBeanId() {
		String idpart = getUriComs().getPath();
		if (idpart == null) {
			return 0;
		}
		String idstrs[] = idpart.split("/");
		String idstr = idstrs[idstrs.length - 1];
		
		try {
			return Long.valueOf(idstr);
		} catch (NumberFormatException e) {
			return 0;
		}
		
	}

}
