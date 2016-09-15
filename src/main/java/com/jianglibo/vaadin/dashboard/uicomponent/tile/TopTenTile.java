package com.jianglibo.vaadin.dashboard.uicomponent.tile;

import com.jianglibo.vaadin.dashboard.view.dashboard.TopTenMoviesTable;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class TopTenTile extends TileBase {

	@Override
	public void setTileStyles() {
		addStyleName("top10-revenue");
	}

	@Override
	protected Component getWrapedContent() {
		return new TopTenMoviesTable();
	}

	@Override
	protected String getTileTitle() {
		return "TOP 10 TITLES BY REVENUE";
	}

}
