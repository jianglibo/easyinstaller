package com.jianglibo.vaadin.dashboard.util;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.jianglibo.vaadin.dashboard.Tbase;

public class TestBeanName extends Tbase {

	@Autowired
	private ApplicationContext applicationContext;
	
	@Test
	public void t() {
		String[] bns = applicationContext.getBeanDefinitionNames();
		assertThat(Arrays.asList(bns), hasItem(AbeanHasAnameSingleton.ABBN));
		assertThat(Arrays.asList(bns), not(hasItem("a" + AbeanHasAnameSingleton.class.getSimpleName().substring(1))));
		
		assertThat(Arrays.asList(bns), hasItem(AbeanHasAnamePrototype.ABBN));
		assertThat(Arrays.asList(bns), not(hasItem("a" + AbeanHasAnamePrototype.class.getSimpleName().substring(1))));

		
	}
}
