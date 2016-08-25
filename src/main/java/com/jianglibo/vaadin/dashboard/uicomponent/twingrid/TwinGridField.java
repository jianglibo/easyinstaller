package com.jianglibo.vaadin.dashboard.uicomponent.twingrid;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

@SuppressWarnings("serial")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TwinGridField<T extends Collection<? extends BaseEntity>> extends CustomField<T>{
	
	private Class<T> clazz;
	
	private int perPage;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	public TwinGridField<T> afterInjection(Class<T> clazz, int perPage) {
		this.clazz = clazz;
		this.perPage = perPage;
		return this;
	}

	@Override
	protected Component initContent() {
		return applicationContext.getBean(TwinGridLayout.class).afterInjection(clazz, perPage);
	}

	@Override
	public Class<? extends T> getType() {
		return (Class<? extends T>) Collection.class;
	}
}
