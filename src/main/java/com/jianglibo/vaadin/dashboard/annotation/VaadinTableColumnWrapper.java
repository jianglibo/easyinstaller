package com.jianglibo.vaadin.dashboard.annotation;

import java.lang.reflect.Field;

public class VaadinTableColumnWrapper {

	private VaadinTableColumn vtc;
	
	private Field reflectField;

	public VaadinTableColumnWrapper(VaadinTableColumn vtc,Field reflectField) {
		super();
		this.vtc = vtc;
		this.reflectField = reflectField;
	}

	public VaadinTableColumn getVtc() {
		return vtc;
	}

	public void setVtc(VaadinTableColumn vtc) {
		this.vtc = vtc;
	}

	public String getName() {
		return reflectField.getName();
	}

	public Field getReflectField() {
		return reflectField;
	}

	public void setReflectField(Field reflectField) {
		this.reflectField = reflectField;
	};
}
