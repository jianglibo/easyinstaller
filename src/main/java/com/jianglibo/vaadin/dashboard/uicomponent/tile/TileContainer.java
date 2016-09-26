package com.jianglibo.vaadin.dashboard.uicomponent.tile;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.uicomponent.tile.TileBase.TileMenuClickListener;
import com.vaadin.server.Responsive;
import com.vaadin.ui.CssLayout;

@SuppressWarnings("serial")
public class TileContainer extends CssLayout {

	private List<TileBase> tbs = Lists.newArrayList();
	
	private TileMenuClickListener tmcl;

	public TileContainer(TileBase... tbs) {
		this.tbs = Lists.newArrayList(tbs);
		addStyleName("dashboard-panels");
		Responsive.makeResponsive(this);
		addComponents(tbs);
	}
	
	public  void addTile(TileBase tb) {
		this.tbs.add(tb);
		tb.addTileMenuClickListener(tmcl);
		addComponent(tb);
	}
	
	public Optional<TileBase> findTile(Class<?> clazz) {
		return tbs.stream().filter(tb -> clazz.isAssignableFrom(tb.getClass())).findAny();
	}
	
	public void AddTileMenuClickListener(TileMenuClickListener tmcl) {
		this.tmcl = tmcl;
		this.tbs.forEach(tb -> tb.addTileMenuClickListener(tmcl));
	}
}
