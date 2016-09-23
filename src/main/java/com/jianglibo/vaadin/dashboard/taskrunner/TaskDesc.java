package com.jianglibo.vaadin.dashboard.taskrunner;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.BoxGroup;
import com.jianglibo.vaadin.dashboard.domain.BoxHistory;
import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.security.PersonVo;

/**
 * Create a task for standard alone box? It is ambiguous. If said creating a
 * task for box in a named cluster, It is more accurate.
 * 
 * @author jianglibo@gmail.com
 *
 */
public class TaskDesc {

	private Box box;

	private final PersonVo person;

	private final Software software;

	private BoxGroup boxGroup;

	private final String taskId;

	private final OneTaskFinishListener tfl;
	
	private boolean clusterAction;
	
	private String action;

	// public TaskDesc(PersonVo person, Box box, Software software,
	// OneTaskFinishListener tfl) {
	// this.box = box;
	// this.tfl = tfl;
	// this.software = software;
	// this.person = person;
	// this.taskId = UUID.randomUUID().toString();
	// }

	/**
	 * create a task inherit property from previously run task.
	 * @param person
	 * @param boxHistory
	 * @param tfl
	 */
	public TaskDesc(PersonVo person, BoxHistory boxHistory,String action, OneTaskFinishListener tfl) {
		this.box = boxHistory.getBox();
		this.tfl = tfl;
		this.action = action;
		this.software = boxHistory.getSoftware();
		this.boxGroup = boxHistory.getBoxGroup();
		this.person = person;
		this.taskId = UUID.randomUUID().toString();
		this.setClusterAction(false);
	}

	public TaskDesc(PersonVo person, BoxGroup boxGroup, Software software,String action,  OneTaskFinishListener tfl) {
		this.boxGroup = boxGroup;
		this.software = software;
		this.action = action;
		this.tfl = tfl;
		this.person = person;
		this.taskId = UUID.randomUUID().toString();
		this.setClusterAction(true);
	}

	public List<OneThreadTaskDesc> createOneThreadTaskDescs() {
		if (isClusterAction()) {
			return getBoxGroup().getBoxes().stream()
					.map(b -> new OneThreadTaskDesc(this, boxGroup, b, getSoftware(),getAction(), getTfl()))
					.collect(Collectors.toList());
		} else {
			return Lists.newArrayList(new OneThreadTaskDesc(this, boxGroup, getBox(), getSoftware(),getAction(), getTfl()));
		}
	}

	public String getTaskId() {
		return taskId;
	}

	public Box getBox() {
		return box;
	}

	public void setBox(Box box) {
		this.box = box;
	}

	public BoxGroup getBoxGroup() {
		return boxGroup;
	}

	public void setBoxGroup(BoxGroup boxGroup) {
		this.boxGroup = boxGroup;
	}

	public Software getSoftware() {
		return software;
	}

	public OneTaskFinishListener getTfl() {
		return tfl;
	}

	public PersonVo getPerson() {
		return person;
	}

	public static interface OneTaskFinishListener {
		void OneTaskFinished(OneThreadTaskDesc ottd);
	}

	public boolean isClusterAction() {
		return clusterAction;
	}

	public void setClusterAction(boolean clusterAction) {
		this.clusterAction = clusterAction;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	
}
