package com.jianglibo.vaadin.dashboard.uicomponent.pager;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class Pager extends HorizontalLayout {
	
	public Pager() {
		MarginInfo mf = new MarginInfo(true, true, true, false);
		setMargin(mf);
		addStyleName("pager");
		Button left = new Button("");
		left.addStyleName(ValoTheme.BUTTON_LINK);
		left.addStyleName(ValoTheme.BUTTON_SMALL);
		left.setIcon(FontAwesome.ARROW_LEFT);
		Button right = new Button("");
		
		right.setIcon(FontAwesome.ARROW_RIGHT);
		right.addStyleName(ValoTheme.BUTTON_LINK);
		right.addStyleName(ValoTheme.BUTTON_SMALL);
		

		Label label = new Label("第3页，一共99页");
		addComponents(left, label, right);

	}
}
