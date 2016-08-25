package com.jianglibo.vaadin.dashboard.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField.Ft;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumn;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@Entity
@Table(name = "singleInstallation")
@VaadinTable(multiSelect=true,sortable=true, messagePrefix="domain.singleinstallaion.", styleNames={ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES, ValoTheme.TABLE_COMPACT}, selectable=true, fullSize=true)
public class SingleInstallation extends BaseEntity {

	@ManyToOne
	@VaadinTableColumn
	@VaadinFormField(fieldType=Ft.COMBO_BOX, jpql="SELECT s FROM Software AS s WHERE archived=false ORDER BY createdAt DESC")
	private Software software;
	
	@ManyToMany
	private Set<Box> boxes;
	
	@VaadinTableColumn
	@VaadinFormField(enabled = false, readOnly=true)
	private String state = "unstarted";
	
	@Lob
	@Column(length=64000)
	@VaadinFormField(fieldType=Ft.TEXT_AREA)
	private String config;
	
	@Lob
	@Column(length=64000)
	@VaadinFormField(fieldType=Ft.TEXT_AREA)
	private String output;

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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	@Override
	public String getDisplayName() {
		return null;
	}

}
