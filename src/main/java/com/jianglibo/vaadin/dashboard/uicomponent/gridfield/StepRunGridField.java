package com.jianglibo.vaadin.dashboard.uicomponent.gridfield;

import java.util.List;

import org.springframework.context.MessageSource;

import com.google.common.reflect.TypeToken;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.unused.StepRun;
import com.jianglibo.vaadin.dashboard.util.ColumnUtil;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Notification;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickEvent;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickListener;

public class StepRunGridField extends BaseGridField<List<StepRun>, StepRun> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StepRunGridField(Domains domains, MessageSource messageSource, VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw) {
		super(domains,new TypeToken<List<StepRun>>() {}, StepRun.class, messageSource, vtw, vffw);
		StyleUtil.setBtnLinkStyleContainer(this);
		StyleUtil.setDisableCellFocus(this);
	}

	@SuppressWarnings("serial")
	@Override
	public void setupColumn(Column col, String name) {
		switch (name) {
		case "!edit":
			ColumnUtil.setExternalImageRender(col,  ColumnUtil.EDIT_ICON_URL, new RendererClickListener() {
				@Override
				public void click(RendererClickEvent event) {
					Notification.show(event.getItemId().toString());
				}
			});
			break;
		case "!up":
			ColumnUtil.setExternalImageRender(col,  ColumnUtil.ARROW_UP_URL, new RendererClickListener() {
				@Override
				public void click(RendererClickEvent event) {
					StepRun stepRun = (StepRun) event.getItemId();
					setValue(ColumnUtil.alterHasPositionList((List<StepRun>) getValue(), stepRun));
				}
			});
			break;
		default:
			break;
		}
	}

	@Override
	public void addGeneratedProperty(GeneratedPropertyContainer gpcontainer, String extraName) {
		switch (extraName) {
		case "!edit":
			ColumnUtil.addGeneratedPropertyWithEmptyContent(gpcontainer, extraName);
			break;
		default:
			ColumnUtil.addGeneratedPropertyWithEmptyContent(gpcontainer, extraName);
			break;
		}
	}

	@Override
	public void whenItemClicked(ItemClickEvent event) {
	}

	@Override
	protected void setupGrid(Grid grid) {
		grid.setCellStyleGenerator(cell -> {
			if ("!edit".equals(cell.getPropertyId()) || "!up".equals(cell.getPropertyId())) {
				return "v-align-center";
			} else {
				return null;
			}
		});
	}
}
