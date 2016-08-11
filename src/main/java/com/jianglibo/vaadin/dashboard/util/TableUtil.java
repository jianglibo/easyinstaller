package com.jianglibo.vaadin.dashboard.util;


import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumns;
import com.vaadin.server.Page;
import com.vaadin.ui.Table;

public class TableUtil {

	
	public static boolean autoCollapseColumnsNeedChangeState(Table table, VaadinTableColumns tc) {
		boolean result = true;
		for (String propertyId : tc.getAutoCollapseColumns()) {
			if (table.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
				result = false;
			}
		}
		return result;
	}
}
