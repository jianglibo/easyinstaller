package com.jianglibo.vaadin.dashboard.annotation;

public class VaadinTableColumnWrapper {

	private VaadinTableColumn vtc;
	
	private String name;

	public VaadinTableColumnWrapper(VaadinTableColumn vtc, String name) {
		super();
		this.vtc = vtc;
		this.name = name;
	}

	public VaadinTableColumn getVtc() {
		return vtc;
	}

	public void setVtc(VaadinTableColumn vtc) {
		this.vtc = vtc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	};
}
