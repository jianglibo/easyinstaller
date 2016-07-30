package com.jianglibo.vaadin.dashboard.util;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.common.base.Strings;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class ItemViewFragmentBuilder {
	
	public static final String PREVIOUS_VIEW_PARAMETER_NAME = "pv";
	
	private String pstr;
	
	private String viewName;
	
	private UriComponentsBuilder uriCb;
	
	private UriComponents uriComs;
	
	public ItemViewFragmentBuilder(String pstr, String viewName) {
		setPstr(pstr);
		setViewName(viewName);
		setUriCb(UriComponentsBuilder.fromUriString(getPstr()));
		setUriComs(uriCb.build());
	}
	
	public String getPreviousView() {
		return getParameterValue(PREVIOUS_VIEW_PARAMETER_NAME);
	}
	
	public ItemViewFragmentBuilder(ViewChangeEvent vce) {
		setPstr(vce.getParameters());
		setViewName(vce.getViewName());
		setUriCb(UriComponentsBuilder.fromUriString(getPstr()));
		setUriComs(uriCb.build());		
	}
	
	public ItemViewFragmentBuilder setBoolean(String pname, boolean value) {
		if (value) {
			uriCb.replaceQueryParam(pname, value);
		} else {
			uriCb.replaceQueryParam(pname);
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
	
	public ItemViewFragmentBuilder setString(String pname, String value) {
		if (Strings.isNullOrEmpty(value)) {
			uriCb.replaceQueryParam(pname);
		} else {
			uriCb.replaceQueryParam(pname, value);
		}
		return this;
	}
	
	private Long str2l(String stri) {
		try {
			return Long.valueOf(stri);
		} catch (NumberFormatException e) {
			return 0L;
		}
	}
	
	public String toNavigateString() {
		return viewName + "/" + build().toUriString();
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

	public long getBeanId() {
		String idpart = getUriComs().getPath();
		String idstrs[] = idpart.split("/");
		String idstr = idstrs[idstrs.length - 1];
		
		try {
			return Long.valueOf(idstr);
		} catch (NumberFormatException e) {
			return 0;
		}
		
	}

}
