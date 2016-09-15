package com.jianglibo.vaadin.dashboard.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGrid;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumn;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.vaadin.ui.themes.ValoTheme;

@Entity
@VaadinGrid(multiSelect = true, messagePrefix = "domain.kkv.", footerVisible = true, styleNames = {
		ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES,
		ValoTheme.TABLE_COMPACT }, selectable = true, fullSize = true)
@VaadinTable(multiSelect = true, messagePrefix = "domain.kkv.", footerVisible = true, styleNames = {
		ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES,
		ValoTheme.TABLE_COMPACT }, selectable = true, fullSize = true)
@Table(name = "kkv", uniqueConstraints = { @UniqueConstraint(columnNames = { "kgroup", "key", "value" }) })
public class Kkv extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@VaadinGridColumn(order = 10, sortable = true, filterable = true)
	@VaadinFormField(order = 10)
	private String kgroup;

	@VaadinGridColumn(order = 20)
	@VaadinFormField(order = 20)
	private String key;

	@VaadinGridColumn(order = 30)
	@VaadinFormField(order = 30)
	private String value;
	
	@ManyToOne
	private Person creator;

	@Override
	public String getDisplayName() {
		return String.format("%s[%s:%s]", getKgroup(), getKey(), getValue());
	}

	public String getKgroup() {
		return kgroup;
	}

	public void setKgroup(String kgroup) {
		this.kgroup = kgroup;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Person getCreator() {
		return creator;
	}

	public void setCreator(Person creator) {
		this.creator = creator;
	}
}
