package com.jianglibo.vaadin.dashboard.util;

import java.util.Optional;

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
	
	public Optional<String> getPreviousView() {
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
		Optional<String> bsOp = getParameterValue(pname);
		if (bsOp.isPresent()) {
			String bs = bsOp.get();
			if ("true".equalsIgnoreCase(bs) || "yes".equalsIgnoreCase(bs) || "1".equalsIgnoreCase(bs)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public long getLong(String pname) {
		return str2l(getParameterValue(pname));
	}
	
	protected Integer str2i(Optional<String> stri) {
		try {
			if (stri.isPresent()) {
				return Integer.valueOf(stri.get());	
			} else {
				return 0;
			}
		} catch (NumberFormatException e) {
			return 0;
		}
	}


	
	protected Long str2l(Optional<String> stri) {
		try {
			if (stri.isPresent()) {
				return Long.valueOf(stri.get());
			} else {
				return 0L;
			}
			
		} catch (NumberFormatException e) {
			return 0L;
		}
	}
	
	public String toNavigateString() {
		return getViewName() + "/" + build().toUriString();
	}
	
	protected Optional<String> getParameterValue(String pname) {
		String v = null;
		if (getUriComs().getQueryParams().containsKey(pname)) {
			v =  getUriComs().getQueryParams().getFirst(pname);
		}
		return Optional.ofNullable(v);
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
