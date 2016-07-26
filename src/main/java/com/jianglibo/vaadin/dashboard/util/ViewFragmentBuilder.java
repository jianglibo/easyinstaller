package com.jianglibo.vaadin.dashboard.util;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.common.base.Strings;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class ViewFragmentBuilder {
	
	public static final String PAGE_PARAM_NAME = "p";
	
	public static final String FILTER_STR_PARAM_NAME = "q";
	
	private String pstr;
	
	private String viewName;
	
	private UriComponentsBuilder uriCb;
	
	private UriComponents uriComs;
	
	public ViewFragmentBuilder(String pstr, String viewName) {
		setPstr(pstr);
		setViewName(viewName);
		setUriCb(UriComponentsBuilder.fromUriString(getPstr()));
		setUriComs(uriCb.build());
	}
	
	public ViewFragmentBuilder(ViewChangeEvent vce) {
		setPstr(vce.getParameters());
		setViewName(vce.getViewName());
		setUriCb(UriComponentsBuilder.fromUriString(getPstr()));
		setUriComs(uriCb.build());		
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
	
	public ViewFragmentBuilder setFilterStr(String filterStr) {
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
	
	public ViewFragmentBuilder increasePage() {
		String page = getParameterValue(PAGE_PARAM_NAME);
		int i = str2i(page);
		i = i + 1;
		if (i < 2) {
			i = 2;
		}
		uriCb.replaceQueryParam(PAGE_PARAM_NAME, i);
		return this;
	}
	
	public ViewFragmentBuilder decreasePage() {
		String page = getParameterValue(PAGE_PARAM_NAME);
		int i = str2i(page);

		i = i - 1;
		
		if (i < 2) {
			uriCb.replaceQueryParam(PAGE_PARAM_NAME);
		} else {
			uriCb.replaceQueryParam(PAGE_PARAM_NAME, i);
		}
		return this;
	}

	
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

}
