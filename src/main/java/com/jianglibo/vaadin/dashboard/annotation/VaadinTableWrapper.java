package com.jianglibo.vaadin.dashboard.annotation;

public class VaadinTableWrapper {
	
	private VaadinTable vt;
	
	private Class<?> clazz;

	public VaadinTableWrapper(VaadinTable vt, Class<?> clazz) {
		super();
		this.vt = vt;
		this.clazz = clazz;
	}

	public VaadinTable getVt() {
		return vt;
	}

	public void setVt(VaadinTable vt) {
		this.vt = vt;
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
