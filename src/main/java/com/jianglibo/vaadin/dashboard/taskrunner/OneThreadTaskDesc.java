package com.jianglibo.vaadin.dashboard.taskrunner;

import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.BoxGroup;
import com.jianglibo.vaadin.dashboard.domain.BoxHistory;
import com.jianglibo.vaadin.dashboard.domain.Software;

/**
 * 
 * @author jianglibo@gmail.com
 *
 */
public class OneThreadTaskDesc {

	private final Box box;

	private final Software software;
	
	private final BoxHistory boxHistory;
	
	private final TaskDesc td;
	
	private final BoxGroup boxGroup;
	
	private final String action;
	
	private final String uniqueUiId;

	public OneThreadTaskDesc(TaskDesc td,BoxGroup boxGroup, Box box, Software software, String action) {
		super();
		this.td = td;
		this.action = action;
		this.box = box;
		this.software = software;
		this.uniqueUiId = td.getUniqueUiId();
		this.boxGroup = boxGroup;
		this.boxHistory = new BoxHistory.BoxHistoryBuilder(boxGroup, software, box, "", true).build();
	}

	public TaskDesc getTd() {
		return td;
	}

	public Box getBox() {
		return box;
	}

	public Software getSoftware() {
		return software;
	}

	public BoxHistory getBoxHistory() {
		return boxHistory;
	}

	public BoxGroup getBoxGroup() {
		return boxGroup;
	}

	public String getAction() {
		return action;
	}

	public String getUniqueUiId() {
		return uniqueUiId;
	}
}
