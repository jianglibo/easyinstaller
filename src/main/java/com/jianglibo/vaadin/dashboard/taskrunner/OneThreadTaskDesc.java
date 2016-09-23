package com.jianglibo.vaadin.dashboard.taskrunner;

import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.BoxGroup;
import com.jianglibo.vaadin.dashboard.domain.BoxHistory;
import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.taskrunner.TaskDesc.OneTaskFinishListener;

/**
 * 
 * @author jianglibo@gmail.com
 *
 */
public class OneThreadTaskDesc {

	private final Box box;

	private final Software software;
	
	private final OneTaskFinishListener tfl;
	
	private final BoxHistory boxHistory;
	
	private final TaskDesc td;
	
	private final String taskId;
	
	private final BoxGroup boxGroup;
	
	private final String action;

	public OneThreadTaskDesc(TaskDesc td,BoxGroup boxGroup, Box box, Software software, String action, OneTaskFinishListener tfl) {
		super();
		this.td = td;
		this.action = action;
		this.box = box;
		this.software = software;
		this.tfl = tfl;
		this.taskId = td.getTaskId();
		this.boxGroup = boxGroup;
		this.boxHistory = new BoxHistory.BoxHistoryBuilder(td.getTaskId(),boxGroup, software, box, "", true).build();
	}
	
	public void notifyOneTaskFinished() {
		getTfl().OneTaskFinished(this);
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

	public OneTaskFinishListener getTfl() {
		return tfl;
	}

	public BoxHistory getBoxHistory() {
		return boxHistory;
	}

	public String getTaskId() {
		return taskId;
	}

	public BoxGroup getBoxGroup() {
		return boxGroup;
	}

	public String getAction() {
		return action;
	}
	
	
}
