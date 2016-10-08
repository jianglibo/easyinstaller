package com.jianglibo.vaadin.dashboard.view.clustersoftware;

import java.util.List;

import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.BoxGroup;
import com.jianglibo.vaadin.dashboard.domain.BoxGroupHistory;
import com.jianglibo.vaadin.dashboard.domain.Domains;

@SuppressWarnings("serial")
public class OneBoxGroupHistoriesDc  extends FreeContainer<BoxGroupHistory>{
	private BoxGroup boxGroup;
	
	public OneBoxGroupHistoriesDc(BoxGroup boxGroup, Domains domains, int perPage, List<?> sortableContainerPropertyIds) {
		super(domains.getRepositoryCommonCustom(BoxGroupHistory.class.getSimpleName()),domains.getDefaultSort(BoxGroupHistory.class), BoxGroupHistory.class, perPage, sortableContainerPropertyIds);
		this.boxGroup = boxGroup;
	}
	
	@Override
	public int size() {
		if (boxGroup == null) {
			return 0;
		} else {
			return boxGroup.getHistories().size();
		}
	}
	
	@Override
	public void fetchPage() {
		int start = getCurrentPage() * getPerPage();
		int end = (getCurrentPage() + 1) * getPerPage();
		int total = size();
		
		start = start > total  ? total : start;
		end = end > total ? total : end;
		List<BoxGroupHistory> bghs = boxGroup.getHistories().subList(start, end); 
		setCurrentWindow(bghs);
	}

	public BoxGroup getBoxGroup() {
		return boxGroup;
	}

	public void setBoxGroup(BoxGroup boxGroup) {
		this.boxGroup = boxGroup;
		refresh();
	}
}
