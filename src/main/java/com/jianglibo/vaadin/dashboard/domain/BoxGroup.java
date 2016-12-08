package com.jianglibo.vaadin.dashboard.domain;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField.Ft;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGrid;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumn;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumn;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.TwinGridFieldDescription;
import com.vaadin.ui.Grid;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 * 
 * @author jianglibo@gmail.com
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "box_group", uniqueConstraints = { @UniqueConstraint(columnNames = "name") })
@VaadinGrid(multiSelect = true, messagePrefix = "domain.boxgroup.", footerVisible = true, styleNames = {
		ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES,
		ValoTheme.TABLE_COMPACT }, selectable = true, fullSize = true, showCreatedAt=false, defaultSort="-updatedAt", selectMode=Grid.SelectionMode.MULTI)
@VaadinTable(multiSelect = true, messagePrefix = "domain.boxgroup.", footerVisible = true, styleNames = {
		ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES,
		ValoTheme.TABLE_COMPACT }, selectable = true, fullSize = true, showCreatedAt=false, defaultSort="-updatedAt")
public class BoxGroup extends BaseEntity implements HasUpdatedAt {

	@VaadinGridColumn
	@VaadinFormField(order = 10)
	@NotNull
	private String name;
	
	@ManyToMany(fetch=FetchType.EAGER, mappedBy = "boxGroups")
	@TwinGridFieldDescription(leftClazz = Box.class, rightClazz = Box.class, leftPageLength = 100, rightColumns = {"!addtoleft",
			"name", "ip", "roles" }, leftColumns = { "name", "ip", "roles", "!remove" }, rowNumber = 4)
	@VaadinFormField(fieldType = Ft.HAND_MAKER, order = 30)
	private Set<Box> boxes = Sets.newHashSet();
	
	@OneToMany(mappedBy="boxGroup", fetch=FetchType.EAGER, cascade=CascadeType.REMOVE)
	private List<BoxGroupHistory> histories;
	
	@ManyToOne
	@NotNull
	private Person creator;
	
	@Lob
	@Column(length = 65536)
	@VaadinFormField(fieldType = Ft.TEXT_AREA, order = 40, rowNumber=6)
	private String installResults;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@VaadinTableColumn(order = 9990, sortable = true)
	@VaadinGridColumn(order = 9990, sortable = true)
	private Date updatedAt;
	

	/**
	 * If box has no dnsServer, It should be found here.
	 */
	@VaadinGridColumn
	@VaadinFormField(order = 20)
	private String dnsServer;
	
	@Lob
	@VaadinFormField(fieldType = Ft.TEXT_AREA, order = 30, rowNumber=6)
	private String configContent;
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("name", getName()).add("boxnumber", getBoxes().size()).toString();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Box> getBoxes() {
		return boxes;
	}


	public void setBoxes(Set<Box> boxes) {
		this.boxes = boxes;
	}


	public String getDnsServer() {
		return dnsServer;
	}


	public void setDnsServer(String dnsServer) {
		this.dnsServer = dnsServer;
	}
	
	public String getConfigContent() {
		return configContent;
	}


	public void setConfigContent(String configContent) {
		this.configContent = configContent;
	}

	@Override
	public String getDisplayName() {
		return getName();
	}


	public Person getCreator() {
		return creator;
	}

	public void setCreator(Person creator) {
		this.creator = creator;
	}

	public List<BoxGroupHistory> getHistories() {
		return histories;
	}

	public void setHistories(List<BoxGroupHistory> histories) {
		this.histories = histories;
	}
	
	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getInstallResults() {
		return installResults;
	}

	public void setInstallResults(String installResults) {
		this.installResults = installResults;
	}

	public static class BoxGroupBuilder {
		private final String name;
		private final Person creator;
		
		private Set<Box> boxes = Sets.newHashSet();
		
		private String dnsServer;
		
		private String configContent;
		
		public BoxGroupBuilder(String name, Person creator) {
			super();
			this.name = name;
			this.creator = creator;
		}
		
		public BoxGroup build() {
			BoxGroup bg = new BoxGroup();
			bg.setName(getName());
			bg.setCreator(getCreator());
			bg.setBoxes(getBoxes());
			bg.setDnsServer(getDnsServer());
			bg.setConfigContent(getConfigContent());
			return bg;
		}
		
		public Set<Box> getBoxes() {
			return boxes;
		}

		public BoxGroupBuilder setBoxes(Set<Box> boxes) {
			this.boxes = boxes;
			return this;
		}

		public String getDnsServer() {
			return dnsServer;
		}
		public BoxGroupBuilder setDnsServer(String dnsServer) {
			this.dnsServer = dnsServer;
			return this;
		}

		public String getConfigContent() {
			return configContent;
		}

		public BoxGroupBuilder setConfigContent(String configContent) {
			this.configContent = configContent;
			return this;
		}

		public String getName() {
			return name;
		}
		public Person getCreator() {
			return creator;
		}
	}

}
