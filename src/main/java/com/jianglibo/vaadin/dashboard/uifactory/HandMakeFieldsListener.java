package com.jianglibo.vaadin.dashboard.uifactory;

import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.vaadin.ui.Field;

public interface HandMakeFieldsListener {
	Field<?> createField(VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw);
}
