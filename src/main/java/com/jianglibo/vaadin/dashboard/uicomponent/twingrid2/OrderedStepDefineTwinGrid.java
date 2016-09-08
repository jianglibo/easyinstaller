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
import com.jianglibo.vaadin.dashboard.util.ColumnUtil;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickEvent;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickListener;

@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OrderedStepDefineTwinGrid
		extends BaseTwinGridField<List<OrderedStepDefine>, OrderedStepDefine, StepDefine> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private OrderedStepDefineRepository orderedStepDefineRepository;

	@Autowired
	public OrderedStepDefineTwinGrid(Domains domains, MessageSource messageSource,
			OrderedStepDefineRepository orderedStepDefineRepository) {
		super(OrderedStepDefine.class, StepDefine.class, domains, messageSource);
		this.orderedStepDefineRepository = orderedStepDefineRepository;
		StyleUtil.setBtnLinkStyleContainer(this);
		StyleUtil.setDisableCellFocus(this);
	}

	@Override
	public OrderedStepDefineTwinGrid afterInjection(VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw) {
		return (OrderedStepDefineTwinGrid) super.afterInjection(vtw, vffw);
	}

	public OrderedStepDefineTwinGrid done() {
		return this;
	}

	@Override
	public void addGeneratedPropertyForLeft(GeneratedPropertyContainer gpcontainer, String extraName) {
		switch (extraName) {
		default:
			ColumnUtil.addGeneratedPropertyWithEmptyContent(gpcontainer, extraName);
			break;
		}
	}

	@Override
	public void addGeneratedPropertyForRight(GeneratedPropertyContainer gpcontainer, String extraName) {
		switch (extraName) {
		default:
			ColumnUtil.addGeneratedPropertyWithEmptyContent(gpcontainer, extraName);
			break;
		}
	}

	@SuppressWarnings("serial")
	@Override
	public void setupLeftColumn(Column col, String name) {
		switch (name) {
		case "!remove":
			ColumnUtil.setExternalImageRender(col, ColumnUtil.REMOVE_URL, new RendererClickListener() {
				@Override
				public void click(RendererClickEvent event) {
					List<OrderedStepDefine> osds = (List<OrderedStepDefine>) getValue();
					List<OrderedStepDefine> newOsds = Lists.newArrayList(osds);
					OrderedStepDefine osd = (OrderedStepDefine) event.getItemId();
					if (newOsds.remove(osd)) {
						orderedStepDefineRepository.delete(osd);
					}
					setValue(newOsds);
					Notification.show(osd.getDisplayName() + " removed.");
				}
			});
			break;
		case "!up":
			ColumnUtil.setExternalImageRender(col, ColumnUtil.ARROW_UP_URL, new RendererClickListener() {
				@Override
				public void click(RendererClickEvent event) {
					OrderedStepDefine osd = (OrderedStepDefine) event.getItemId();
					setValue(ColumnUtil.alterHasPositionList(getValue(), osd));
				}
			});
			break;
		case "stepDefine":
			col.setConverter(new EntityStringConverter<>(StepDefine.class));
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("serial")
	@Override
	public void setupRightColumn(Column col, String name) {
		switch (name) {
		case "!addtoleft":
			ColumnUtil.setExternalImageRender(col, ColumnUtil.ARROW_LEFT_URL, new RendererClickListener() {
				@Override
				public void click(RendererClickEvent event) {
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
			});
			break;
		default:
			break;
		}
	}

	@Override
	public void whenLeftItemClicked(ItemClickEvent event) {
	}

	@Override
	public void whenRightItemClicked(ItemClickEvent event) {
	}

	@Override
	public void setupLeftGrid(Grid grid) {
		
	}

	@Override
	public void setupRightGrid(Grid grid) {
	}
}
