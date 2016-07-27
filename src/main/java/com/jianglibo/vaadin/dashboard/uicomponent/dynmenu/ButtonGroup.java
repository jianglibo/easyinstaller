package com.jianglibo.vaadin.dashboard.uicomponent.dynmenu;

import java.util.List;

import com.google.gwt.thirdparty.guava.common.collect.Lists;

public class ButtonGroup {

	private List<ButtonDescription> buttons;
	
	public ButtonGroup(ButtonDescription...btnDescriptions) {
		setButtons(Lists.newArrayList(btnDescriptions));
	}

	public List<ButtonDescription> getButtons() {
		return buttons;
	}

	public void setButtons(List<ButtonDescription> buttons) {
		this.buttons = buttons;
	}
}
