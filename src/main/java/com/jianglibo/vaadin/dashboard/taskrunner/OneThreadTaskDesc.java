package com.jianglibo.vaadin.dashboard.taskrunner;

import com.jianglibo.vaadin.dashboard.domain.Box;
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

	public OneThreadTaskDesc(TaskDesc td, Box box, Software software, OneTaskFinishListener tfl) {
		super();
		this.td = td;
		this.box = box;
		this.software = software;
		this.tfl = tfl;
		this.boxHistory = new BoxHistory.BoxHistoryBuilder(software, box, "", true).build();
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
}
