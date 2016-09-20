package com.jianglibo.vaadin.dashboard.taskrunner;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.BoxGroup;
import com.jianglibo.vaadin.dashboard.domain.Software;

public class TaskDesc {

	private Box box;

	private final Software software;

	private BoxGroup boxGroup;
	
	private final OneTaskFinishListener tfl;

	public TaskDesc(Box box, Software software, OneTaskFinishListener tfl) {
		this.box = box;
		this.tfl = tfl;
		this.software = software;
	}

	public TaskDesc(BoxGroup boxGroup, Software software, OneTaskFinishListener tfl) {
		this.boxGroup = boxGroup;
		this.software = software;
		this.tfl = tfl;
	}
	
	public List<OneThreadTaskDesc> createOneThreadTaskDescs() {
		if (isGroupTask()) {
			return getBoxGroup().getBoxes().stream().map(b -> new OneThreadTaskDesc(this, b, getSoftware(), getTfl()))
					.collect(Collectors.toList());
		} else {
			return Lists.newArrayList(new OneThreadTaskDesc(this, getBox(), getSoftware(), getTfl()));
		}
	}

	public boolean isGroupTask() {
		return boxGroup != null;
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
	
	public static interface OneTaskFinishListener {
		void OneTaskFinished(OneThreadTaskDesc ottd);
	}
}
