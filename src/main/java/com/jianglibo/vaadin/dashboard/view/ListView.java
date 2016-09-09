package com.jianglibo.vaadin.dashboard.view;


import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription;
import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.DynButtonComponent;
import com.jianglibo.vaadin.dashboard.uicomponent.pager.Pager;

public interface ListView extends FilterListener {

	DynButtonComponent getDynButtonComponent();
	
	void onDynButtonClicked(ButtonDescription btnDesc);
	
	Pager getPager();
	
	void trashBtnClicked(boolean b);
	
	void gotoPage(int p);

}
