package com.jianglibo.vaadin.dashboard.uicomponent.gridfield;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.GridFieldDescription;
import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

@SuppressWarnings("serial")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class GridField<T extends Collection<? extends BaseEntity>> extends CustomField<T>{
	
	private GridFieldDescription dfd;
	
	@Autowired
	private ApplicationContext applicationContext;
	

	public GridField<Collection<? extends BaseEntity>> afterInjection(VaadinTableWrapper vtw,
			VaadinFormFieldWrapper vffw) {
		this.dfd = vffw.getReflectField().getAnnotation(GridFieldDescription.class);
		return (GridField<Collection<? extends BaseEntity>>) this;
	}

	@Override
	protected Component initContent() {
		return applicationContext.getBean(GridFieldGrid.class).afterInjection(dfd);
	}

	@Override
	public Class<? extends T> getType() {
		return (Class<? extends T>) Collection.class;
	}


}
