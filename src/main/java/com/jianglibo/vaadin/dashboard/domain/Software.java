package com.jianglibo.vaadin.dashboard.domain;

import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.google.common.base.Objects;
import com.jianglibo.vaadin.dashboard.GlobalComboOptions;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField.Ft;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumn;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.ComboBoxBackByStringOptions;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.themes.ValoTheme;


/**
 * A installation can not exists alone, It must install to a machine.
 * @author jianglibo@gmail.com
 *
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "software", uniqueConstraints = { @UniqueConstraint(columnNames = {"name", "ostype"})})
@VaadinTable(multiSelect = true,footerVisible=true, messagePrefix="domain.software.",styleNames={ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES, ValoTheme.TABLE_COMPACT}, selectable=true, fullSize=true)
public class Software extends BaseEntity {
	
	@ComboBoxBackByStringOptions(key = GlobalComboOptions.SOFTWARE_NAMES)
	@VaadinFormField(fieldType = Ft.COMBO_BOX)
	@VaadinTableColumn(alignment = Align.LEFT)
	@NotNull
	private String name;
	
	@ComboBoxBackByStringOptions(key = GlobalComboOptions.OS_TYPES)
	@VaadinFormField(fieldType = Ft.COMBO_BOX)
	@VaadinTableColumn()
	@NotNull
	private String ostype;
	
	@OneToMany
	private Set<InstallStepDefine> installStepDefines;
	
	
	public Software() {
		
	}
	
	public Software(String name, String ostype) {
		setName(name);
		setOstype(ostype);
	}
	
	public Install createNewInstall(){
		Install in = new Install(this);
		in.setInstallSteps(getInstallStepDefines().stream().map(isd -> new InstallStep(in, isd)).collect(Collectors.toList()));
		return in;
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

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("name", getName()).add("ostye", getOstype()).toString();
	}

	@Override
	public String getDisplayName() {
		return name;
	}

	public Set<InstallStepDefine> getInstallStepDefines() {
		return installStepDefines;
	}

	public void setInstallStepDefines(Set<InstallStepDefine> installStepDefines) {
		this.installStepDefines = installStepDefines;
	}
}
