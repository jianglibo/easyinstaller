package com.jianglibo.vaadin.dashboard.uicomponent.dynmenu;

import java.util.List;

import com.google.gwt.thirdparty.guava.common.collect.Lists;

public class ButtonGroups {

	private List<ButtonDescription> buttons;
	
	public ButtonGroups(ButtonDescription...btnDescriptions) {
		setButtons(Lists.newArrayList(btnDescriptions));
	}

	public List<ButtonDescription> getButtons() {
		return buttons;
	}

	public void setButtons(List<ButtonDescription> buttons) {
		this.buttons = buttons;
	}
}
