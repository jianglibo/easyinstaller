package com.jianglibo.vaadin.dashboard.uicomponent.gridfield;

import java.util.Set;

import org.springframework.context.MessageSource;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.util.ColumnUtil;
import com.vaadin.data.Property;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickEvent;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickListener;

@SuppressWarnings("serial")
public class FilesToUploadScalarGridField extends AddableScalarGridField<Set<String>, String>{

	public FilesToUploadScalarGridField(Domains domains, Class<String> clazz, MessageSource messageSource,
			VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw) {
		super(domains,new TypeToken<Set<String>>() {}, clazz, messageSource, vtw, vffw);
	}

	@Override
	public void addGeneratedProperty(GeneratedPropertyContainer gpcontainer, String extraName) {
		switch (extraName) {
		case "!remove":
			ColumnUtil.addGeneratedPropertyWithEmptyContent(gpcontainer, extraName);
			break;
		default:
			ColumnUtil.addGeneratedPropertyWithEmptyContent(gpcontainer, extraName);
			break;
		}
		
	}

	@Override
	protected void setupGrid(Grid grid) {
		grid.setCellStyleGenerator(cell -> {
			if ("!remove".equals(cell.getPropertyId())) {
				return "v-align-center";
			} else {
				return null;
			}
		});
		
	}

	@Override
	public void setupColumn(Column col, String name) {
		switch (name) {
		case "!remove":
			ColumnUtil.setExternalImageRender(col,  ColumnUtil.REMOVE_URL, new RendererClickListener() {
				@Override
				public void click(RendererClickEvent event) {
					String f = (String) event.getItemId();
					Set<String> newValue = Sets.newHashSet(getValue());
					newValue.remove(f);
					setValue(newValue);
				}
			});
			break;
		default:
			break;
		}
	}

	@Override
	protected void aboutToAddValue(Property<String> property) {
		String v = property.getValue();
		if (Strings.isNullOrEmpty(v)) {
			return;
		}
		
		Set<String> newValue = Sets.newHashSet(getValue());
		newValue.add(property.getValue());
		setValue(newValue);
		property.setValue("");
	}

}
