package com.jianglibo.vaadin.dashboard.util;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.jianglibo.vaadin.dashboard.annotation.MainMenu;
import com.jianglibo.vaadin.dashboard.util.MethodInvoker;
import com.jianglibo.vaadin.dashboard.view.dashboard.DashboardViewMenuItem;

public class TestMethodInvoker {

	@Test
	public void t() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		MethodInvoker mik = new MethodInvoker(AclassToInvoke.class, "sum", int.class, int.class);
		Object o = mik.invokeStatic(1, 2);
		assertThat(o, equalTo(3));
	}
	
	@Test
	public void tpackageName() {
		String ps = this.getClass().getPackage().getName();
		assertThat(ps, equalTo("com.jianglibo.vaadin.dashboard.util"));
	}
	
	@Test
	public void anno() {
		assertNotNull("should has @MainMenu annotation.", DashboardViewMenuItem.class.getAnnotation(MainMenu.class));
	}
	
	@Test
	public void toary() {
		Map<Integer, String> m = Maps.newHashMap();
		m.put(1, "a");
		m.put(2, "b");
		Integer[] is = new Integer[m.keySet().size()];
		m.keySet().toArray(is);
		assertThat(is.length, equalTo(2));
		assertThat(Arrays.asList(is), containsInAnyOrder(1, 2));
	}

}
