package com.jianglibo.vaadin.dashboard.uicomponent.twingrid;

import com.vaadin.ui.HorizontalLayout;

@SuppressWarnings("serial")
public class TwinGridLayout extends HorizontalLayout {

	public TwinGridLayout() {
		TwinGridLeft tgl = new TwinGridLeft();
		TwinGridRight tgr = new TwinGridRight();
		
		addComponent(tgl);
		addComponent(tgr);
		
		setExpandRatio(tgl, 1);
		setExpandRatio(tgr, 1);
		
	}
}
