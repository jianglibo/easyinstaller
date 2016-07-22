package com.jianglibo.vaadin.dashboard.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

public class NestBeanPopulater {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T populate(T o,Map map, String...nestedProperties) {
		try {
			for(String np: nestedProperties) {
				Map nm =  (Map) map.get(np);
				Class<?> c = PropertyUtils.getPropertyType(o, np);
				Object no = c.newInstance();
				BeanUtils.populate(no, nm);
				map.put(np, no);
			}
			BeanUtils.populate(o, map);
			return o;
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();

		}
		return null;
	}
	
	@SuppressWarnings({ "unused", "rawtypes" })
	private static void changeMap(Map parentMap,Object parentObj, String propertyName) {
		
	}
}
