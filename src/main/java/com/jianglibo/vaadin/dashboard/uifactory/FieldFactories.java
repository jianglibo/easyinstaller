package com.jianglibo.vaadin.dashboard.uifactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FieldFactories {

	private final ComboBoxFieldFactory comboBoxFieldFactory;
	private final TwinColSelectFieldFactory twinColSelectFieldFactory;

	@Autowired
	public FieldFactories(ComboBoxFieldFactory comboBoxFieldFactory,
			TwinColSelectFieldFactory twinColSelectFieldFactory) {
		super();
		this.comboBoxFieldFactory = comboBoxFieldFactory;
		this.twinColSelectFieldFactory = twinColSelectFieldFactory;
	}

	public ComboBoxFieldFactory getComboBoxFieldFactory() {
		return comboBoxFieldFactory;
	}

	public TwinColSelectFieldFactory getTwinColSelectFieldFactory() {
		return twinColSelectFieldFactory;
	}
}
