package com.jianglibo.vaadin.dashboard.uicomponent.twingrid2;

import java.util.Set;

import org.springframework.context.MessageSource;

import com.google.common.collect.Sets;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.repositories.BoxRepository;
import com.jianglibo.vaadin.dashboard.util.ColumnUtil;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Notification;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickEvent;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickListener;

public class BoxTwinGrid extends BaseTwinGridField<Set<Box>, Box, Box> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BoxTwinGrid(Domains domains, MessageSource messageSource, BoxRepository repository, VaadinTableWrapper vtw,
			VaadinFormFieldWrapper vffw) {
		super(Box.class, Box.class, domains, messageSource, vtw, vffw);
		StyleUtil.setBtnLinkStyleContainer(this);
		StyleUtil.setDisableCellFocus(this);
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
					Set<Box> currentValue = (Set<Box>) getValue();
					Set<Box> newValue = Sets.newHashSet(currentValue);
					Box item = (Box) event.getItemId();
					newValue.remove(item);
					setValue(newValue);
					Notification.show(item.getDisplayName() + " removed.");
				}
			});
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
					Set<Box> currentValue = (Set<Box>) getValue();
					Set<Box> newValue = Sets.newHashSet(currentValue);
					Box sd = (Box) event.getItemId();
					newValue.add(sd);
					setValue(newValue);
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
