package com.jianglibo.vaadin.dashboard.sshrunner;

import java.util.Map;
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
		Map<String, Object> m = applicationContext.getBeansWithAnnotation(Runner.class);
		Set<String> nkeys = Sets.newHashSet();
		for(String n : m.keySet()) {
			Runner runner = m.get(n).getClass().getAnnotation(Runner.class);
			runners.put(runner.value(), (BaseRunner) m.get(n));
			nkeys.add(runner.value());
		}
		globalComboOptions.getWholeStringMap().put(GlobalComboOptions.RUNNERS, nkeys);
	}
}
