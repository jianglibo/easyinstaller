package com.jianglibo.vaadin.dashboard.util;

public class ReflectUtil {
	
	public static String getStaticString(Class<?> clazz, String fieldName) {
		try {
			return (String) clazz.getField(fieldName).get(null);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getDomainName(Class<?> clazz) {
		return getStaticString(clazz, "DOMAIN_NAME");
	}

}
