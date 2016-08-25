package com.jianglibo.vaadin.dashboard.annotation;

public class VaadinTableWrapper {
	
	private VaadinTable vt;
	
	private String name;

	public VaadinTableWrapper(VaadinTable vt, String name) {
		super();
		this.vt = vt;
		this.name = name;
	}

	public VaadinTable getVt() {
		return vt;
	}

	public void setVt(VaadinTable vt) {
		this.vt = vt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
