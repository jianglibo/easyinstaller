package com.jianglibo.vaadin.dashboard.taskrunner;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.BoxGroup;
import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.security.PersonVo;

/**
 * send task to boxgroups, never standalone box.
 * 
 * 
 * @author jianglibo@gmail.com
 *
 */
public class TaskDesc {

	private final PersonVo person;

	private final Software software;

	private BoxGroup boxGroup;

	private final String taskId;

	private final OneTaskFinishListener tfl;

	private Set<Box> boxes;

	private String action;
	
//	public TaskDesc(PersonVo person, BoxGroup boxGroup, Software software, String action, OneTaskFinishListener tfl) {
//		this.boxGroup = boxGroup;
//		this.boxes = boxGroup.getBoxes();
//		this.software = software;
//		this.action = action;
//		this.tfl = tfl;
//		this.person = person;
//		this.taskId = UUID.randomUUID().toString();
//	}

	public TaskDesc(PersonVo person, BoxGroup boxGroup, Set<Box> boxes, Software software, String action, OneTaskFinishListener tfl) {
		this.boxGroup = boxGroup;
		if (boxes == null || boxes.isEmpty()) {
			this.boxes = boxGroup.getBoxes();
		} else {
			this.boxes = boxes;
		}
		this.software = software;
		this.action = action;
		this.tfl = tfl;
		this.person = person;
		this.taskId = UUID.randomUUID().toString();
	}

	public List<OneThreadTaskDesc> createOneThreadTaskDescs() {
		return getBoxes().stream()
				.map(b -> new OneThreadTaskDesc(this, boxGroup, b, getSoftware(), getAction(), getTfl()))
				.collect(Collectors.toList());
	}

	public String getTaskId() {
		return taskId;
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
		void oneTaskFinished(OneThreadTaskDesc ottd, boolean groupFinished);
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Set<Box> getBoxes() {
		return boxes;
	}

	public void setBoxes(Set<Box> boxes) {
		this.boxes = boxes;
	}
}
