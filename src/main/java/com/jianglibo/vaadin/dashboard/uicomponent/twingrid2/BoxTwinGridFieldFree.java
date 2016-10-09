package com.jianglibo.vaadin.dashboard.uicomponent.twingrid2;

import java.util.List;
import java.util.Set;

import org.springframework.context.MessageSource;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.repositories.BoxRepository;
import com.jianglibo.vaadin.dashboard.util.ColumnUtil;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Notification;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickEvent;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickListener;

public class BoxTwinGridFieldFree extends BaseTwinGridFieldFree<Set<Box>, Box, Box, BoxContainerInRc> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("serial")
	public BoxTwinGridFieldFree(BoxContainerInRc dContainer, Domains domains, MessageSource messageSource, BoxRepository repository, int leftRowNumber, int rightRowNumber, String leftMessagePrefix, String rightMessagePrefix) {
		super(dContainer, new TypeToken<Set<Box>>() {}, Box.class, messageSource,leftRowNumber, rightRowNumber, leftMessagePrefix, rightMessagePrefix);
		StyleUtil.setBtnLinkStyleContainer(this);
		StyleUtil.setDisableCellFocus(this);
		setValue(Sets.newHashSet());
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
	public void setupLeftGrid(Grid grid) {

	}

	@Override
	public void setupRightGrid(Grid grid) {
	}

	@Override
	protected String[] getLeftSortableColumns() {
		return new String[]{};
	}

	@Override
	protected String[] getLeftColumns() {
		return new String[]{"name", "ip", "!remove" };
	}

	@Override
	protected String[] getRightColumns() {
		return new String[]{"!addtoleft","name", "ip"};
	}

	@Override
	protected List<String> getRightSortableColumns() {
		return Lists.newArrayList();
	}
}
