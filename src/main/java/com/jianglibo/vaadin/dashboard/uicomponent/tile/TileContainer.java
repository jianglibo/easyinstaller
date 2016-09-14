package com.jianglibo.vaadin.dashboard.uicomponent.tile;

import java.util.List;

import com.google.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.uicomponent.tile.TileBase.TileMenuClickListener;
import com.vaadin.server.Responsive;
import com.vaadin.ui.CssLayout;

@SuppressWarnings("serial")
public class TileContainer extends CssLayout {

	private List<? extends TileBase> tbs;

	public TileContainer(TileBase... tbs) {
		this.tbs = Lists.newArrayList(tbs);
		addStyleName("dashboard-panels");
		Responsive.makeResponsive(this);
		addComponents(tbs);
	}
	
	public void AddTileMenuClickListener(TileMenuClickListener tmcl) {
		this.tbs.forEach(tb -> tb.AddTileMenuClickListener(tmcl));
	}
}
