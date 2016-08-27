package com.jianglibo.vaadin.dashboard.annotation;

import java.lang.reflect.Field;

public class VaadinFormFieldWrapper {

	private VaadinFormField vff;
	
	private Field reflectField;

	public VaadinFormFieldWrapper(VaadinFormField vff, Field reflectField) {
		super();
		this.setVff(vff);
		this.reflectField = reflectField;
	}
	
	public String getName() {
		return reflectField.getName();
	}

	public VaadinFormField getVff() {
		return vff;
	}

	public void setVff(VaadinFormField vff) {
		this.vff = vff;
	}

	public Field getReflectField() {
		return reflectField;
	}
	
	
}
