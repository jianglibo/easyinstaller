package com.jianglibo.vaadin.dashboard.uicomponent.tile;

import org.springframework.context.MessageSource;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class NotesTile extends TileBase {

	public NotesTile(MessageSource messageSource, String messageId) {
		super(messageSource, messageId);
	}

	@Override
	protected Component getWrapedContent(MessageSource messageSource, String messageId) {
        TextArea notes = new TextArea("Notes");
        notes.setValue("Remember to:\n· Zoom in and out in the Sales view\n· Filter the transactions and drag a set of them to the Reports tab\n· Create a new report\n· Change the schedule of the movie theater");
        notes.setSizeFull();
        notes.addStyleName(ValoTheme.TEXTAREA_BORDERLESS);
        return notes;
	}

	@Override
	public void setTileStyles() {
		addStyleName("notes");
	}

	@Override
	protected String getTileTitle(MessageSource messageSource, String messageId) {
		return "Notes";
	}

}
