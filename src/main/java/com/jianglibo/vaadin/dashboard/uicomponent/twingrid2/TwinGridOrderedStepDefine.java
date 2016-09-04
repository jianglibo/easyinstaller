package com.jianglibo.vaadin.dashboard.uicomponent.twingrid2;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.data.vaadinconverter.EntityStringConverter;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.OrderedStepDefine;
import com.jianglibo.vaadin.dashboard.domain.StepDefine;
import com.jianglibo.vaadin.dashboard.repositories.OrderedStepDefineRepository;
import com.vaadin.data.Item;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.renderers.HtmlRenderer;

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
		super(OrderedStepDefine.class, StepDefine.class, domains, messageSource);
		this.orderedStepDefineRepository = orderedStepDefineRepository;
	}
	
	@Override
	public TwinGridOrderedStepDefine afterInjection(VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw) {
		return (TwinGridOrderedStepDefine) super.afterInjection(vtw, vffw);
	}
	
	public TwinGridOrderedStepDefine done() {
		return this;
	}

	@SuppressWarnings("serial")
	@Override
	public void addGeneratedPropertyForLeft(GeneratedPropertyContainer gpcontainer, String extraName) {
		switch (extraName) {
		case "!removefromleft":
			gpcontainer.addGeneratedProperty(extraName, new PropertyValueGenerator<String>() {
				@Override
				public String getValue(Item item, Object itemId, Object propertyId) {
					return FontAwesome.ARROW_CIRCLE_RIGHT.getHtml();
				}
				@Override
				public Class<String> getType() {
					return String.class;
				}
			});
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("serial")
	@Override
	public void addGeneratedPropertyForRight(GeneratedPropertyContainer gpcontainer, String extraName) {
		switch (extraName) {
		case "!addtoleft":
			gpcontainer.addGeneratedProperty(extraName, new PropertyValueGenerator<String>() {
				@Override
				public String getValue(Item item, Object itemId, Object propertyId) {
					return FontAwesome.ARROW_CIRCLE_LEFT.getHtml();
				}
				@Override
				public Class<String> getType() {
					return String.class;
				}
			});
			break;
		default:
			break;
		}
	}

	@Override
	public void setupLeftColumn(Column col, String name) {
		switch (name) {
		case "!removefromleft":
			col.setRenderer(new HtmlRenderer(""));
			break;
		case "stepDefine":
			col.setConverter(new EntityStringConverter<>(StepDefine.class));
			break;
		default:
			break;
		}
	}

	@Override
	public void setupRightColumn(Column col, String name) {
		switch (name) {
		case "!addtoleft":
			col.setRenderer(new HtmlRenderer(""));
			break;
		default:
			break;
		}
	}

	@Override
	public void whenLeftItemClicked(ItemClickEvent event) {
		List<OrderedStepDefine> osds = (List<OrderedStepDefine>) getValue();
		List<OrderedStepDefine> newOsds = Lists.newArrayList(osds);
		OrderedStepDefine osd = (OrderedStepDefine) event.getItemId();
		if (newOsds.remove(osd)) {
			orderedStepDefineRepository.delete(osd);
		}
		setValue(newOsds);
	}

	@Override
	public void whenRightItemClicked(ItemClickEvent event) {
		List<OrderedStepDefine> osds = (List<OrderedStepDefine>) getValue();
		List<OrderedStepDefine> newOsds = Lists.newArrayList(osds);
		StepDefine sd = (StepDefine) event.getItemId();
		int position = 0;
		if (!osds.isEmpty()) {
			position = osds.get(osds.size() - 1).getPosition() + 50;
		}
		OrderedStepDefine osd = orderedStepDefineRepository.save(new OrderedStepDefine(sd, position));
		newOsds.add(osd);
		setValue(newOsds);
	}
}
