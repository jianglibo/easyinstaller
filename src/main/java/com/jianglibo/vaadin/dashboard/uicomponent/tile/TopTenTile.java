package com.jianglibo.vaadin.dashboard.uicomponent.tile;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.view.dashboard.TopTenMoviesTable;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class TopTenTile extends TileBase {

	public TopTenTile(MessageSource messageSource, String messageId) {
		super(messageSource, messageId);
	}

	@Override
	public void setTileStyles() {
		addStyleName("top10-revenue");
	}

	@Override
	protected Component getWrapedContent(MessageSource messageSource, String messageId) {
		return new TopTenMoviesTable();
	}

	@Override
	protected String getTileTitle(MessageSource messageSource, String messageId) {
		return "TOP 10 TITLES BY REVENUE";
	}

}
