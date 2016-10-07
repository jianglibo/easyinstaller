package com.jianglibo.vaadin.dashboard.taskrunner;

import java.util.List;
import java.util.Set;
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

	private Set<Box> boxes;

	private String action;
	
	/**
	 * use broadcast instead of reference, because of when task finished, UI maybe not exists any more. 
	 */
	private final String uniqueUiId;

	public TaskDesc(String uniqueUiId, PersonVo person, BoxGroup boxGroup, Set<Box> boxes, Software software, String action) {
		this.boxGroup = boxGroup;
		this.uniqueUiId = uniqueUiId;
		if (boxes == null || boxes.isEmpty()) {
			this.boxes = boxGroup.getBoxes();
		} else {
			this.boxes = boxes;
		}
		this.software = software;
		this.action = action;
		this.person = person;
	}

	public List<OneThreadTaskDesc> createOneThreadTaskDescs() {
		return getBoxes().stream()
				.map(b -> new OneThreadTaskDesc(this, boxGroup, b, getSoftware(), getAction()))
				.collect(Collectors.toList());
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

	public PersonVo getPerson() {
		return person;
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

	public String getUniqueUiId() {
		return uniqueUiId;
	}
}
