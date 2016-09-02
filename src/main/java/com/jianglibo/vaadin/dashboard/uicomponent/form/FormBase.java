package com.jianglibo.vaadin.dashboard.uicomponent.form;

import java.util.List;
import java.util.Map;

import org.springframework.context.MessageSource;

import com.google.common.eventbus.EventBus;
import com.google.gwt.thirdparty.guava.common.collect.Maps;
import com.jianglibo.vaadin.dashboard.annotation.FormFields;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.event.view.HistoryBackEvent;
import com.jianglibo.vaadin.dashboard.uifactory.FormFieldsFactory;
import com.jianglibo.vaadin.dashboard.uifactory.FormFieldsFactory.PropertyIdAndField;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.jianglibo.vaadin.dashboard.vo.HandMakeField;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

/**
 * It's better to allow hand make fields to participating.
 * @author jianglibo@gmail.com
 *
 * @param <T>
 */
@SuppressWarnings("serial")
public abstract class FormBase<T> extends FormLayout {

	protected final Domains domains;
	protected final MessageSource messageSource;
	protected final FormFieldsFactory formFieldsFactory;
	
	protected final Class<T> clazz;
	
	protected BeanFieldGroup<T> fieldGroup;
	
	protected EventBus eventBus;
	
	protected String domainName;
	
	private boolean attachFields;
	
	private List<PropertyIdAndField> fields;
	
	@SuppressWarnings("rawtypes")
	private Map<String, HandMakeField> handMakeFields = Maps.newHashMap();
	
	public FormBase(Class<T> clazz, MessageSource messageSource, Domains domains, FormFieldsFactory formFieldsFactory) {
		this.clazz = clazz;
		this.domainName = clazz.getSimpleName();
		this.domains = domains;
		this.messageSource = messageSource;
		this.formFieldsFactory = formFieldsFactory;
	}
	
	public void defaultAfterInjection(EventBus eventBus, boolean attachFields) {
		this.eventBus = eventBus;
		this.attachFields = attachFields;
		eventBus.register(this);
		fieldGroup = new BeanFieldGroup<T>(clazz);
		addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        addEnterListener();
        addEscapeListener();
	}
	
	@SuppressWarnings("rawtypes")
	public FormBase<T> addHandMakeFields(HandMakeField handMakeField) {
		this.handMakeFields.put(handMakeField.getName(), handMakeField);
		return this;
	}
	
	public abstract FormBase<T> done();
	
	
	public void defaultDone() {
        VaadinTableWrapper vtw = domains.getTables().get(domainName);
        FormFields ffs = domains.getFormFields().get(domainName);
        
        fields = formFieldsFactory.buildFields(vtw, ffs, handMakeFields);
        
        for(PropertyIdAndField paf : fields) {
			fieldGroup.bind(paf.getField(), paf.getPropertyId());
			if (attachFields) {
				addComponent(paf.getField());
			}
        }
        StyleUtil.setMarginTopTwenty(this);
	}
	
	public void notifySuccess() {
        Notification success = new Notification(messageSource.getMessage("shared.msg.savesuccess", null, UI.getCurrent().getLocale()));
        success.setDelayMsec(2000);
        success.setStyleName("bar success small");
        success.setPosition(Position.BOTTOM_CENTER);
        success.show(Page.getCurrent());
	}
	
	public void notifyFailure(){
       Notification.show(messageSource.getMessage("shared.msg.savefailed",null, UI.getCurrent().getLocale()),
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
	
	protected void addEscapeListener() {
	    addShortcutListener(new ShortcutListener("submit", null, KeyCode.ESCAPE) {
			@Override
			public void handleAction(Object sender, Object target) {
				eventBus.post(new HistoryBackEvent());
			}
		});
	}
	
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
	
	public void setItemDataSource(T domain){
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
}
