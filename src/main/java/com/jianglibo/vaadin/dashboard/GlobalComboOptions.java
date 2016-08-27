package com.jianglibo.vaadin.dashboard;

import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.google.gwt.thirdparty.guava.common.collect.Maps;
import com.google.gwt.thirdparty.guava.common.collect.Sets;

@Component
public class GlobalComboOptions {
	
	
	public static final String SOFTWARE_NAMES = "softwareNames";
	
	public static final String OS_TYPES = "ostypes";
	
	private Map<String, Set<String>> stringOptions = Maps.newHashMap();
	
	
	public Set<String> getInstallerNames() {
		return getStringOptions(SOFTWARE_NAMES);
	}
	
	public Set<String> getOstypes() {
		return getStringOptions(OS_TYPES);
	}
	
	public Set<String> getStringOptions(String key) {
		if (!stringOptions.containsKey(key)) {
			stringOptions.put(key, Sets.newHashSet());
		} 
		return stringOptions.get(key);
	}

	public Map<String, Set<String>> getStringOptions() {
		return stringOptions;
	}
}
