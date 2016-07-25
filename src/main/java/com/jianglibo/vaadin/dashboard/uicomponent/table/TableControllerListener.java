package com.jianglibo.vaadin.dashboard.uicomponent.table;

import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.DynMenuListener;
import com.jianglibo.vaadin.dashboard.uicomponent.pager.PagerListener;

public interface TableControllerListener extends DynMenuListener, PagerListener{
	void checkBoxChanged(String cbName, boolean state);
}
