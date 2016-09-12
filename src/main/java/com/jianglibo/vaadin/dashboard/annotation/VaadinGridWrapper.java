package com.jianglibo.vaadin.dashboard.annotation;

import java.util.List;
import java.util.stream.Collectors;

public class VaadinGridWrapper {
	
	private VaadinGrid vg;
	
	private Class<?> clazz;
	
	private List<VaadinGridColumnWrapper> columns;
	
	private List<VaadinFormFieldWrapper> formFields;

	public VaadinGridWrapper(VaadinGrid vg, Class<?> clazz) {
		super();
		this.vg = vg;
		this.clazz = clazz;
	}
	
	
	
	public List<VaadinFormFieldWrapper> getFormFields() {
		return formFields;
	}



	public void setFormFields(List<VaadinFormFieldWrapper> formFields) {
		this.formFields = formFields;
	}



	public List<String> getSortableColumnNames() {
		return columns.stream().filter(vgcw -> vgcw.getVgc().sortable()).map(vgcw -> vgcw.getName()).collect(Collectors.toList());
	}
	
	public List<VaadinGridColumnWrapper> getColumns() {
		return columns;
	}

	public void setColumns(List<VaadinGridColumnWrapper> columns) {
		this.columns = columns;
	}

	public VaadinGrid getVg() {
		return vg;
	}


	public void setVg(VaadinGrid vg) {
		this.vg = vg;
	}


	public String getName() {
		return clazz.getSimpleName();
	}


	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
}
