package com.jianglibo.vaadin.dashboard.uicomponent.twingrid;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.TwinGridFieldDescription;
import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

@SuppressWarnings("serial")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TwinGridLayout<T extends Collection<? extends BaseEntity>> extends HorizontalLayout {
	
	@Autowired
	private ApplicationContext applicationContext;

	@SuppressWarnings("unchecked")
	public Component afterInjection(VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw) {
		TwinGridFieldDescription tgfd = vffw.getReflectField().getAnnotation(TwinGridFieldDescription.class);
		
		TwinGridLeft<T> left = applicationContext.getBean(TwinGridLeft.class).afterInjection(vffw, tgfd);
		TwinGridRight<T> right = applicationContext.getBean(TwinGridRight.class).afterInjection(vffw, tgfd);
		
		addComponent(left);
		addComponent(right);
		
		setExpandRatio(left, 1);
		setExpandRatio(right, 1);
		return this;
	}
	


}
