package com.jianglibo.vaadin.dashboard.view;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import com.google.common.collect.Maps;
import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.annotation.MainMenu;
import com.jianglibo.vaadin.dashboard.util.ClassScanner;
import com.jianglibo.vaadin.dashboard.util.MethodInvoker;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.internal.Conventions;
import com.vaadin.ui.Component;

@SpringComponent
@Scope("prototype")
public class MainMenuItems {
	private static final Logger LOGGER = LoggerFactory.getLogger(MainMenuItems.class);

	private final ApplicationContext applicationContext;
	
	List<MenuItemWrapper> items = Lists.newArrayList();
	
	@Autowired
	public MainMenuItems(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	@PostConstruct
	void after() {
		Map<String, Object> wrappers =  applicationContext.getBeansWithAnnotation(MainMenu.class);
		Map<Integer, Object> orderWrap = Maps.newHashMap();
		
		for(Entry<String, Object> entry : wrappers.entrySet()) {
			MainMenu mm = entry.getValue().getClass().getAnnotation(MainMenu.class);
			int i = mm.menuOrder();
			while (orderWrap.containsKey(i)) {
				i++;
			}
			orderWrap.put(i, entry.getValue());
			
		}
		Integer[] orders = new Integer[orderWrap.keySet().size()];
		orderWrap.keySet().toArray(orders);
		Arrays.sort(orders);
		
		for(Integer i: orders) {
			items.add((MenuItemWrapper) orderWrap.get(i));
		}
		
	}
	
//	@PostConstruct
//	void init() {
//        final String[] viewBeanNames = applicationContext
//                .getBeanNamesForAnnotation(SpringView.class);
//        for (String beanName : viewBeanNames) {
//            final Class<?> type = applicationContext.getType(beanName);
//            if (View.class.isAssignableFrom(type)) {
//                final SpringView annotation = applicationContext
//                        .findAnnotationOnBean(beanName, SpringView.class);
//                final String viewName = getViewNameFromAnnotation(type,
//                        annotation);
//                
//                try {
//					MethodInvoker mi = new MethodInvoker(type, "getMenuItem");
//					Object o = mi.invokeStatic();
//					items.put(viewName, (Component) o);
//				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
//						| InvocationTargetException e) {
//					LOGGER.info("The view bean [{}] does not has getMenuItem static method.");
//				}
//
//            } else {
//                LOGGER.error("The view bean [{}] does not implement View",
//                        beanName);
//                throw new IllegalStateException("SpringView bean [" + beanName
//                        + "] must implement View");
//            }
//        } 
//	}

//	protected String getViewNameFromAnnotation(Class<?> beanClass, SpringView annotation) {
//		return Conventions.deriveMappingForView(beanClass, annotation);
//	}
	
	public List<MenuItemWrapper> getItems() {
		return items;
	}

}
