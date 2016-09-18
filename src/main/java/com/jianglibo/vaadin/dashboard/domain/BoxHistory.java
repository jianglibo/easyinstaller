package com.jianglibo.vaadin.dashboard.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.jianglibo.vaadin.dashboard.annotation.VaadinGrid;
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
	private Software software;
	
	@ManyToOne
	private Box box;
	
	@ManyToOne
	private ClusterHistory clusterHistory;
	
	@VaadinTableColumn
	private boolean success;
	
	@Lob
	@Column(length = 131072)
	private String log = "";
	
	public void appendLog(String onelog) {
		setLog(getLog() + "\n" + onelog);
		setSuccess(false);
	}
	
	@Override
	public String getDisplayName() {
		return "";
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

	public ClusterHistory getClusterHistory() {
		return clusterHistory;
	}

	public void setClusterHistory(ClusterHistory clusterHistory) {
		this.clusterHistory = clusterHistory;
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
		
		private ClusterHistory clusterHistory;
		
		private final boolean success;
		
		private final String log;
		
		public BoxHistoryBuilder(Software software, Box box, String log, boolean success) {
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
			bh.setClusterHistory(clusterHistory);
			return bh;
		}
		
		public BoxHistoryBuilder setClusterHistory(ClusterHistory clusterHistory) {
			this.clusterHistory = clusterHistory;
			return this;
		}
	}

}
