package com.jianglibo.vaadin.dashboard.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.jianglibo.vaadin.dashboard.GlobalComboOptions;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField.Ft;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumn;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.ComboBoxBackByStringOptions;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.ComboBoxBackByYaml;
import com.vaadin.ui.themes.ValoTheme;

@Entity
@VaadinTable(multiSelect = true, messagePrefix = "domain.stepdefine.", footerVisible = true, styleNames = {
		ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES,
		ValoTheme.TABLE_COMPACT }, selectable = true, fullSize = true)
@Table(name = "stepdefine", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "ostype" }) })
public class StepDefine extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	@VaadinTableColumn
	@VaadinFormField(order = 10)
	private String name;

	@NotNull
	@NotEmpty
	@ComboBoxBackByStringOptions(key = GlobalComboOptions.RUNNERS)
	@VaadinFormField(fieldType = Ft.COMBO_BOX, order = 15)
	private String runner;

	@NotNull
	@VaadinTableColumn
	@VaadinFormField(order = 20)
	private String ostype;

	@VaadinFormField(fieldType = Ft.COMBO_BOX, order = 30)
	@ComboBoxBackByYaml(ymlKey = GlobalComboOptions.PREFERED_FORMAT)
	private String preferredInfoFormat;

	@Lob
	@Column(length = 64000)
	@VaadinFormField(fieldType = Ft.TEXT_AREA, order = 30)
	private String infoContent;

	@Lob
	@Column(length = 64000)
	@VaadinFormField(fieldType = Ft.TEXT_AREA, order = 40)
	private String codeContent;

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

	public String getCodeContent() {
		return codeContent;
	}

	public void setCodeContent(String codeContent) {
		this.codeContent = codeContent;
	}

	public String getInfoContent() {
		return infoContent;
	}

	public void setInfoContent(String infoContent) {
		this.infoContent = infoContent;
	}

	@Override
	public String getDisplayName() {
		return String.format("[%s,%s,%s]", getName(), getOstype(), getId());
	}

	public String getRunner() {
		return runner;
	}

	public void setRunner(String runner) {
		this.runner = runner;
	}

	public String getPreferredInfoFormat() {
		return preferredInfoFormat;
	}

	public void setPreferredInfoFormat(String preferredInfoFormat) {
		this.preferredInfoFormat = preferredInfoFormat;
	}
}
