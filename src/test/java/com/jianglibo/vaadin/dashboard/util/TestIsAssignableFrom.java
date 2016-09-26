package com.jianglibo.vaadin.dashboard.util;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestIsAssignableFrom {
	
	protected class A {
		
	}
	
	protected class B extends A {
		
	}
	
	@Test
	public void t() {
		assertTrue(A.class.isAssignableFrom(B.class));
		
		assertTrue(!B.class.isAssignableFrom(A.class));
	}


}
