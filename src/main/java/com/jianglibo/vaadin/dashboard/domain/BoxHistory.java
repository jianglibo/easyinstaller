package com.jianglibo.vaadin.dashboard.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.jianglibo.vaadin.dashboard.annotation.VaadinGrid;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumn;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumn;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@Entity
@Table(name = "box_history")
@VaadinTable(multiSelect = true, footerVisible = true, messagePrefix = "domain.box_history.", styleNames = {
		ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES,
		ValoTheme.TABLE_COMPACT }, selectable = true, fullSize = true)
@VaadinGrid(multiSelect = true, messagePrefix = "domain.box_history.", footerVisible = true, styleNames = {
		ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES,
		ValoTheme.TABLE_COMPACT }, selectable = true, fullSize = true)
public class BoxHistory extends BaseEntity {

	@ManyToOne
	@VaadinGridColumn(order = 10)
	private Software software;
	
	/**
	 * unique TaskId, usually is uuid.
	 */
	@VaadinGridColumn(order = 40)
	private String taskId;
	
	@ManyToOne
	@VaadinGridColumn(order = 30)
	private Box box;
	
	@ManyToOne
	@VaadinGridColumn(order = 20)
	private BoxGroup boxGroup;
	
	@ManyToOne
	@NotNull
	private Person runner;
	
	@VaadinTableColumn
	@VaadinGridColumn(order = 50)
	private boolean success;
	
	private boolean readed = false;
	
	@Lob
	@Column(length = 131072)
	private String log = "";
	
	public void appendLog(String onelog) {
		setLog(getLog() + "\n" + onelog);
		setSuccess(false);
	}
	
	@Override
	public String getDisplayName() {
		return String.format("[%s, %s, %s]", box.getIp(), software.getName(), isSuccess());
	}
	
	public Software getSoftware() {
		return software;
	}

	public void setSoftware(Software software) {
		this.software = software;
	}

	public Box getBox() {
		return box;
	}

	public void setBox(Box box) {
		this.box = box;
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

	public String getLog() {
		return log;
	}

	public BoxGroup getBoxGroup() {
		return boxGroup;
	}

	public void setBoxGroup(BoxGroup boxGroup) {
		this.boxGroup = boxGroup;
	}

	private void setLog(String log) {
		this.log = log;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public static class BoxHistoryBuilder {
		
		private final Software software;
		
		private final Box box;
		
		private final BoxGroup boxGroup;
		
		private final boolean success;
		
		private final String taskId;
		
		private final String log;
		
		public BoxHistoryBuilder(String taskId,BoxGroup boxGroup, Software software, Box box, String log, boolean success) {
			this.software = software;
			this.box = box;
			this.log = log;
			this.taskId = taskId;
			this.boxGroup = boxGroup;
			this.success = success;
		}
		
		public BoxHistory build() {
			BoxHistory bh =  new BoxHistory();
			bh.setSoftware(software);
			bh.setBox(box);
			bh.setLog(log);
			bh.setBoxGroup(boxGroup);
			bh.setSuccess(success);
			bh.setTaskId(taskId);
			return bh;
		}
	}

}
