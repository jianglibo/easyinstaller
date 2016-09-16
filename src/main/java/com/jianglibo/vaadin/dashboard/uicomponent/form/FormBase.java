package com.jianglibo.vaadin.dashboard.uicomponent.form;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import com.google.common.eventbus.EventBus;
import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.event.view.HistoryBackEvent;
import com.jianglibo.vaadin.dashboard.uicomponent.filecontentfield.FileContentField;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

/**
 * It's better to allow hand make fields to participating.
 * 
 * @author jianglibo@gmail.com
 *
 * @param <T>
 */
@SuppressWarnings("serial")
public abstract class FormBase<T> extends FormLayout {
	
	private static Logger LOGGER = LoggerFactory.getLogger(FormBase.class);

	protected final Domains domains;
	protected final MessageSource messageSource;
	protected final Class<T> clazz;
	protected final FieldFactories fieldFactories;

	protected BeanFieldGroup<T> fieldGroup;

	protected String domainName;

	private List<PropertyIdAndField> fields;

	private HandMakeFieldsListener handMakeFieldsListener;

	public static interface HandMakeFieldsListener {
		Field<?> handMakeFieldCounted(VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw);
	}
	
	public FormBase(Class<T> clazz, MessageSource messageSource, Domains domains, FieldFactories fieldFactories, HandMakeFieldsListener handMakeFieldsListener) {
		this.clazz = clazz;
		this.domainName = clazz.getSimpleName();
		this.domains = domains;
		this.messageSource = messageSource;
		this.fieldFactories = fieldFactories;
		this.handMakeFieldsListener = handMakeFieldsListener;
		fieldGroup = new BeanFieldGroup<T>(clazz);
		addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
		addEnterListener();
//		addEscapeListener();

		VaadinTableWrapper vtw = domains.getTables().get(domainName);
		List<VaadinFormFieldWrapper> ffs = domains.getFormFields(clazz);

		fields = buildFields(vtw, ffs);

		for (PropertyIdAndField paf : fields) {
			fieldGroup.bind(paf.getField(), paf.getPropertyId());
			addComponent(paf.getField());
		}
		StyleUtil.setMarginTopTwenty(this);
	}

	public void notifySuccess() {
		Notification success = new Notification(
				messageSource.getMessage("shared.msg.savesuccess", null, UI.getCurrent().getLocale()));
		success.setDelayMsec(2000);
		success.setStyleName("bar success small");
		success.setPosition(Position.BOTTOM_CENTER);
		success.show(Page.getCurrent());
	}

	public void notifyFailure() {
		Notification.show(messageSource.getMessage("shared.msg.savefailed", null, UI.getCurrent().getLocale()),
				Type.ERROR_MESSAGE);
	}

	protected void addEnterListener() {
		addShortcutListener(new ShortcutListener("submit", null, KeyCode.ENTER) {
			@Override
			public void handleAction(Object sender, Object target) {
				save();
			}
		});
	}

//	protected void addEscapeListener() {
//		addShortcutListener(new ShortcutListener("submit", null, KeyCode.ESCAPE) {
//			@Override
//			public void handleAction(Object sender, Object target) {
//				eventBus.post(new HistoryBackEvent());
//			}
//		});
//	}

	public abstract boolean saveToRepo();

	public void save() {
		try {
			fieldGroup.commit();
			if (saveToRepo()) {
				notifySuccess();
			} else {
				notifyFailure();
			}
		} catch (CommitException e) {
			notifyFailure();
		}
	}

	public List<PropertyIdAndField> buildFields(VaadinTableWrapper vtw, List<VaadinFormFieldWrapper> vffws) {
		List<PropertyIdAndField> fields = Lists.newArrayList();
		for (VaadinFormFieldWrapper vffw : vffws) {
			switch (vffw.getVff().fieldType()) {
			case COMBO_BOX:
				ComboBox cb = fieldFactories.getComboBoxFieldFactory().create(vtw, vffw);
				addStyleName(vffw, cb);
				fields.add(new PropertyIdAndField(vffw, cb));
				break;
			case HAND_MAKER:
				if (handMakeFieldsListener != null) {
					fields.add(new PropertyIdAndField(vffw, handMakeFieldsListener.handMakeFieldCounted(vtw, vffw)));
				} else {
					LOGGER.warn("handMakeFieldsListener not set for FormBase.");
				}
				break;
			case TEXT_AREA:
				TextArea ta = new TextArea(MsgUtil.getFieldMsg(messageSource, vtw.getVt().messagePrefix(), vffw));
				ta.setNullRepresentation("");
				addStyleName(vffw, ta);
				fields.add(new PropertyIdAndField(vffw, ta));
				break;
			case TWIN_COL_SELECT:
				TwinColSelect tcs = fieldFactories.getTwinColSelectFieldFactory().create(vtw, vffw);
				addStyleName(vffw, tcs);
				fields.add(new PropertyIdAndField(vffw, tcs));
				break;
			case FILE_CONTENT_STRING:
				FileContentField fcf = new FileContentField(messageSource);
				fcf.setCaption(MsgUtil.getFieldMsg(messageSource, vtw.getVt().messagePrefix(), vffw));
				addStyleName(vffw, fcf);
				fields.add(new PropertyIdAndField(vffw, fcf));
				break;
			default:
				String caption = MsgUtil.getFieldMsg(messageSource, vtw.getVt().messagePrefix(), vffw);
				TextField tf = new TextField(caption);
				tf.setDescription(MsgUtil.getFieldDescription(messageSource, vtw.getVt().messagePrefix(), vffw));
				tf.setNullRepresentation("");
				addStyleName(vffw, tf);
				fields.add(new PropertyIdAndField(vffw, tf));
				break;
			}
		}
		return fields;
	}

	private void addStyleName(VaadinFormFieldWrapper vfw, Field<?> f) {
		if (vfw.getVff().styleNames().length > 0) {
			for (String sn : vfw.getVff().styleNames()) {
				f.addStyleName(sn);
			}
		}
	}

	public void setItemDataSource(T domain) {
		fieldGroup.setItemDataSource(domain);
	}

	public T getWrappedBean() {
		return fieldGroup.getItemDataSource().getBean();
	}

	public BeanFieldGroup<T> getFieldGroup() {
		return fieldGroup;
	}

	public void setFieldGroup(BeanFieldGroup<T> fieldGroup) {
		this.fieldGroup = fieldGroup;
	}

	public List<PropertyIdAndField> getFields() {
		return fields;
	}

	public void setFields(List<PropertyIdAndField> fields) {
		this.fields = fields;
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
