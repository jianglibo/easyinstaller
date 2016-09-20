package com.jianglibo.vaadin.dashboard.uicomponent.tile;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class HtmlContentTile extends TileBase {
	
	public HtmlContentTile(MessageSource messageSource, String messageId) {
		super(messageSource, messageId);
	}

	@Override
	public void setTileStyles() {
	}

	@Override
	protected Component getWrapedContent(MessageSource messageSource, String messageId) {
		Label lb = new Label();
		lb.setSizeFull();
		lb.setContentMode(ContentMode.HTML);
		lb.setValue(MsgUtil.getMsgFallbackToSelf(messageSource, "tiles.content.", messageId));
		StyleUtil.setMarginTen(lb);
		return lb;
	}

	@Override
	protected String getTileTitle(MessageSource messageSource, String messageId) {
		return MsgUtil.getMsgFallbackToSelf(messageSource, "tiles.title.", messageId);
	}

}
