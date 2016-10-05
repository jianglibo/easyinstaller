package com.jianglibo.vaadin.dashboard.view.dashboard;

import java.util.List;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.service.HttpPageGetter.NewNew;
import com.jianglibo.vaadin.dashboard.uicomponent.tile.TileBase;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class NewNewsTile extends TileBase {
	
	private NewNewsTable newNewTable;
	
	private List<NewNew> news;

	public NewNewsTile(List<NewNew> news, MessageSource messageSource, String messageId) {
		super(messageSource, messageId);
		this.news = news;
	}

	@Override
	public void setTileStyles() {
	}

	@Override
	protected Component getWrapedContent(MessageSource messageSource, String messageId) {
		setNewNewTable(new NewNewsTable(news)); 
		return getNewNewTable();
	}

	public NewNewsTable getNewNewTable() {
		return newNewTable;
	}

	public void setNewNewTable(NewNewsTable newNewTable) {
		this.newNewTable = newNewTable;
	}
	
}
