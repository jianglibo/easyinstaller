package com.jianglibo.vaadin.dashboard.ssh;

import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import com.jianglibo.vaadin.dashboard.domain.StepRun;

/**
 * If StepDefine is a template, Then StepConfig like as a instance. If any properties has no value, the value will come from StepDefine.
 * @author jianglibo@gmail.com
 *
 */
public class StepConfig {

	private static final Yaml yaml = new Yaml();

	private Map<String, Object> map;

	@SuppressWarnings("unchecked")
	public StepConfig(String s) {
		this.map = (Map<String, Object>) yaml.load(s);
	}
	
	public static StepConfig createStepConfig(StepRun stepRun) {
		return new StepConfig(stepRun.getInfoContent());
	}
	
//	public static UploadDescription getUploadDescription(StepRun stepRun) {
//		return yaml.loadAs(stepRun.getInfoContent(), UploadDescription.class);
//	}

	public String getStringValue(String... keys) {
		Map<String, Object> localMap = map;
		int idx = keys.length - 1;
		for (int i = 0; i < idx; i++) {
			localMap = (Map<String, Object>) localMap.get(keys[i]);
			if (localMap == null) {
				return null;
			}
		}
		return (String) localMap.get(keys[idx]);
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
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
