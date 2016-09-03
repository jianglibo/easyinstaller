package com.jianglibo.vaadin.dashboard.uicomponent.twingrid2;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;

import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.OrderedStepDefine;
import com.jianglibo.vaadin.dashboard.domain.StepDefine;
import com.vaadin.spring.annotation.SpringComponent;

@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TwinGridOrderedStepDefine extends BaseTwinGridField<List<OrderedStepDefine>, OrderedStepDefine, StepDefine> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	public TwinGridOrderedStepDefine(Domains domains, MessageSource messageSource) {
		super(OrderedStepDefine.class, new String[]{"position", "stepDefine"}, StepDefine.class, new String[]{"name", "ostype"}, domains, messageSource);
	}
	
	public TwinGridOrderedStepDefine afterInjection(VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw) {
		buildTwinGridContent(vtw, vffw);
		return this;
	}
	
	@Override
	protected FreeContainer<OrderedStepDefine> createLeftContainer(VaadinFormFieldWrapper vffw) {
		return super.createLeftContainer(vffw);
	}
	
	public TwinGridOrderedStepDefine done() {
		return this;
	}

}
