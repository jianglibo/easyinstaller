package com.jianglibo.vaadin.dashboard.util;

import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.google.common.base.Strings;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class ListViewFragmentBuilder extends ViewFragmentBuilder {
	
	public static final String PAGE_PARAM_NAME = "p";
	
	public static final String PER_PAGE_PARAM_NAME = "pp";
	
	public static final String SORT_PARAM_NAME = "sort";
	
	public static final String FILTER_STR_PARAM_NAME = "q";
	
	public static final String TRASHED_PARAM_NAME = "trashed";
	
	private boolean fromNav = false;
	
	public ListViewFragmentBuilder(ViewChangeEvent vce) {
		super(vce.getParameters(), vce.getViewName());
	}
	
	public ListViewFragmentBuilder(ViewChangeEvent vce, boolean fromNav) {
		super(vce.getParameters(), vce.getViewName());
		setFromNav(fromNav);
	}
	
	protected ListViewFragmentBuilder(String pstr, String viewName) {
		super(pstr, viewName);
	}
	
	public Optional<String> getCurrentSortField() {
		return getParameterValue(SORT_PARAM_NAME);
	}
	
	public boolean isTrashed() {
		return getBoolean(TRASHED_PARAM_NAME);
	}
	
	public ListViewFragmentBuilder setSort(String fname, boolean ascending, Sort defaultSort) {
		String s;
		if (ascending) {
			s = fname;
		} else {
			s = "-" + fname;
		}
		
		Order od = defaultSort.iterator().next();
		
		if (od.getProperty().equals(fname) && ascending == od.isAscending()) {
			getUriCb().replaceQueryParam(SORT_PARAM_NAME);
		} else {
			getUriCb().replaceQueryParam(SORT_PARAM_NAME, s);
		}
		return this;
	}
	
	public Optional<Sort> getSort() {
		
		Optional<String> sOp = getParameterValue(SORT_PARAM_NAME);
		Sort sort = null;
		
		if (sOp.isPresent()) {
			String s = sOp.get();
			if (s.startsWith("-")) {
				sort = new Sort(Direction.DESC, s.substring(1));
			} else {
				sort =  new Sort(Direction.ASC, s); 
			}
		}
		return Optional.ofNullable(sort);
	}
	
	public ListViewFragmentBuilder setCurrentPage(int page) {
		if (page < 2) {
			getUriCb().replaceQueryParam(PAGE_PARAM_NAME);
		} else {
			getUriCb().replaceQueryParam(PAGE_PARAM_NAME, page);
		}
		return this;
	}
	
	public ListViewFragmentBuilder setFilterStr(String filterStr) {
		if (Strings.isNullOrEmpty(filterStr)) {
			getUriCb().replaceQueryParam(FILTER_STR_PARAM_NAME);
		} else {
			getUriCb().replaceQueryParam(FILTER_STR_PARAM_NAME, filterStr);
		}
		
		return this;
	}
	
	public Optional<String> getFilterStr() {
		return getParameterValue(FILTER_STR_PARAM_NAME);
	}
	
	public int getCurrentPage() {
		int i = str2i(getParameterValue(PAGE_PARAM_NAME));
		if (i < 2) {
			return 1;
		} else {
			return i;
		}
	}
	
	public int getPerPage() {
		return str2i(getParameterValue(PER_PAGE_PARAM_NAME));
	}

	
	public boolean isFromNav() {
		return fromNav;
	}

	public void setFromNav(boolean fromNav) {
		this.fromNav = fromNav;
	}

}
