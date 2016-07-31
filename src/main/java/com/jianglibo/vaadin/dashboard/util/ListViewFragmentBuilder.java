package com.jianglibo.vaadin.dashboard.util;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.common.base.Strings;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class ListViewFragmentBuilder {
	
	public static final String PAGE_PARAM_NAME = "p";
	
	public static final String SORT_PARAM_NAME = "sort";
	
	public static final String FILTER_STR_PARAM_NAME = "q";
	
	public static final String TRASHED_PARAM_NAME = "trashed";
	
	private String pstr;
	
	private String viewName;
	
	private UriComponentsBuilder uriCb;
	
	private UriComponents uriComs;
	
	private boolean fromNav = false;
	
	public ListViewFragmentBuilder(ViewChangeEvent vce) {
		setPstr(vce.getParameters());
		setViewName(vce.getViewName());
		setUriCb(UriComponentsBuilder.fromUriString(getPstr()));
		setUriComs(uriCb.build());		
	}
	
	public ListViewFragmentBuilder(ViewChangeEvent vce, boolean fromNav) {
		setPstr(vce.getParameters());
		setFromNav(fromNav);
		setViewName(vce.getViewName());
		setUriCb(UriComponentsBuilder.fromUriString(getPstr()));
		setUriComs(uriCb.build());		
	}
	
	protected ListViewFragmentBuilder(String pstr, String viewName) {
		setPstr(pstr);
		setViewName(viewName);
		setUriCb(UriComponentsBuilder.fromUriString(getPstr()));
		setUriComs(uriCb.build());
	}
	

	
	public ListViewFragmentBuilder setBoolean(String pname, boolean value) {
		if (value) {
			uriCb.replaceQueryParam(pname, value);
		} else {
			uriCb.replaceQueryParam(pname);
		}
		return this;
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
			uriCb.replaceQueryParam(SORT_PARAM_NAME);
		} else {
			uriCb.replaceQueryParam(SORT_PARAM_NAME, s);
		}
		return this;
	}
	
	public Sort getSort() {
		String s = getParameterValue(SORT_PARAM_NAME);
		if (Strings.isNullOrEmpty(s)) {
			return null;
		}
		if (s.startsWith("-")) {
			return new Sort(Direction.DESC, s.substring(1));
		} else {
			return new Sort(Direction.ASC, s); 
		}
	}
	
	public boolean getBoolean(String pname) {
		String bs = getParameterValue(pname);
		if (Strings.isNullOrEmpty(bs)) {
			return false;
		} else {
			if ("true".equalsIgnoreCase(bs) || "yes".equalsIgnoreCase(bs) || "1".equalsIgnoreCase(bs)) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public ListViewFragmentBuilder setString(String pname, String value) {
		if (Strings.isNullOrEmpty(value)) {
			uriCb.replaceQueryParam(pname);
		} else {
			uriCb.replaceQueryParam(pname, value);
		}
		return this;
	}
	
	
	public ListViewFragmentBuilder setCurrentPage(int page) {
		if (page < 2) {
			uriCb.replaceQueryParam(PAGE_PARAM_NAME);
		} else {
			uriCb.replaceQueryParam(PAGE_PARAM_NAME, page);
		}
		return this;
	}
	
	private Integer str2i(String stri) {
		try {
			return Integer.valueOf(stri);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	public String toNavigateString() {
		return viewName + "/" + build().toUriString();
	}
	
	public ListViewFragmentBuilder setFilterStr(String filterStr) {
		if (Strings.isNullOrEmpty(filterStr)) {
			uriCb.replaceQueryParam(FILTER_STR_PARAM_NAME);
		} else {
			uriCb.replaceQueryParam(FILTER_STR_PARAM_NAME, filterStr);
		}
		
		return this;
	}
	
	public String getFilterStr() {
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
	
//	public ViewFragmentBuilder increasePage() {
//		String page = getParameterValue(PAGE_PARAM_NAME);
//		int i = str2i(page);
//		i = i + 1;
//		if (i < 2) {
//			i = 2;
//		}
//		uriCb.replaceQueryParam(PAGE_PARAM_NAME, i);
//		return this;
//	}
//	
//	public ViewFragmentBuilder decreasePage() {
//		String page = getParameterValue(PAGE_PARAM_NAME);
//		int i = str2i(page);
//
//		i = i - 1;
//		
//		if (i < 2) {
//			uriCb.replaceQueryParam(PAGE_PARAM_NAME);
//		} else {
//			uriCb.replaceQueryParam(PAGE_PARAM_NAME, i);
//		}
//		return this;
//	}

	
	private String getParameterValue(String pname) {
		if (uriComs.getQueryParams().containsKey(pname)) {
			return uriComs.getQueryParams().getFirst(pname);
		} else {
			return "";
		}
	}
	
	public UriComponents build() {
		return uriCb.build();
	}

	public String getPstr() {
		return pstr;
	}

	public void setPstr(String pstr) {
		this.pstr = pstr;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}



	public UriComponentsBuilder getUriCb() {
		return uriCb;
	}

	public void setUriCb(UriComponentsBuilder uriCb) {
		this.uriCb = uriCb;
	}

	public UriComponents getUriComs() {
		return uriComs;
	}

	public void setUriComs(UriComponents uriComs) {
		this.uriComs = uriComs;
	}

	public boolean isFromNav() {
		return fromNav;
	}

	public void setFromNav(boolean fromNav) {
		this.fromNav = fromNav;
	}

}
