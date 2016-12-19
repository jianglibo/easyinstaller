package com.jianglibo.vaadin.dashboard.util;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

import org.springframework.context.MessageSource;

import com.google.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.domain.HasPositionComparator;
import com.jianglibo.vaadin.dashboard.domain.HasPositionField;
import com.vaadin.data.Item;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.ImageRenderer;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickListener;
import com.vaadin.ui.renderers.HtmlRenderer;

public class ColumnUtil {

	public static final String EDIT_ICON_URL = "font-awesome_4-6-3_edit_16_0_007dff_none.png";
	public static final String ARROW_DOWN_URL = "font-awesome_4-6-3_arrow-circle-o-down_16_0_007dff_none.png";
	public static final String ARROW_UP_URL = "font-awesome_4-6-3_arrow-circle-o-up_16_0_007dff_none.png";
	public static final String ARROW_LEFT_URL = "font-awesome_4-6-3_arrow-circle-o-left_16_0_007dff_none.png";
	public static final String ARROW_RIGHT_URL = "font-awesome_4-6-3_arrow-circle-o-right_16_0_007dff_none.png";
	public static final String REMOVE_URL = "ionicons_2-0-1_android-remove-circle_16_0_007dff_none.png";
	
	
	public static final String getFullIconPath(String iconFn) {
		return "/icon-images/" + iconFn;
	}
	
	public static Object[] toObjectArray(String[] stringArray) {
		return IntStream.range(0, stringArray.length).mapToObj(i -> stringArray[i]).toArray();
	}
	
	public static Object[] toObjectArray(List<String> stringList) {
		return IntStream.range(0, stringList.size()).mapToObj(i -> stringList.get(i)).toArray();
	}

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
	
	public static String format(long l) {

	    String s = String.valueOf(l);
	    int digits = 0;
	    while (s.length() > 3) {
	        s = s.substring(0, s.length() - 3);
	        digits++;
	    }
	    StringBuffer buffer = new StringBuffer();
	    buffer.append(s);
	    if ((s.length() == 1) && (String.valueOf(l).length() >= 3)) {
	        buffer.append(".");
	        buffer.append(String.valueOf(l).substring(1, 3));
	    } else if ((s.length() == 2) && (String.valueOf(l).length() >= 3)) {
	        buffer.append(".");
	        buffer.append(String.valueOf(l).substring(2, 3));
	    }
	    if (digits == 0) {
	        buffer.append(" B");
	    } else if (digits == 1) {
	        buffer.append(" KB");
	    } else if (digits == 2) {
	        buffer.append(" MB");
	    } else if (digits == 3) {
	        buffer.append(" GB");
	    } else if (digits == 4) {
	        buffer.append(" TB");
	    }
	    return buffer.toString();
	}
	
	@SuppressWarnings("serial")
	public static void setFileLengthRender(Grid.Column column) {
		column.setRenderer(new HtmlRenderer(), new Converter<String, Long>(){

			@Override
			public Long convertToModel(String value, Class<? extends Long> targetType, Locale locale)
					throws com.vaadin.data.util.converter.Converter.ConversionException {
				return 0L;
			}

			@Override
			public String convertToPresentation(Long value, Class<? extends String> targetType, Locale locale)
					throws com.vaadin.data.util.converter.Converter.ConversionException {
				return format(value);
			}

			@Override
			public Class<Long> getModelType() {
				return Long.class;
			}

			@Override
			public Class<String> getPresentationType() {
				return String.class;
			}});
	}
	
	
	@SuppressWarnings("serial")
	public static void setTrueFalseRender(Grid.Column column, boolean displayFalse) {
		column.setRenderer(new HtmlRenderer(), new Converter<String, Boolean>() {

			@Override
			public Boolean convertToModel(String value, Class<? extends Boolean> targetType, Locale locale)
					throws com.vaadin.data.util.converter.Converter.ConversionException {
				return false;
			}

			@Override
			public String convertToPresentation(Boolean value, Class<? extends String> targetType, Locale locale)
					throws com.vaadin.data.util.converter.Converter.ConversionException {
				if (value) {
					return FontAwesome.CHECK_SQUARE_O.getHtml();
				} else {
					if (displayFalse) {
						return FontAwesome.CLOSE.getHtml();
					} else {
						return "";
					}
					
				}
			}

			@Override
			public Class<Boolean> getModelType() {
				return Boolean.class;
			}

			@Override
			public Class<String> getPresentationType() {
				return String.class;
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
