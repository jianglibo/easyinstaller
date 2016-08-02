package com.jianglibo.vaadin.dashboard.annotation;

import java.util.Collection;

public class FormFields {

	private final Collection<VaadinFormFieldWrapper> fields;
	
	public FormFields(Collection<VaadinFormFieldWrapper> fields) {
		this.fields = fields;
	}
	
	public Collection<VaadinFormFieldWrapper> getFields() {
		return fields;
	}
}
