package com.jianglibo.vaadin.dashboard.taskrunner;

import static com.google.common.base.Preconditions.checkState;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.BoxGroup;
import com.jianglibo.vaadin.dashboard.domain.BoxGroupHistory;
import com.jianglibo.vaadin.dashboard.domain.BoxHistory;
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
	
	private boolean forAllBox;
	
	/**
	 * use broadcast instead of reference, because of when task finished, UI maybe not exists any more. 
	 */
	private final String uniqueUiId;

	public TaskDesc(String uniqueUiId, PersonVo person, BoxGroup boxGroup, Set<Box> boxes, Software software, String action) {
		this.boxGroup = boxGroup;
		this.uniqueUiId = uniqueUiId;
		if (boxes == null || boxes.isEmpty()) {
			this.forAllBox = true;
			this.boxes = boxGroup.getBoxes();
		} else {
			this.boxes = boxes;
			if (boxes.size() == boxGroup.getBoxes().size()) {
				this.forAllBox = true;
			} else {
				this.forAllBox = false;
			}
		}
		this.software = software;
		this.action = action;
		this.person = person;
	}

	public TaskDesc(String uniqueUiID, PersonVo principa, BoxGroupHistory bgh) {
		this.uniqueUiId = uniqueUiID;
		this.software = bgh.getSoftware();
		this.person = principa;
		this.action = bgh.getAction();
		this.boxGroup = bgh.getBoxGroup();
		this.boxes = bgh.getBoxHistories().stream().map(BoxHistory::getBox).collect(Collectors.toSet());
		checkState(!this.boxes.isEmpty(), "if replay boxgroupHistory, boxes never empty.");
		if (bgh.isForAllBox()) {
			this.boxes = bgh.getBoxGroup().getBoxes();
			this.forAllBox = true;
		}
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

	public boolean isForAllBox() {
		return forAllBox;
	}

	public void setForAllBox(boolean forAllBox) {
		this.forAllBox = forAllBox;
	}
}
