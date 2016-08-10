package com.jianglibo.vaadin.dashboard.uicomponent.form;

import java.util.List;

import org.springframework.context.MessageSource;

import com.google.common.eventbus.EventBus;
import com.jianglibo.vaadin.dashboard.annotation.FormFields;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.event.view.HistoryBackEvent;
import com.jianglibo.vaadin.dashboard.util.FormFieldsFactory;
import com.jianglibo.vaadin.dashboard.util.ReflectUtil;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.jianglibo.vaadin.dashboard.util.FormFieldsFactory.PropertyIdAndField;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public abstract class FormBase<T> extends FormLayout {

	protected final Domains domains;
	protected final MessageSource messageSource;
	protected final FormFieldsFactory formFieldsFactory;
	
	protected final Class<T> clazz;
	
	protected BeanFieldGroup<T> fieldGroup;
	
	protected EventBus eventBus;
	
	protected String domainName;
	
	public FormBase(Class<T> clazz, MessageSource messageSource, Domains domains, FormFieldsFactory formFieldsFactory) {
		this.clazz = clazz;
		this.domainName = ReflectUtil.getDomainName(clazz);
		this.domains = domains;
		this.messageSource = messageSource;
		this.formFieldsFactory = formFieldsFactory;
	}
	
	protected void defaultAfterInjection(EventBus eventBus) {
		this.eventBus = eventBus;
		eventBus.register(this);
		fieldGroup = new BeanFieldGroup<T>(clazz);
		addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        addEnterListener();
        addEscapeListener();
        createTable();
	}
	
	public void createTable() {
        VaadinTable vt = domains.getTables().get(domainName);
        FormFields ffs = domains.getFormFields().get(domainName);
        
        List<PropertyIdAndField> fields = formFieldsFactory.buildFields(vt, ffs);
        
        for(PropertyIdAndField paf : fields) {
			fieldGroup.bind(paf.getField(), paf.getPropertyId());
			addComponent(paf.getField());
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
}
