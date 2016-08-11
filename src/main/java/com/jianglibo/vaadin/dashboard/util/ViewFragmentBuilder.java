package com.jianglibo.vaadin.dashboard.util;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.common.base.Strings;

public class ViewFragmentBuilder {
	
	public static final String PREVIOUS_VIEW_PARAMETER_NAME = "pv";

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
	
	public String getPreviousView() {
		return getParameterValue(PREVIOUS_VIEW_PARAMETER_NAME);
	}
	
	public ViewFragmentBuilder setString(String pname, String value) {
		if (Strings.isNullOrEmpty(value)) {
			getUriCb().replaceQueryParam(pname);
		} else {
			getUriCb().replaceQueryParam(pname, value);
		}
		return this;
	}
	
	public ViewFragmentBuilder setBoolean(String pname, boolean value) {
		if (value) {
			getUriCb().replaceQueryParam(pname, value);
		} else {
			getUriCb().replaceQueryParam(pname);
		}
		return this;
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
	
	public long getLong(String pname) {
		return str2l(getParameterValue(pname));
	}
	
	protected Integer str2i(String stri) {
		try {
			return Integer.valueOf(stri);
		} catch (NumberFormatException e) {
			return 0;
		}
	}


	
	protected Long str2l(String stri) {
		try {
			return Long.valueOf(stri);
		} catch (NumberFormatException e) {
			return 0L;
		}
	}
	
	public String toNavigateString() {
		return getViewName() + "/" + build().toUriString();
	}
	
	protected String getParameterValue(String pname) {
		if (getUriComs().getQueryParams().containsKey(pname)) {
			return getUriComs().getQueryParams().getFirst(pname);
		} else {
			return "";
		}
	}
	
	public UriComponents build() {
		return getUriCb().build();
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
