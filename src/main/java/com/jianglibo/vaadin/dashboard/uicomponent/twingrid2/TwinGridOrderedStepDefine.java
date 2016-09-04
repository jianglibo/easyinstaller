package com.jianglibo.vaadin.dashboard.uicomponent.twingrid2;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;

import com.google.common.collect.Collections2;
import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.OrderedStepDefine;
import com.jianglibo.vaadin.dashboard.domain.StepDefine;
import com.jianglibo.vaadin.dashboard.event.ui.TwinGridFieldItemClickEvent;
import com.jianglibo.vaadin.dashboard.repositories.OrderedStepDefineRepository;
import com.vaadin.spring.annotation.SpringComponent;

@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TwinGridOrderedStepDefine extends BaseTwinGridField<List<OrderedStepDefine>, OrderedStepDefine, StepDefine> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private OrderedStepDefineRepository orderedStepDefineRepository;
	
	@Autowired
	public TwinGridOrderedStepDefine(Domains domains, MessageSource messageSource, OrderedStepDefineRepository orderedStepDefineRepository) {
		super(OrderedStepDefine.class, new String[]{"position", "stepDefine"}, StepDefine.class, new String[]{"name", "ostype"}, domains, messageSource);
		this.orderedStepDefineRepository = orderedStepDefineRepository;
	}
	
	public TwinGridOrderedStepDefine afterInjection(VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw) {
		buildTwinGridContent(vtw, vffw);
		return this;
	}
	
	public TwinGridOrderedStepDefine done() {
		return this;
	}

	@Override
	public void itemClicked(TwinGridFieldItemClickEvent twinGridFieldItemClickEvent) {
		List<OrderedStepDefine> osds = (List<OrderedStepDefine>) getValue();
		List<OrderedStepDefine> newOsds = Lists.newArrayList(osds);
		if (twinGridFieldItemClickEvent.isLeftClicked()) {
			OrderedStepDefine osd = (OrderedStepDefine) twinGridFieldItemClickEvent.getItemValue();
			newOsds.remove(osd);
		} else {
			StepDefine sd = (StepDefine) twinGridFieldItemClickEvent.getItemValue();
			int position = 0;
			if (!osds.isEmpty()) {
				position = osds.get(osds.size() - 1).getPosition() + 50;
			}
			OrderedStepDefine osd = orderedStepDefineRepository.save(new OrderedStepDefine(sd, position));
			newOsds.add(osd);
		}
		setValue(newOsds);
	}

}
