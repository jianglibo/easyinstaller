package com.jianglibo.vaadin.dashboard.view.dashboard;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.uicomponent.tile.TileBase;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class NewNewsTile extends TileBase {
	
	private NewNewsTable newNewTable;

	public NewNewsTile(MessageSource messageSource, String messageId) {
		super(messageSource, messageId);
	}

	@Override
	public void setTileStyles() {
	}

	@Override
	protected Component getWrapedContent(MessageSource messageSource, String messageId) {
		setNewNewTable(new NewNewsTable(null)); 
		return getNewNewTable();
	}

	public NewNewsTable getNewNewTable() {
		return newNewTable;
	}

	public void setNewNewTable(NewNewsTable newNewTable) {
		this.newNewTable = newNewTable;
	}
	
}
