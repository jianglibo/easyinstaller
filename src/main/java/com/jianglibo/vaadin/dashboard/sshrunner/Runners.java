package com.jianglibo.vaadin.dashboard.sshrunner;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;
import com.google.gwt.thirdparty.guava.common.collect.Maps;
import com.jianglibo.vaadin.dashboard.GlobalComboOptions;
import com.jianglibo.vaadin.dashboard.annotation.Runner;

@Component
public class Runners {

	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private GlobalComboOptions globalComboOptions;
	
	private Map<String, BaseRunner> runners = Maps.newHashMap();
	
	@PostConstruct
	public void post() {
		Map<String, Object> runnerBeans = applicationContext.getBeansWithAnnotation(Runner.class);
		Set<String> nkeys = Sets.newHashSet();
		for(Entry<String, Object> en : runnerBeans.entrySet()) {
			Runner runner = runnerBeans.get(en.getKey()).getClass().getAnnotation(Runner.class);
			nkeys.add(runner.value());
			runners.put(runner.value(), (BaseRunner) en.getValue());
		}
		globalComboOptions.getWholeStringMap().put(GlobalComboOptions.RUNNERS, nkeys);
	}

	public Map<String, BaseRunner> getRunners() {
		return runners;
	}

	public void setRunners(Map<String, BaseRunner> runners) {
		this.runners = runners;
	}
}
