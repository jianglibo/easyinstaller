package com.jianglibo.vaadin.dashboard.util;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumnWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumns;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.vaadin.server.Page;
import com.vaadin.ui.Table;

public class TableUtil {
	
	public static void decorateTable(Table table, MessageSource messageSource, VaadinTable vt, VaadinTableColumns tableColumns) {
		if (vt.fullSize()) {
			table.setSizeFull();
		}
		
		table.setSortEnabled(vt.sortable());
		
		for(String sn : vt.styleNames()) {
			table.addStyleName(sn);
		}
		table.setSelectable(vt.selectable());
		
		table.setColumnReorderingAllowed(vt.columnCollapsingAllowed());

		table.setColumnCollapsingAllowed(vt.columnCollapsingAllowed());
		
		table.setFooterVisible(vt.footerVisible());
		table.setMultiSelect(vt.multiSelect());
		
		for(VaadinTableColumnWrapper tcw: tableColumns.getColumns()) {
			table.setColumnCollapsible(tcw.getName(), tcw.getVtc().collapsible());
			table.setColumnAlignment(tcw.getName(), tcw.getVtc().alignment());
		}
		
		table.setVisibleColumns(tableColumns.getVisibleColumns());
		table.setColumnHeaders(tableColumns.getColumnHeaders(vt, messageSource));
	}
	
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
