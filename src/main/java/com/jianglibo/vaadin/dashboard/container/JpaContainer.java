package com.jianglibo.vaadin.dashboard.container;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.vaadin.maddon.ListContainer;

import com.google.common.eventbus.EventBus;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.event.view.TableSortEvent;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public class JpaContainer<T> extends ListContainer<T> {
	
	private int perPage;
	
	private boolean trashed;
	
	private EventBus eventBus;
	
	private String filterStr;
	
	private int currentPage;
	
	private Sort sort;
	
	private final Domains domains;
	
	private boolean enableSort = false;
	
	private Table table;
	
	
	public JpaContainer(Class<T> clazz, Domains domains){
		super(clazz);
		this.domains = domains;
	}
	
	public void setupProperties(Table table, EventBus eventBus, Sort defaultSort, int perPage) {
		setTable(table);
		setEventBus(eventBus);
		setSort(defaultSort);
		setPerPage(perPage);
		getEventBus().register(this);
	}
	
	public void persistState(ListViewFragmentBuilder vfb) {
		setTrashed(vfb.getBoolean(ListViewFragmentBuilder.TRASHED_PARAM_NAME));
		// only if vfb.getSort() exists.
		if (vfb.getSort() != null) {
			setSort(vfb.getSort());
		}
		setFilterStr(vfb.getFilterStr());
		setCurrentPage(vfb.getCurrentPage());
		// will cause circular sort event.
		
//		if (getSort() != null) {
//			Order od = getSort().iterator().next();
//			table.setSortContainerPropertyId(od.getProperty());
//			table.setSortAscending(od.isAscending());
//		}
	}
	
	
	@Override
	public void sort(Object[] propertyId, boolean[] ascending) {
		if (enableSort) {
			if (propertyId.length > 0) {
				String fname = (String) propertyId[0];
				Direction ndirection = ascending[0] ? Direction.ASC : Direction.DESC;
				eventBus.post(new TableSortEvent(new Sort(ndirection, fname)));
			}
		}
	}

	public int getPerPage() {
		return perPage;
	}


	public void setPerPage(int perPage) {
		this.perPage = perPage;
	}


	public EventBus getEventBus() {
		return eventBus;
	}


	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public Sort getSort() {
		return sort;
	}

	public String getFilterStr() {
		return filterStr;
	}

	public void setFilterStr(String filterStr) {
		this.filterStr = filterStr;
	}

	public void setSort(Sort sort) {
		this.sort = sort;
	}


	public void setEnableSort(boolean enableSort) {
		this.enableSort = enableSort;
	}

	public Domains getDomains() {
		return domains;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public boolean isTrashed() {
		return trashed;
	}

	public void setTrashed(boolean trashed) {
		this.trashed = trashed;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}
	
	
}
