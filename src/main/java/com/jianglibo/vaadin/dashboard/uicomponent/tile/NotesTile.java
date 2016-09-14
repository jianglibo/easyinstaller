package com.jianglibo.vaadin.dashboard.uicomponent.tile;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class NotesTile extends TileBase {

	@Override
	protected Component getWrapedContent() {
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

}
