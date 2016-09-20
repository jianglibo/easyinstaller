package com.jianglibo.vaadin.dashboard.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.google.common.base.Objects;
import com.google.gwt.thirdparty.guava.common.collect.Sets;
import com.jianglibo.vaadin.dashboard.GlobalComboOptions;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField.Ft;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumn;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.ComboBoxBackByStringOptions;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.ComboBoxBackByYaml;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.ScalarGridFieldDescription;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Software is made of files to upload, code to execute, and custom
 * configurations. When install happen, also given an environment which take
 * information of box to install, and boxgroup target box belongs.
 * 
 * 
 * @author jianglibo@gmail.com
 *
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "software", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "ostype" }) })
@VaadinTable(multiSelect = true, footerVisible = true, messagePrefix = "domain.software.", styleNames = {
		ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES,
		ValoTheme.TABLE_COMPACT }, selectable = true, fullSize = true)
public class Software extends BaseEntity {

	@VaadinFormField(order = 10)
	@VaadinTableColumn(alignment = Align.LEFT)
	@NotNull
	private String name;

	@ComboBoxBackByYaml(ymlKey = GlobalComboOptions.OS_TYPES)
	@VaadinFormField(fieldType = Ft.COMBO_BOX, order = 20)
	@VaadinTableColumn()
	@NotNull
	private String ostype;

	@ElementCollection(fetch = FetchType.EAGER)
	@VaadinFormField(fieldType = Ft.HAND_MAKER, order = 100)
	@ScalarGridFieldDescription(columns = { "value", "!remove"}, clazz = String.class, rowNumber=4)
	private Set<String> filesToUpload = Sets.newHashSet();

	@Lob
	@Column(length = 154112)
	@VaadinFormField(fieldType=Ft.TEXT_AREA, order = 120, rowNumber=10)
	private String codeToExecute;

	@Lob
	@Column(length = 154112)
	@VaadinFormField(fieldType=Ft.TEXT_AREA, order = 110)
	private String configContent;
	
	@VaadinFormField(fieldType = Ft.COMBO_BOX, order = 115)
	@ComboBoxBackByYaml(ymlKey = GlobalComboOptions.PREFERED_FORMAT)
	private String preferredFormat;
	
	@ManyToOne
	private Person creator;

	public Software() {

	}

	public Software(String name, String ostype) {
		setName(name);
		setOstype(ostype);
	}

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

	public Set<String> getFilesToUpload() {
		return filesToUpload;
	}

	public void setFilesToUpload(Set<String> filesToUpload) {
		this.filesToUpload = filesToUpload;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("name", getName()).add("ostye", getOstype()).toString();
	}

	@Override
	public String getDisplayName() {
		return name;
	}

	public String getCodeToExecute() {
		return codeToExecute;
	}

	public void setCodeToExecute(String codeToExecute) {
		this.codeToExecute = codeToExecute;
	}

	public String getConfigContent() {
		return configContent;
	}

	public void setConfigContent(String configContent) {
		this.configContent = configContent;
	}

	public String getPreferredFormat() {
		return preferredFormat;
	}

	public void setPreferredFormat(String preferredFormat) {
		this.preferredFormat = preferredFormat;
	}

	public Person getCreator() {
		return creator;
	}

	public void setCreator(Person creator) {
		this.creator = creator;
	}
}
