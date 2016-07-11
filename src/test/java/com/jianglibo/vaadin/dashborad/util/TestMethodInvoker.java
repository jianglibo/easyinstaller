package com.jianglibo.vaadin.dashborad.util;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import com.jianglibo.vaadin.dashboard.util.MethodInvoker;

public class TestMethodInvoker {

	@Test
	public void t() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		MethodInvoker mik = new MethodInvoker(AclassToInvoke.class, "sum", int.class, int.class);
		Object o = mik.invokeStatic(1, 2);
		assertThat(o, equalTo(3));
	}

}
