package com.jianglibo.vaadin.dashboard.uifactory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.annotation.FormFields;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.uicomponent.filecontentfield.FileContentField;
import com.jianglibo.vaadin.dashboard.uicomponent.gridfield.GridField;
import com.jianglibo.vaadin.dashboard.uicomponent.twingrid.TwinGridField;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;

@Component
public class FormFieldsFactory {
	
	@Autowired
	private ComboBoxFieldFactory comboBoxFieldFactory;
	
	@Autowired
	private GridFieldFactory gridFieldFactory;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private TwinColSelectFieldFactory twinColSelectFieldFactory;
	
	@Autowired
	private TwinGridFieldFactory twinGridFieldFactory;
	
	public List<PropertyIdAndField> buildFields(VaadinTableWrapper vtw, FormFields ffs) {
		List<PropertyIdAndField> fields = Lists.newArrayList();
        for(VaadinFormFieldWrapper vfw : ffs.getFields()) {
        	switch (vfw.getVff().fieldType()) {
			case COMBO_BOX:
				ComboBox cb = comboBoxFieldFactory.create(vtw, vfw);
				addStyleName(vfw, cb);
				fields.add(new PropertyIdAndField(vfw, cb));
				break;
			case TEXT_AREA:
				TextArea ta = new TextArea(MsgUtil.getFieldMsg(messageSource, vtw.getVt().messagePrefix(), vfw));
				ta.setNullRepresentation("");
				addStyleName(vfw, ta);
				fields.add(new PropertyIdAndField(vfw, ta));
				break;
			case TWIN_COL_SELECT:
				TwinColSelect tcs = twinColSelectFieldFactory.create(vtw, vfw);
				addStyleName(vfw, tcs);
				fields.add(new PropertyIdAndField(vfw, tcs));
				break;
			case FILE_CONTENT_STRING:
				FileContentField fcf = applicationContext.getBean(FileContentField.class);
				fcf.setCaption(MsgUtil.getFieldMsg(messageSource, vtw.getVt().messagePrefix(), vfw));
				addStyleName(vfw, fcf);
				fields.add(new PropertyIdAndField(vfw, fcf));
				break;
			case GRID:
				GridField<?> gf = gridFieldFactory.create(vtw, vfw);
				gf.setCaption(MsgUtil.getFieldMsg(messageSource, vtw.getVt().messagePrefix(), vfw));
				addStyleName(vfw, gf);
				fields.add(new PropertyIdAndField(vfw, gf));
				break;
			case TWIN_GRID:
				TwinGridField<?> tgf = twinGridFieldFactory.create(vtw, vfw);
				tgf.setCaption(MsgUtil.getFieldMsg(messageSource, vtw.getVt().messagePrefix(), vfw));
				addStyleName(vfw, tgf);
				fields.add(new PropertyIdAndField(vfw, tgf));
				break;
			default:
				String caption = MsgUtil.getFieldMsg(messageSource, vtw.getVt().messagePrefix(), vfw);
				TextField tf = new TextField(caption);
				tf.setNullRepresentation("");
				addStyleName(vfw, tf);
				fields.add(new PropertyIdAndField(vfw, tf));
				break;
			}
        }
        return fields;
	}
	
	private void addStyleName(VaadinFormFieldWrapper vfw, Field<?> f) {
		if (vfw.getVff().styleNames().length > 0) {
			for(String sn: vfw.getVff().styleNames()) {
				f.addStyleName(sn);
			}
		}
	}
	
	public static class PropertyIdAndField {
		private String propertyId;
		private Field<?> field;
		
		public PropertyIdAndField(VaadinFormFieldWrapper vfw, Field<?> field) {
			super();
			field.setEnabled(vfw.getVff().enabled());
			field.setReadOnly(vfw.getVff().readOnly());
			this.propertyId = vfw.getName();
			this.field = field;
		}

		public String getPropertyId() {
			return propertyId;
		}

		public void setPropertyId(String propertyId) {
			this.propertyId = propertyId;
		}

		public Field<?> getField() {
			return field;
		}

		public void setField(Field<?> field) {
			this.field = field;
		}
	}
}
