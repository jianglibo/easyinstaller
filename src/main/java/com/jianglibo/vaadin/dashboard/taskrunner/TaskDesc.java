package com.jianglibo.vaadin.dashboard.taskrunner;

import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.BoxGroup;
import com.jianglibo.vaadin.dashboard.domain.BoxHistory;
import com.jianglibo.vaadin.dashboard.domain.Software;

public class TaskDesc {

	private final String id;

	private Box box;

	private final Software software;

	private BoxGroup boxGroup;

	private final Map<Long, BoxHistory> histories = Maps.newHashMap();

	public TaskDesc(Box box, Software software) {
		this.box = box;
		this.software = software;
		this.id = UUID.randomUUID().toString();
	}

	public TaskDesc(BoxGroup boxGroup, Software software) {
		this.boxGroup = boxGroup;
		this.software = software;
		this.id = UUID.randomUUID().toString();
	}

	public synchronized void addHistory(BoxHistory history) {
		histories.put(history.getBox().getId(), history);
	}

	public synchronized BoxHistory getHistory(Box box) {
		if (!histories.containsKey(box.getId())) {
			histories.put(box.getId(), new BoxHistory.BoxHistoryBuilder(software, box, "", true).build());
		}
		return histories.get(box.getId());
	}

	public String getId() {
		return id;
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

	public Map<Long, BoxHistory> getHistories() {
		return histories;
	}
}
