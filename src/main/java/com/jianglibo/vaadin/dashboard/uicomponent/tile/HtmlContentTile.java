package com.jianglibo.vaadin.dashboard.uicomponent.tile;

import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class HtmlContentTile extends TileBase {

	@Override
	public void setTileStyles() {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	protected Component getWrapedContent() {
		Label lb = new Label();
		lb.setSizeFull();
		lb.setContentMode(ContentMode.HTML);
		lb.setValue("<h3><a href=\"https://github.com\" target=\"_blank\">github</a></h3><br/><ul><li>aaaaaaaa</li><li>bbbbbbbbbb</li></ul>");
		StyleUtil.setMarginTen(lb);
		return lb;
	}

	@Override
	protected String getTileTitle() {
		return "Html Content";
	}

}
