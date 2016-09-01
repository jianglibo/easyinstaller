package com.jianglibo.vaadin.dashboard.uicomponent.twingrid;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.jianglibo.vaadin.dashboard.event.ui.TwinGridFieldItemClickListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

@SuppressWarnings("serial")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TwinGridField<T extends Collection<? extends BaseEntity>> extends CustomField<T>{
	
	@Autowired
	private ApplicationContext applicationContext;
	
	private VaadinTableWrapper vtw;
	private VaadinFormFieldWrapper vffw;
	
	private TwinGridLayout<T> twinGridLayout;
	
	/**
	 * I don't know when initContent will called.
	 */
	private TwinGridFieldItemClickListener itemClickListener;
	
	public void addItemClickListener(TwinGridFieldItemClickListener itemClickListener) {
		this.itemClickListener = itemClickListener;
		if (twinGridLayout != null) {
			twinGridLayout.addItemClickListener(itemClickListener);
			this.itemClickListener = null;
		}
	}
	
	public TwinGridField<T> afterInjection(VaadinTableWrapper vtw,
			VaadinFormFieldWrapper vffw) {
		this.vtw = vtw;
		this.vffw = vffw;
		return this;
	}

	@Override
	protected Component initContent() {
		twinGridLayout = (TwinGridLayout<T>) applicationContext.getBean(TwinGridLayout.class).afterInjection(vtw, vffw);
		if (itemClickListener != null) {
			twinGridLayout.addItemClickListener(itemClickListener);
		}
		return twinGridLayout;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends T> getType() {
		return (Class<? extends T>) Collection.class;
	}
}
