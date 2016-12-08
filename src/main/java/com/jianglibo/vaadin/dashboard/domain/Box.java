package com.jianglibo.vaadin.dashboard.domain;

import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jianglibo.vaadin.dashboard.GlobalComboOptions;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField.Ft;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGrid;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumn;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumn;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.ComboBoxBackByYaml;
import com.jianglibo.vaadin.dashboard.util.StrUtil;
import com.vaadin.ui.Grid;
import com.vaadin.ui.themes.ValoTheme;

@Entity
@VaadinTable(multiSelect=true, messagePrefix="domain.box.",footerVisible=true, styleNames={ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES, ValoTheme.TABLE_COMPACT}, selectable=true, fullSize=true, showCreatedAt=false, defaultSort="-updatedAt")
@VaadinGrid(multiSelect=true, messagePrefix="domain.box.",footerVisible=true, styleNames={ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES, ValoTheme.TABLE_COMPACT}, selectable=true, fullSize=true, showCreatedAt=false, defaultSort="-updatedAt", selectMode = Grid.SelectionMode.MULTI)
@Table(name = "box", uniqueConstraints = { @UniqueConstraint(columnNames = "ip") })
public class Box extends BaseEntity implements HasUpdatedAt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@NotEmpty
	@VaadinTableColumn(order = 0)
	@VaadinGridColumn(order = 0)
	@VaadinFormField(order = 0)
	private String ip;
	
	@VaadinTableColumn(order = 1)
	@VaadinFormField(order = 10)
	@VaadinGridColumn(order = 10)
	@NotNull
	private String name;
	
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<BoxGroup> boxGroups = Sets.newHashSet();
	
	@VaadinFormField(order = 120)
	@VaadinGridColumn(order = 120)
	private String roles;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@NotNull
	private Person creator;
	
	@Temporal(TemporalType.TIMESTAMP)
	@VaadinTableColumn(order = 9990, sortable = true)
	@VaadinGridColumn(order = 9990, sortable = true)
	private Date updatedAt;
	
	@VaadinTableColumn(order=2)
	@VaadinGridColumn(order = 2)
	@ComboBoxBackByYaml(ymlKey = GlobalComboOptions.OS_TYPES)
	@VaadinFormField(order = 20, fieldType=Ft.COMBO_BOX)
	@NotNull
	@NotEmpty
	private String ostype;
	
	@OneToMany(mappedBy="box", fetch = FetchType.EAGER, cascade=CascadeType.REMOVE)
	private List<BoxHistory> histories = Lists.newArrayList();
	
	@VaadinFormField(order = 3000, fieldType=Ft.TEXT_AREA)
	private String description;
	
	@VaadinFormField(order = 50)
	private String keyFilePath;
	
	@VaadinFormField(order = 60)
	private int port = 22;
	
	@VaadinFormField(order = 70)
	private String sshUser = "root";
	
	@VaadinFormField(order = 80)
	private String hostname;
	
	@VaadinFormField(order = 90)
	private String dnsServer;
	
	@VaadinFormField(order = 100)
	private String ips;
	
	@VaadinFormField(order = 110)
	private String ports;
	
	@Lob
	@VaadinFormField(fieldType = Ft.TEXT_AREA, order = 200, rowNumber = 5)
	private String boxRoleConfig = "";
	
	public List<BoxHistory> getHistories() {
		return histories;
	}

	public void setHistories(List<BoxHistory> histories) {
		this.histories = histories;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getKeyFilePath() {
		return keyFilePath;
	}

	public Path getKeyFilePath(Path sshsBase) {
		return sshsBase.resolve(getKeyFilePath());
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

	public String getOstype() {
		return ostype;
	}

	public void setOstype(String ostype) {
		this.ostype = ostype;
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
	
	public Set<String> getRoleSetUpCase() {
		if (getRoles() == null || getRoles().trim().isEmpty()) {
			return Sets.newHashSet();
		} else {
			return Sets.newHashSet(StrUtil.commaSplitter.split(getRoles().toUpperCase()));
		}
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
		if (Strings.isNullOrEmpty(hostname)) {
			return getIp();
		} else {
			if (hostname.trim().isEmpty()) {
				return getIp();
			} else {
				return hostname;
			}
		}
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

	public String getIps() {
		return ips;
	}

	public void setIps(String ips) {
		this.ips = ips;
	}

	public String getPorts() {
		return ports;
	}

	public void setPorts(String ports) {
		this.ports = ports;
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

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		if (roles != null) {
			this.roles = roles.toUpperCase();
		} else {
			this.roles = roles;
		}
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getBoxRoleConfig() {
		return boxRoleConfig;
	}

	public void setBoxRoleConfig(String boxRoleConfig) {
		this.boxRoleConfig = boxRoleConfig;
	}
}
