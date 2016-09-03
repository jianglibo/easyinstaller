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
import com.jianglibo.vaadin.dashboard.event.ui.TwinGridFieldItemClickListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

@SuppressWarnings("serial")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TwinGridLayout<T extends Collection<? extends BaseEntity>> extends HorizontalLayout {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	private TwinGridLeft<T> left;
	
	private TwinGridRight<T> right;

	@SuppressWarnings("unchecked")
	public Component afterInjection(VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw) {
		setWidth("100%");
		TwinGridFieldDescription tgfd = vffw.getReflectField().getAnnotation(TwinGridFieldDescription.class);
		
		setLeft(applicationContext.getBean(TwinGridLeft.class).afterInjection(vffw, tgfd, this));
		setRight(applicationContext.getBean(TwinGridRight.class).afterInjection(vffw, tgfd, this));
		
		addComponent(left);
		addComponent(right);
		
		setExpandRatio(left, 1);
		setExpandRatio(right, 1);
		return this;
	}
	
	public void addItemClickListener(TwinGridFieldItemClickListener itemClickListener) {
		getLeft().addItemClickListener(itemClickListener);
		getRight().addItemClickListener(itemClickListener);
	}
	
	public void refreshValue() {
		getLeft().getFreeContainer().refreshWindow(0);
	}

	public TwinGridLeft<T> getLeft() {
		return left;
	}

	public void setLeft(TwinGridLeft<T> left) {
		this.left = left;
	}

	public TwinGridRight<T> getRight() {
		return right;
	}

	public void setRight(TwinGridRight<T> right) {
		this.right = right;
	}
}
