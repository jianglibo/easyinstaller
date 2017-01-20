package com.jianglibo.vaadin.dashboard.view.menuatleft;

import java.util.List;
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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jianglibo.vaadin.dashboard.annotation.MainMenu;
import com.vaadin.spring.annotation.SpringComponent;

/**
 * 
 * @author jianglibo@gmail.com
 *
 */
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MainMenuItems {
	private static final Logger LOGGER = LoggerFactory.getLogger(MainMenuItems.class);

	private final ApplicationContext applicationContext;
	
	private Map<String, MenuItemWrapper> menuMap = Maps.newHashMap();
	
	private List<MenuItemWrapper> orderedItems;
	
	@Autowired
	public MainMenuItems(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	@PostConstruct
	void after() {
		SortedMap<Integer, MenuItemWrapper> items = Maps.newTreeMap();
		Map<String, Object> wrappers =  applicationContext.getBeansWithAnnotation(MainMenu.class);
		
		for(Entry<String, Object> entry : wrappers.entrySet()) {
			MainMenu mm = entry.getValue().getClass().getAnnotation(MainMenu.class);
			int i = mm.menuOrder();
			while(items.containsKey(i)) {
				i++;
			}
			MenuItemWrapper miw = (MenuItemWrapper) entry.getValue(); 
			items.put(i, miw);
			menuMap.put(miw.getClass().getName(), miw);
		}
		setOrderedItems(Lists.newArrayList(items.values()));
	}

	public List<MenuItemWrapper> getOrderedItems() {
		return orderedItems;
	}

	public void setOrderedItems(List<MenuItemWrapper> orderedItems) {
		this.orderedItems = orderedItems;
	}

	public Map<String, MenuItemWrapper> getMenuMap() {
		return menuMap;
	}

	public void setMenuMap(Map<String, MenuItemWrapper> menuMap) {
		this.menuMap = menuMap;
	}
}
