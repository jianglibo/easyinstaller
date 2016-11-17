package com.jianglibo.vaadin.dashboard.uicomponent.dynmenu;

import java.util.Set;

import com.jianglibo.vaadin.dashboard.config.CommonMenuItemIds;
import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.vaadin.server.Resource;

public class UnArchiveButtonDescription implements ButtonDescription {

	@Override
	public boolean isEnabled(Set<Object> selected) {
		if (selected.isEmpty()) {
			return false;
		} else {
			// if there exists one not archived,return false(don't display menu button.)
			return !selected.stream().anyMatch(e -> !((BaseEntity)e).isArchived());
		}
		
	}

	@Override
	public String getItemId() {
		return CommonMenuItemIds.UN_ARCHIVE;
	}

	@Override
	public Resource getIcon() {
		return null;
	}

}
