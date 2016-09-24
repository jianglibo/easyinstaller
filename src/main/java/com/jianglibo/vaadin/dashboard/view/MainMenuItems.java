package com.jianglibo.vaadin.dashboard.view;

import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import com.google.common.collect.Maps;
import com.jianglibo.vaadin.dashboard.annotation.MainMenu;
import com.vaadin.spring.annotation.SpringComponent;

@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MainMenuItems {
	private static final Logger LOGGER = LoggerFactory.getLogger(MainMenuItems.class);

	private final ApplicationContext applicationContext;
	
	private SortedMap<Integer, MenuItemWrapper> items = Maps.newTreeMap();
	
	@Autowired
	public MainMenuItems(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	@PostConstruct
	void after() {
		Map<String, Object> wrappers =  applicationContext.getBeansWithAnnotation(MainMenu.class);
		
		for(Entry<String, Object> entry : wrappers.entrySet()) {
			MainMenu mm = entry.getValue().getClass().getAnnotation(MainMenu.class);
			int i = mm.menuOrder();
			while(items.containsKey(i)) {
				i++;
			}
			items.put(i, (MenuItemWrapper) entry.getValue());
		}
	}
	
	public SortedMap<Integer, MenuItemWrapper> getItems() {
		return items;
	}

}
