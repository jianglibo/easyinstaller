package com.jianglibo.vaadin.dashboard.domain;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@SuppressWarnings("serial")
@Entity
@Table(name = "blobindb", uniqueConstraints = { @UniqueConstraint(columnNames = "name") })
public class BlobObjectInDb extends BaseEntity {
	
	private String name;
	
	@Lob
	private String blob;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBlob() {
		return blob;
	}

	public void setBlob(String blob) {
		this.blob = blob;
	}

	@Override
	public String getDisplayName() {
		return "blob:" + getName();
	}
	
	
}
