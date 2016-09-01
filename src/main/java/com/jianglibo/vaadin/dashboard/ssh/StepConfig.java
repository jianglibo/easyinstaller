package com.jianglibo.vaadin.dashboard.ssh;

import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class StepConfig {
	
	private static final Yaml yaml = new Yaml();
	
	private Map<String, Object> map;
	
	@SuppressWarnings("unchecked")
	public StepConfig(String s) {
		this.map = (Map<String, Object>) yaml.load(s);
	}
	
	public String getStringValue(String...keys) {
		String value = null;
		Map<String, Object> localMap = map;
		int idx = keys.length - 1;
		for(int i=0; i < idx ; i++) {
			localMap = (Map<String, Object>) localMap.get(keys[i]);
			if (localMap == null) {
				return null;
			}
		}
		return (String) localMap.get(idx);
	}
	
	public String getRunner() {
		return (String) map.get("runner");
	}
	
	public String getName() {
		return (String) map.get("name");
	}
	
	public String getOstype() {
		return (String) map.get("ostype");	
	}
}
