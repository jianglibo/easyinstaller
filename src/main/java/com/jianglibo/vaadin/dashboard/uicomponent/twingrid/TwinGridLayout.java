package com.jianglibo.vaadin.dashboard.uicomponent.twingrid;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.HorizontalLayout;

@SuppressWarnings("serial")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TwinGridLayout<T extends Collection<? extends BaseEntity>> extends HorizontalLayout {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	private TwinGridLeft<T> left;
	
	private TwinGridRight<T> right;

	public TwinGridLayout<T> afterInjection(Class<T> clazz, int perPage) {
		left = applicationContext.getBean(TwinGridLeft.class);
		right = applicationContext.getBean(TwinGridRight.class).afterInjection(clazz, perPage);
		
		addComponent(left);
		addComponent(right);
		
		setExpandRatio(left, 1);
		setExpandRatio(right, 1);
		return this;
	}

	public TwinGridLeft<T> getLeft() {
		return left;
	}

	public TwinGridRight<T> getRight() {
		return right;
	}
}
