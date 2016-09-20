package com.jianglibo.vaadin.dashboard.view.dashboard;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.domain.BoxHistory;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.uicomponent.tile.TileBase;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class BoxHistoryTile extends TileBase {
	
	private final Domains domains;

	public BoxHistoryTile(Domains domains, MessageSource messageSource, String messageId) {
		super(messageSource, messageId);
		this.domains = domains;
	}

	@Override
	public void setTileStyles() {
	}

	@Override
	protected Component getWrapedContent(MessageSource messageSource, String messageId) {
		BoxHistoryGrid bhg = new BoxHistoryGrid(messageSource, domains, BoxHistory.class);
		return bhg;
	}

}
