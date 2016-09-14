package com.jianglibo.vaadin.dashboard.uicomponent.tile;

import java.util.List;

import com.google.common.collect.Lists;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ValoTheme;

public abstract class TileBase extends CssLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<TileMenuClickListener> tmcls = Lists.newArrayList();

	public TileBase() {
		setWidth("100%");
		addStyleName("dashboard-panel-slot");
		setTileStyles();
		CssLayout card = new CssLayout();
		card.setWidth("100%");
		card.addStyleName(ValoTheme.LAYOUT_CARD);

		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.addStyleName("dashboard-panel-toolbar");
		toolbar.setWidth("100%");
		
		Component content = getWrapedContent();

		Label caption = new Label(content.getCaption());
		caption.addStyleName(ValoTheme.LABEL_H4);
		caption.addStyleName(ValoTheme.LABEL_COLORED);
		caption.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		content.setCaption(null);

		MenuBar tools = new MenuBar();
		tools.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
		MenuItem max = tools.addItem("", FontAwesome.EXPAND, new Command() {

			@Override
			public void menuSelected(final MenuItem selectedItem) {
				if (!getStyleName().contains("max")) {
					selectedItem.setIcon(FontAwesome.COMPRESS);
					tmcls.stream().forEach(tmcl -> tmcl.menuClicked(TileBase.this, selectedItem, true));
				} else {
					removeStyleName("max");
					selectedItem.setIcon(FontAwesome.EXPAND);
					tmcls.stream().forEach(tmcl -> tmcl.menuClicked(TileBase.this, selectedItem, false));
				}
			}
		});
		max.setStyleName("icon-only");
		MenuItem root = tools.addItem("", FontAwesome.COG, null);
		root.addItem("Configure", new Command() {
			@Override
			public void menuSelected(final MenuItem selectedItem) {
				Notification.show("Not implemented in this demo");
			}
		});
		root.addSeparator();
		root.addItem("Close", new Command() {
			@Override
			public void menuSelected(final MenuItem selectedItem) {
				Notification.show("Not implemented in this demo");
			}
		});

		toolbar.addComponents(caption, tools);
		toolbar.setExpandRatio(caption, 1);
		toolbar.setComponentAlignment(caption, Alignment.MIDDLE_LEFT);

		card.addComponents(toolbar, content);
		addComponent(card);
	}
	
	public abstract void setTileStyles();
	
	public void AddTileMenuClickListener(TileMenuClickListener tmcl) {
		this.tmcls.add(tmcl);
	}
	
	protected abstract Component getWrapedContent();

	public static interface TileMenuClickListener {
		void menuClicked(TileBase tb, MenuItem mi, boolean b);
	}

}
