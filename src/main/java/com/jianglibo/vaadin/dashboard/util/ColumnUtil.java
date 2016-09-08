package com.jianglibo.vaadin.dashboard.util;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;

import com.google.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.domain.HasPositionComparator;
import com.jianglibo.vaadin.dashboard.domain.HasPositionField;
import com.vaadin.data.Item;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.ImageRenderer;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickListener;

public class ColumnUtil {

	public static final String EDIT_ICON_URL = "font-awesome_4-6-3_edit_16_0_007dff_none.png";
	public static final String ARROW_DOWN_URL = "font-awesome_4-6-3_arrow-circle-o-down_16_0_007dff_none.png";
	public static final String ARROW_UP_URL = "font-awesome_4-6-3_arrow-circle-o-up_16_0_007dff_none.png";
	public static final String ARROW_LEFT_URL = "font-awesome_4-6-3_arrow-circle-o-left_16_0_007dff_none.png";
	public static final String ARROW_RIGHT_URL = "font-awesome_4-6-3_arrow-circle-o-right_16_0_007dff_none.png";
	public static final String REMOVE_URL = "ionicons_2-0-1_android-remove-circle_16_0_007dff_none.png";

	@SuppressWarnings("serial")
	public static void addGeneratedProperty(MessageSource messageSource, GeneratedPropertyContainer gpcontainer,
			String colname, String content) {
		gpcontainer.addGeneratedProperty(colname, new PropertyValueGenerator<String>() {
			@Override
			public String getValue(Item item, Object itemId, Object propertyId) {
				return MsgUtil.getMsgFallbackToSelf(messageSource, "grid.column.", content);
			}

			@Override
			public Class<String> getType() {
				return String.class;
			}
		});
	}

	@SuppressWarnings("serial")
	public static void addGeneratedPropertyWithEmptyContent(GeneratedPropertyContainer gpcontainer, String colname) {
		gpcontainer.addGeneratedProperty(colname, new PropertyValueGenerator<String>() {
			@Override
			public String getValue(Item item, Object itemId, Object propertyId) {
				return "";
			}

			@Override
			public Class<String> getType() {
				return String.class;
			}
		});
	}

	@SuppressWarnings("serial")
	public static void setExternalImageRender(Grid.Column column, String imgUrl, RendererClickListener listener) {
		ImageRenderer ir;
		if (listener == null) {
			ir = new ImageRenderer();
		} else {
			ir = new ImageRenderer(listener);
		}
		column.setRenderer(ir, new Converter<Resource, String>() {
			@Override
			public String convertToModel(Resource value, Class<? extends String> targetType, Locale l)
					throws Converter.ConversionException {
				return "not needed";
			}

			@Override
			public Resource convertToPresentation(String value, Class<? extends Resource> targetType, Locale l)
					throws Converter.ConversionException {
				return new ExternalResource("/icon-images/" + imgUrl);
			}

			@Override
			public Class<String> getModelType() {
				return String.class;
			}

			@Override
			public Class<Resource> getPresentationType() {
				return Resource.class;
			}
		});
	}
	
	public static <T extends HasPositionField> List<T> alterHasPositionList(List<T> listOrigin, T o) {
		List<T> list = Lists.newArrayList(listOrigin); // copy from origin, let origin untouched.
		int len = list.size();
		if (len < 2) {
			return listOrigin;
		}
		
		for (int i = 0; i < len; i++) {
			if (o == list.get(i)) {// first item clicked. move this item to last.
				if (i == 0) {
					o.setPosition(list.get(len - 1).getPosition() + 1);
				} else {
					int osdPosition = o.getPosition();
					HasPositionField prev = list.get(i - 1); 
					o.setPosition(prev.getPosition());
					prev.setPosition(osdPosition);
				}
				break;
			}
		}
		Collections.sort(list, new HasPositionComparator());
		return list;
	}
}
