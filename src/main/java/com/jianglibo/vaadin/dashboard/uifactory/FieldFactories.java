package com.jianglibo.vaadin.dashboard.uifactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FieldFactories {

	private final ComboBoxFieldFactory comboBoxFieldFactory;
	private final GridFieldFactory gridFieldFactory;
	private final TwinColSelectFieldFactory twinColSelectFieldFactory;
	private final TwinGridFieldFactory twinGridFieldFactory;
	
	@Autowired
	public FieldFactories(ComboBoxFieldFactory comboBoxFieldFactory, GridFieldFactory gridFieldFactory,
			TwinColSelectFieldFactory twinColSelectFieldFactory, TwinGridFieldFactory twinGridFieldFactory) {
		super();
		this.comboBoxFieldFactory = comboBoxFieldFactory;
		this.gridFieldFactory = gridFieldFactory;
		this.twinColSelectFieldFactory = twinColSelectFieldFactory;
		this.twinGridFieldFactory = twinGridFieldFactory;
	}

	public ComboBoxFieldFactory getComboBoxFieldFactory() {
		return comboBoxFieldFactory;
	}


	public GridFieldFactory getGridFieldFactory() {
		return gridFieldFactory;
	}


	public TwinColSelectFieldFactory getTwinColSelectFieldFactory() {
		return twinColSelectFieldFactory;
	}


	public TwinGridFieldFactory getTwinGridFieldFactory() {
		return twinGridFieldFactory;
	}
}
