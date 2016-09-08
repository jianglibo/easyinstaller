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

	@Lob
	@Column(length = 64000)
	@VaadinFormField(fieldType = Ft.TEXT_AREA, order = 30)
	private String ymlContent;

	@Lob
	@Column(length = 64000)
	@VaadinFormField(fieldType = Ft.TEXT_AREA, order = 40)
	private String codeContent;

	private boolean ifSuccessSkipNext;

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

	public String getYmlContent() {
		return ymlContent;
	}

	public void setYmlContent(String ymlContent) {
		this.ymlContent = ymlContent;
	}

	@Override
	public String getDisplayName() {
		return String.format("[%s,%s,%s]", getName(), getOstype(), getId());
	}

	public boolean isIfSuccessSkipNext() {
		return ifSuccessSkipNext;
	}

	public void setIfSuccessSkipNext(boolean ifSuccessSkipNext) {
		this.ifSuccessSkipNext = ifSuccessSkipNext;
	}

	public String getRunner() {
		return runner;
	}

	public void setRunner(String runner) {
		this.runner = runner;
	}
}
