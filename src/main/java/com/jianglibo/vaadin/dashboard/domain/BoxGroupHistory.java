package com.jianglibo.vaadin.dashboard.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.google.common.collect.Sets;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGrid;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumn;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumn;
import com.vaadin.ui.Grid;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@Entity
@Table(name = "box_group_history")
@VaadinTable(multiSelect = true, footerVisible = true, messagePrefix = "domain.box_group_history.", styleNames = {
		ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES,
		ValoTheme.TABLE_COMPACT }, selectable = true, fullSize = true, showCreatedAt=true)
@VaadinGrid(multiSelect = true, messagePrefix = "domain.box_group_history.", footerVisible = true, styleNames = {
		ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES,
		ValoTheme.TABLE_COMPACT }, selectable = true, fullSize = true, showCreatedAt=true, selectMode=Grid.SelectionMode.MULTI)
public class BoxGroupHistory extends BaseEntity {

	@ManyToOne
	@VaadinGridColumn(order = 10)
	private Software software;
	
	@ManyToOne
	@VaadinGridColumn(order = 30)
	private BoxGroup boxGroup;
	
	@VaadinGridColumn(order = 35, sortable=true)
	private String action;
	
	@VaadinGridColumn(order = 40)
	private boolean forAllBox;
	
	@ManyToOne
	@NotNull
	private Person runner;
	
	@VaadinTableColumn
	@VaadinGridColumn(order = 50, sortable=true)
	private boolean success;
	
	// boxhistory has remove cascade by box.
	@OneToMany(mappedBy="boxGroupHistory", fetch=FetchType.EAGER, cascade=CascadeType.REMOVE)
	private Set<BoxHistory> boxHistories = Sets.newHashSet();
	
	private boolean readed = false;
	
	public BoxGroupHistory() {
	}
	
	public BoxGroupHistory(Software software, BoxGroup boxGroup, String action, Set<BoxHistory> boxHistories, boolean forAllBox) {
		this.software = software;
		this.boxGroup = boxGroup;
		this.action = action;
		this.boxHistories = boxHistories;
		this.forAllBox = forAllBox;
	}
	
	@Override
	public String getDisplayName() {
		return String.format("[%s, %s, %s, %s]", boxGroup.getName(), software.getName(), getAction(), isSuccess());
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

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public boolean isForAllBox() {
		return forAllBox;
	}

	public void setForAllBox(boolean forAllBox) {
		this.forAllBox = forAllBox;
	}
}
