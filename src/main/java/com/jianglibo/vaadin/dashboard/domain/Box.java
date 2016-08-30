package com.jianglibo.vaadin.dashboard.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.google.gwt.thirdparty.guava.common.collect.Sets;
import com.jianglibo.vaadin.dashboard.GlobalComboOptions;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField.Ft;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.ComboBoxBackByStringOptions;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumn;
import com.vaadin.ui.themes.ValoTheme;

@Entity
@VaadinTable(multiSelect=true, messagePrefix="domain.box.",footerVisible=true, styleNames={ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES, ValoTheme.TABLE_COMPACT}, selectable=true, fullSize=true)
@Table(name = "box", uniqueConstraints = { @UniqueConstraint(columnNames = "ip") })
public class Box extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@NotEmpty
	@VaadinTableColumn(order = 0)
	@VaadinFormField(order = 0)
	private String ip;
	
	@VaadinTableColumn(order = 1)
	@VaadinFormField(order = 10)
	private String name;
	
	/**
	 * Owning side is which has no mappedBy property. So this IS NOT owning side.
	 */
	@OneToMany(mappedBy = "box")
	private Set<Install> installations = Sets.newHashSet();
	
	@VaadinTableColumn(order=2)
	@ComboBoxBackByStringOptions(key = GlobalComboOptions.OS_TYPES)
	@VaadinFormField(order = 20, fieldType=Ft.COMBO_BOX)
	@NotNull
	@NotEmpty
	private String osType;
	
	@VaadinFormField(order = 30, fieldType=Ft.TEXT_AREA)
	private String description;
	
//	@VaadinFormField(order = 40, fieldType=Ft.FILE_CONTENT_STRING)
//	@Lob
//	@Column(length=2000)
//	private String keyFileContent;
	
	@VaadinFormField(order = 50)
	private String keyFilePath;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

//	public String getKeyFileContent() {
//		return keyFileContent;
//	}
//
//	public void setKeyFileContent(String keyFileContent) {
//		this.keyFileContent = keyFileContent;
//	}

	public String getKeyFilePath() {
		return keyFilePath;
	}

	public void setKeyFilePath(String keyFilePath) {
		this.keyFilePath = keyFilePath;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}


	public Set<Install> getInstallations() {
		return installations;
	}

	public void setInstallations(Set<Install> installations) {
		this.installations = installations;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String getDisplayName() {
		return null;
	}
}
