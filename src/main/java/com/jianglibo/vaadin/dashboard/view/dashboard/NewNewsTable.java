package com.jianglibo.vaadin.dashboard.view.dashboard;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.service.HttpPageGetter.NewNew;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Link;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public final class NewNewsTable extends Table {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NewNewsTable.class);

	public NewNewsTable(List<NewNew> news) {
		setCaption("Develope News");
		addStyleName(ValoTheme.TABLE_BORDERLESS);
		addStyleName(ValoTheme.TABLE_NO_STRIPES);
		addStyleName(ValoTheme.TABLE_NO_VERTICAL_LINES);
		addStyleName(ValoTheme.TABLE_SMALL);
		setSortEnabled(false);

		setRowHeaderMode(RowHeaderMode.INDEX);
		setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
		setSizeFull();

		addContainerProperty("title", Link.class, null);
		addContainerProperty("datetime", String.class, null);
		setColumnAlignment("datetime", Align.RIGHT);
		
		addNews(news == null ? Lists.newArrayList() : news);
	}
	
	public void addNews(List<NewNew> news) {
		removeAllItems();
		int i = 0;
		for(NewNew n: news) {
			LOGGER.info(n.toString());
			Link link = new Link(n.getTitle(), new ExternalResource(n.getUrl()));
			link.setTargetName("_blank");
			addItem(new Object[] { link, n.getDatetime()}, i++);
		}
	}

}
