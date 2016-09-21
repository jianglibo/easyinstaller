package com.jianglibo.vaadin.dashboard.data.container;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.vaadin.maddon.ListContainer;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.event.view.PageMetaEvent;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.SortUtil;
import com.jianglibo.vaadin.dashboard.view.ContainerSortListener;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public abstract class JpaContainer<T> extends ListContainer<T> {
	
	protected final Logger Logger;
	
	private int perPage;
	
	private boolean trashed;
	
	private String filterStr;
	
	private int currentPage;
	
	private Sort sort;
	
	private final Domains domains;
	
	private boolean enableSort = false;
	
	private Table table;
	
	private ContainerSortListener sortListener;
	
	private List<PageMetaChangeListener> pageMetaChangeListeners = Lists.newArrayList();
	
	
	public static interface PageMetaChangeListener {
		void pageMetaChanged(PageMetaEvent pme);
	}
	
	public void addPageMetaChangeListener(PageMetaChangeListener pmcl) {
		pageMetaChangeListeners.add(pmcl);
	}
	
	public void notifyPageMetaChangeListeners(PageMetaEvent pme) {
		pageMetaChangeListeners.forEach(pmcl -> {
			pmcl.pageMetaChanged(pme);
		});
	}
	
	
	public JpaContainer(Class<T> clazz, Domains domains ){
		super(clazz);
		this.Logger = LoggerFactory.getLogger(clazz);
		this.domains = domains;
		VaadinTableWrapper vtw = getDomains().getTables().get(clazz.getSimpleName());
		setupProperties(SortUtil.fromString(vtw.getVt().defaultSort()), vtw.getVt().defaultPerPage());
	}
	
	public void setupProperties(Sort defaultSort, int perPage) {
		setSort(defaultSort);
		setPerPage(perPage);
	}
	
	public void persistState(ListViewFragmentBuilder lvfb) {
		setTrashed(lvfb.getBoolean(ListViewFragmentBuilder.TRASHED_PARAM_NAME));
		// only if vfb.getSort() exists.
		if (lvfb.getSort().isPresent()) {
			setSort(lvfb.getSort().get());
		}
		setFilterStr(lvfb.getFilterStr().orElse(""));
		setCurrentPage(lvfb.getCurrentPage());
		// will cause circular sort event.
	}
	
	/**
	 * The change is from url change.
	 * @param lvfb
	 */
	public abstract void whenUriFragmentChange(ListViewFragmentBuilder lvfb);
	
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
	
	public abstract void refresh();	
	
}
