package com.jianglibo.vaadin.dashboard.vo;

import java.util.List;

public class PreDefinedSoftware {
	private String name;
	private String ostype;
	
	private List<NameOstype> nameOstypes;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOstype() {
		return ostype;
	}

	public void setOstype(String ostype) {
		this.ostype = ostype;
	}

	public List<NameOstype> getNameOstypes() {
		return nameOstypes;
	}

	public void setNameOstypes(List<NameOstype> nameOstypes) {
		this.nameOstypes = nameOstypes;
	}
}
