package com.jianglibo.vaadin.dashboard.ssh;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.jianglibo.vaadin.dashboard.Tbase;
import com.jianglibo.vaadin.dashboard.Tutil;
import com.jianglibo.vaadin.dashboard.util.ClassScanner;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.navigator.SpringViewProvider;

public class TestSpringView extends Tbase {
	
	@Autowired
	private SpringViewProvider provider;
	
	@Test
	public void t() throws ClassNotFoundException, IOException {
		assertTrue(true);
//		Map<String, Object> svs = context.getBeansWithAnnotation(SpringView.class);
		View view = provider.getView("dashboard");
		PathMatchingResourcePatternResolver prr = new PathMatchingResourcePatternResolver();
		List<Class<?>> css = ClassScanner.findAnnotatedBy("com.jianglibo.vaadin.dashboard.unused", SpringView.class);
		
		Tutil.printme(css.size());
	}
	
	


}
