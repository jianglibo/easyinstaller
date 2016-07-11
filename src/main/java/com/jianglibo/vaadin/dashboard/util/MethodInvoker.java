package com.jianglibo.vaadin.dashboard.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodInvoker {
	
	private Method m;
	
	public MethodInvoker(Class<?> c, String mname, Class<?>...cls) throws NoSuchMethodException, SecurityException {
		m = c.getDeclaredMethod(mname, cls);
	}
	
	public Object invoke(Object...params) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return m.invoke(null, params);
	}
	
	public Object invokeStatic(Object...params) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return m.invoke(null, params);
	}


}
