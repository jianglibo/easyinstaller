package com.jianglibo.vaadin.dashboard.annotation;

public class VaadinFormFieldWrapper {

	private VaadinFormField vff;
	
	private String name;

	public VaadinFormFieldWrapper(VaadinFormField vff, String name) {
		super();
		this.setVff(vff);
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public VaadinFormField getVff() {
		return vff;
	}

	public void setVff(VaadinFormField vff) {
		this.vff = vff;
	}
}
