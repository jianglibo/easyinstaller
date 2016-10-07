package com.jianglibo.vaadin.dashboard.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField.Ft;
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
	
	@ManyToOne
	@VaadinGridColumn(order = 30)
	private Box box;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private BoxGroupHistory boxGroupHistory;
	
	@VaadinTableColumn
	@VaadinGridColumn(order = 50)
	@VaadinFormField(fieldType=Ft.TEXT_FIELD, readOnly=true, enabled = false)
	private boolean success = true;
	
	@Lob
	@Column(length = 131072)
	@VaadinFormField(fieldType = Ft.TEXT_AREA, rowNumber = 10)
	private String log = "";
	
	public BoxGroupHistory getBoxGroupHistory() {
		return boxGroupHistory;
	}

	public void setBoxGroupHistory(BoxGroupHistory boxGroupHistory) {
		this.boxGroupHistory = boxGroupHistory;
	}

	public void appendLogAndSetFailure(String onelog) {
		setLog(getLog() + "\n" + onelog);
		setSuccess(false);
	}

	public void appendLog(String onelog) {
		setLog(getLog() + "\n" + onelog);
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


	public void setSuccess(boolean success) {
		this.success = success;
	}


	public String getLog() {
		return log;
	}

	private void setLog(String log) {
		this.log = log;
	}

	public static class BoxHistoryBuilder {
		
		private final Software software;
		
		private final Box box;
		
		private final boolean success;
		
		private final String log;
		
		public BoxHistoryBuilder(BoxGroup boxGroup, Software software, Box box, String log, boolean success) {
			this.software = software;
			this.box = box;
			this.log = log;
			this.success = success;
		}
		
		public BoxHistory build() {
			BoxHistory bh =  new BoxHistory();
			bh.setSoftware(software);
			bh.setBox(box);
			bh.setLog(log);
			bh.setSuccess(success);
			return bh;
		}
	}

}
