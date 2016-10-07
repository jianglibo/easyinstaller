package com.jianglibo.vaadin.dashboard.domain;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGrid;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumn;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumn;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@Entity
@Table(name = "box_group_history")
@VaadinTable(multiSelect = true, footerVisible = true, messagePrefix = "domain.box_group_history.", styleNames = {
		ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES,
		ValoTheme.TABLE_COMPACT }, selectable = true, fullSize = true)
@VaadinGrid(multiSelect = true, messagePrefix = "domain.box_group_history.", footerVisible = true, styleNames = {
		ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES,
		ValoTheme.TABLE_COMPACT }, selectable = true, fullSize = true)
public class BoxGroupHistory extends BaseEntity {

	@ManyToOne
	@VaadinGridColumn(order = 10)
	private Software software;
	
	@ManyToOne
	@VaadinGridColumn(order = 30)
	private BoxGroup boxGroup;
	
	@ManyToOne
	@NotNull
	private Person runner;
	
	@VaadinTableColumn
	@VaadinGridColumn(order = 50)
	private boolean success;
	
	@OneToMany(mappedBy="boxGroupHistory", fetch=FetchType.EAGER, cascade=CascadeType.REMOVE)
	private Set<BoxHistory> boxHistories = Sets.newHashSet();
	
	private boolean readed = false;
	
	public BoxGroupHistory() {
	}
	
	public BoxGroupHistory(Software software, BoxGroup boxGroup, Set<BoxHistory> boxHistories) {
		this.software = software;
		this.boxGroup = boxGroup;
		this.boxHistories = boxHistories;
	}
	
	@Override
	public String getDisplayName() {
		return String.format("[%s, %s, %s]", boxGroup.getName(), software.getName(), isSuccess());
	}
	
	public Software getSoftware() {
		return software;
	}

	public void setSoftware(Software software) {
		this.software = software;
	}

	public boolean isSuccess() {
		return success;
	}

	public Person getRunner() {
		return runner;
	}

	public void setRunner(Person runner) {
		this.runner = runner;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean isReaded() {
		return readed;
	}

	public void setReaded(boolean readed) {
		this.readed = readed;
	}

	public BoxGroup getBoxGroup() {
		return boxGroup;
	}

	public void setBoxGroup(BoxGroup boxGroup) {
		this.boxGroup = boxGroup;
	}

	public Set<BoxHistory> getBoxHistories() {
		return boxHistories;
	}

	public void setBoxHistories(Set<BoxHistory> boxHistories) {
		this.boxHistories = boxHistories;
	}
}
