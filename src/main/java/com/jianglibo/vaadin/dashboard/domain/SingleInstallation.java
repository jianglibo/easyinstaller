package com.jianglibo.vaadin.dashboard.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumn;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@Entity
@Table(name = "singleInstallation")
@VaadinTable(name = SingleInstallation.DOMAIN_NAME,multiSelect=true, messagePrefix="domain.singleinstallaion.", styleNames={ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES, ValoTheme.TABLE_COMPACT}, selectable=true, fullSize=true)
public class SingleInstallation extends BaseEntity {

	public static final String DOMAIN_NAME = "singleinstallation";
	
	@ManyToOne
	@VaadinTableColumn
	private Software software;
	
	@ManyToMany
	private Set<Box> boxes;
	
	@VaadinTableColumn
	private boolean success;
	
	@Lob
	@Column(length=64000)
	private String config;

	public Software getSoftware() {
		return software;
	}

	public void setSoftware(Software software) {
		this.software = software;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public Set<Box> getBoxes() {
		return boxes;
	}

	public void setBoxes(Set<Box> boxes) {
		this.boxes = boxes;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
