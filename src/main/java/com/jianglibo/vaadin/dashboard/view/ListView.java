package com.jianglibo.vaadin.dashboard.view;

import com.jianglibo.vaadin.dashboard.uicomponent.dynmenu.ButtonDescription;

public interface ListView extends FilterListener, ContainerSortListener, PageMetaEventListener {
	
	void trashBtnClicked(boolean b);
	
	void gotoPage(int p);
	
	void onDynButtonClicked(ButtonDescription btnDesc);
	
	void backward();

}
