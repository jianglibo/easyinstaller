package com.jianglibo.vaadin.dashboard.domain;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.google.common.collect.Sets;
import com.google.gwt.thirdparty.guava.common.base.Objects;
import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.GlobalComboOptions;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField.Ft;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.ComboBoxBackByYaml;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.ScalarGridFieldDescription;
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
	
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<BoxGroup> boxGroups = Sets.newHashSet();
	
	@ElementCollection(fetch = FetchType.EAGER)
	@VaadinFormField(fieldType = Ft.HAND_MAKER, order = 100)
	@ScalarGridFieldDescription(columns = { "value", "!remove"}, clazz = String.class, rowNumber=4)
	private Set<String> roles = Sets.newHashSet();
	
	@ManyToOne(fetch = FetchType.EAGER)
	@NotNull
	private Person creator;
	
	/**
	 * Owning side is which has no mappedBy property. So this IS NOT owning side.
	 */
	@OneToMany(mappedBy = "box", cascade=CascadeType.REMOVE)
	@OrderBy("createdAt DESC")
	private List<BoxHistory> boxHistories = Lists.newArrayList();
	
	@VaadinTableColumn(order=2)
	@ComboBoxBackByYaml(ymlKey = GlobalComboOptions.OS_TYPES)
	@VaadinFormField(order = 20, fieldType=Ft.COMBO_BOX)
	@NotNull
	@NotEmpty
	private String osType;
	
	@VaadinFormField(order = 3000, fieldType=Ft.TEXT_AREA)
	private String description;
	
	@VaadinFormField(order = 50)
	private String keyFilePath;
	
	@VaadinFormField(order = 60)
	private int port = 22;
	
	@VaadinFormField(order = 70)
	private String sshUser = "root";
	
	@VaadinFormField(order = 70)
	private String hostname;
	
	@VaadinFormField(order = 70)
	private String dnsServer;
	
	@VaadinFormField(order = 70)
	private String commaSepIps;
	
	@VaadinFormField(order = 70)
	private String commaSepPorts;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

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

	public List<BoxHistory> getBoxHistories() {
		return boxHistories;
	}

	public void setBoxHistories(List<BoxHistory> boxHistories) {
		this.boxHistories = boxHistories;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("name", getName()).add("ip", getIp()).toString();
	}

	@Override
	public String getDisplayName() {
		return toString();
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getSshUser() {
		return sshUser;
	}

	public void setSshUser(String sshUser) {
		this.sshUser = sshUser;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getDnsServer() {
		return dnsServer;
	}

	public void setDnsServer(String dnsServer) {
		this.dnsServer = dnsServer;
	}

	public String getCommaSepIps() {
		return commaSepIps;
	}

	public void setCommaSepIps(String commaSepIps) {
		this.commaSepIps = commaSepIps;
	}

	public String getCommaSepPorts() {
		return commaSepPorts;
	}

	public void setCommaSepPorts(String commaSepPorts) {
		this.commaSepPorts = commaSepPorts;
	}

	public Set<BoxGroup> getBoxGroups() {
		return boxGroups;
	}

	public void setBoxGroups(Set<BoxGroup> boxGroups) {
		this.boxGroups = boxGroups;
	}

	public Person getCreator() {
		return creator;
	}

	public void setCreator(Person creator) {
		this.creator = creator;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
}
