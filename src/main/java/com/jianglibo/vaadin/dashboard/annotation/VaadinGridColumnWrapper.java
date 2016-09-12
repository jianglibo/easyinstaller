package com.jianglibo.vaadin.dashboard.annotation;

import java.lang.reflect.Field;

public class VaadinGridColumnWrapper {

	private VaadinGridColumn vgc;
	
	private Field reflectField;

	public VaadinGridColumnWrapper(VaadinGridColumn vgc,Field reflectField) {
		super();
		this.setVgc(vgc);
		this.reflectField = reflectField;
	}

	public String getName() {
		return reflectField.getName();
	}

	public Field getReflectField() {
		return reflectField;
	}

	public void setReflectField(Field reflectField) {
		this.reflectField = reflectField;
	}

	public VaadinGridColumn getVgc() {
		return vgc;
	}

	public void setVgc(VaadinGridColumn vgc) {
		this.vgc = vgc;
	};
}
