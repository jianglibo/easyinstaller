package com.jianglibo.vaadin.dashboard.view.boxgrouphistory;

import java.util.List;

import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.BoxGroupHistory;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;

@SuppressWarnings("serial")
public class BoxGroupHistoryContainer extends FreeContainer<BoxGroupHistory> {

	public BoxGroupHistoryContainer(Domains domains, int perPage, List<?> sortableContainerPropertyIds) {
		super(domains, BoxGroupHistory.class, perPage, sortableContainerPropertyIds);
	}
	
	@Override
	public void whenUriFragmentChange(ListViewFragmentBuilder lvfb) {
		super.whenUriFragmentChange(lvfb);
		notifyItemSetChanged();
	}

	@Override
	public int size() {
		if (getLvfb() == null) {
			return 0;
		} else {
			int i = new Long(getDomains().getRepositoryCommonCustom(getSimpleClassName())
					.getFilteredNumberWithOnePhrase(getFilterString(), isTrashed())).intValue();
			return i;
		}
	}

	@Override
	public List<?> getItemIds(int startIndex, int numberOfItems) {
		return super.getItemIds(startIndex, numberOfItems);
	}

}
