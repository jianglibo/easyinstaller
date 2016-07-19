package com.jianglibo.vaadin.dashboard.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ConfigChangeListenner {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigChangeListenner.class);

	@EventListener({ContextStartedEvent.class/*, ContextRefreshedEvent.class*/})
	public void onApplicationEvent(ApplicationEvent event) {
		LOGGER.info("got event {}", event.getClass().getName());
	}


}
