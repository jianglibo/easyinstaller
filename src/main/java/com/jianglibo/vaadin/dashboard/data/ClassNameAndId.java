package com.jianglibo.vaadin.dashboard.data;

import com.google.common.base.Objects;

public class ClassNameAndId {

	private String className;
	
	private Long id;

	public ClassNameAndId(String className, Long id) {
		super();
		this.className = className;
		this.id = id;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("SimpleClassName", className).add("EntityId", id).toString();
	}
	
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		ClassNameAndId other = (ClassNameAndId) obj;
		return this.getClassName().equals(other.getClassName()) && this.getId().equals(other.getId());
	}	
}
