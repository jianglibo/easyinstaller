package com.jianglibo.vaadin.dashboard.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.jianglibo.vaadin.dashboard.GlobalComboOptions;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGrid;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumn;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField.Ft;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.ComboBoxBackByYaml;
import com.vaadin.ui.themes.ValoTheme;

@Entity
@VaadinGrid(multiSelect = true, messagePrefix = "domain.textfile.", footerVisible = true, styleNames = {
		ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES,
		ValoTheme.TABLE_COMPACT }, selectable = true, fullSize = true, showCreatedAt=true)
@VaadinTable(multiSelect = true, messagePrefix = "domain.textfile.", footerVisible = true, styleNames = {
		ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES,
		ValoTheme.TABLE_COMPACT }, selectable = true, fullSize = true, showCreatedAt=true)
@Table(name = "textfile", uniqueConstraints = { @UniqueConstraint(columnNames = "name") })
public class TextFile extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@VaadinGridColumn(order = 10, sortable = true, filterable = true)
	@VaadinFormField(order = 10)
	private String name;

	@Lob
	@Column(length = 1048576)
	@VaadinFormField(fieldType = Ft.TEXT_AREA, order = 20)
	private String content;
	
	@ManyToOne
	@NotNull
	private Software software;
	
	@VaadinFormField(fieldType = Ft.COMBO_BOX, order = 130)
	@ComboBoxBackByYaml(ymlKey = GlobalComboOptions.LINE_SEPERATOR)
	private String codeLineSeperator = "LF";

	public TextFile() {
	}
	
	public TextFile(String name, String content) {
		this.name = name;
		this.content = content;
	}
	

	@Override
	public String getDisplayName() {
		return getName();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Software getSoftware() {
		return software;
	}

	public void setSoftware(Software software) {
		this.software = software;
	}

	public String getCodeLineSeperator() {
		return codeLineSeperator;
	}

	public void setCodeLineSeperator(String codeLineSeperator) {
		this.codeLineSeperator = codeLineSeperator;
	}
}
