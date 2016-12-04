package com.jianglibo.vaadin.dashboard.uicomponent.form;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Person;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.security.M3958SecurityUtil;
import com.jianglibo.vaadin.dashboard.security.PersonVo;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase.PropertyIdAndField;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 * 
 * @author jianglibo@gmail.com
 *
 * @param <T>
 */
@SuppressWarnings("serial")
public abstract class FormBaseFree<T> extends FormLayout {
	
	private static Logger LOGGER = LoggerFactory.getLogger(FormBaseFree.class);

	protected final Domains domains;
	protected final MessageSource messageSource;
	protected final Class<T> clazz;
	protected final FieldFactories fieldFactories;

	protected BeanFieldGroup<T> fieldGroup;
	
	private List<PropertyIdAndField> fields;

	private final PersonRepository personRepository;

	
	public FormBaseFree(Class<T> clazz, PersonRepository personRepository, MessageSource messageSource, Domains domains, FieldFactories fieldFactories) {
		this.clazz = clazz;
		this.domains = domains;
		this.messageSource = messageSource;
		this.fieldFactories = fieldFactories;
		this.personRepository = personRepository;
	}
	
	protected void delayCreateContent() {
		fieldGroup = new BeanFieldGroup<T>(clazz);
		addStyleName(ValoTheme.FORMLAYOUT_LIGHT);

		try {
			fields = buildFields();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (PropertyIdAndField paf : fields) {
			fieldGroup.bind(paf.getField(), paf.getPropertyId());
			addComponent(paf.getField());
		}
		StyleUtil.setMarginTopTwenty(this);
	}
	
	protected abstract List<PropertyIdAndField> buildFields() throws JsonParseException, JsonMappingException, IOException;

	protected Person getCurrentUser() {
		if (M3958SecurityUtil.isLogined()) {
			return getPersonRepository().findOne(M3958SecurityUtil.getLoginPersonId());
		} else {
			return getPersonRepository().findOne(((PersonVo) VaadinSession.getCurrent().getAttribute(Authentication.class).getPrincipal()).getId());
		}
		
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

	public PersonRepository getPersonRepository() {
		return personRepository;
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
}
