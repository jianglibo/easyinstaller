package com.jianglibo.vaadin.dashboard.data.container;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.vaadin.maddon.ListContainer;

import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.view.ContainerSortListener;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public abstract class JpaContainer<T> extends ListContainer<T> {
	
	private int perPage;
	
	private boolean trashed;
	
	private String filterStr;
	
	private int currentPage;
	
	private Sort sort;
	
	private final Domains domains;
	
	private boolean enableSort = false;
	
	private Table table;
	
	private ContainerSortListener sortListener;
	
	
	public JpaContainer(Class<T> clazz, Domains domains, ContainerSortListener sortListener){
		super(clazz);
		this.domains = domains;
		this.sortListener = sortListener;
	}
	
	public void setupProperties(Sort defaultSort, int perPage) {
		setSort(defaultSort);
		setPerPage(perPage);
	}
	
	public void persistState(ListViewFragmentBuilder vfb) {
		setTrashed(vfb.getBoolean(ListViewFragmentBuilder.TRASHED_PARAM_NAME));
		// only if vfb.getSort() exists.
		if (vfb.getSort().isPresent()) {
			setSort(vfb.getSort().get());
		}
		setFilterStr(vfb.getFilterStr().orElse(""));
		setCurrentPage(vfb.getCurrentPage());
		// will cause circular sort event.
		
//		if (getSort() != null) {
//			Order od = getSort().iterator().next();
//			table.setSortContainerPropertyId(od.getProperty());
//			table.setSortAscending(od.isAscending());
//		}
	}
	
	public abstract void whenUriFragmentChange(ListViewFragmentBuilder vfb);
	
	
	@Override
	public void sort(Object[] propertyId, boolean[] ascending) {
		if (enableSort) {
			if (propertyId.length > 0) {
				String fname = (String) propertyId[0];
				Direction ndirection = ascending[0] ? Direction.ASC : Direction.DESC;
				sortListener.notifySort(sort);
			}
		}
	}

	public int getPerPage() {
		return perPage;
	}


	public void setPerPage(int perPage) {
		this.perPage = perPage;
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
