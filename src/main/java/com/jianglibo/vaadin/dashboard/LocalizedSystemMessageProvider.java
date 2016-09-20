package com.jianglibo.vaadin.dashboard;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.jianglibo.vaadin.dashboard.window.localeselector.LocaleSelector;
import com.vaadin.server.CustomizedSystemMessages;
import com.vaadin.server.SystemMessages;
import com.vaadin.server.SystemMessagesInfo;
import com.vaadin.server.SystemMessagesProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.server.SpringVaadinServletRequest;

@SpringComponent
@ConfigurationProperties(prefix="vaadin.systemmessages")
public class LocalizedSystemMessageProvider implements SystemMessagesProvider {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Boolean authenticationErrorNotificationEnabled;

	private Boolean communicationErrorNotificationEnabled;

	private Boolean cookiesDisabledNotificationEnabled;
	
	private Boolean internalErrorNotificationEnabled;

	private Boolean sessionExpiredNotificationEnabled;
	
	@Autowired
	private MessageSource messageSource;

	@Override
	public SystemMessages getSystemMessages(SystemMessagesInfo systemMessagesInfo) {
		CustomizedSystemMessages messages = new CustomizedSystemMessages();
//		VaadinRequest vr = VaadinService.getCurrentRequest();
//		Locale locale = LocaleSelector.getLocaleSupported(RequestContextUtils.getLocale((SpringVaadinServletRequest)vr));
//		VaadinSession.getCurrent().getLocale();
		
		Locale locale;
		if (systemMessagesInfo.getRequest() == null) {
			locale = Locale.US;
		} else {
			locale = LocaleSelector.getLocaleSupported(RequestContextUtils.getLocale((SpringVaadinServletRequest)systemMessagesInfo.getRequest()));	
		}
		
		messages.setAuthenticationErrorCaption(messageSource.getMessage("systemmessage.authenticationErrorCaption", null, locale));
		messages.setAuthenticationErrorMessage(messageSource.getMessage("systemmessage.authenticationErrorMessage", null, locale));
		messages.setAuthenticationErrorNotificationEnabled(authenticationErrorNotificationEnabled);
		messages.setAuthenticationErrorURL(null);
		messages.setCommunicationErrorCaption(messageSource.getMessage("systemmessage.communicationErrorCaption", null, locale));
		messages.setCommunicationErrorMessage(messageSource.getMessage("systemmessage.communicationErrorMessage", null, locale));
		messages.setCommunicationErrorNotificationEnabled(communicationErrorNotificationEnabled);
		messages.setCommunicationErrorURL(null);
		messages.setCookiesDisabledCaption(messageSource.getMessage("systemmessage.cookiesDisabledCaption", null, locale));
		messages.setCookiesDisabledMessage(messageSource.getMessage("systemmessage.cookiesDisabledMessage", null, locale));
		messages.setCookiesDisabledNotificationEnabled(cookiesDisabledNotificationEnabled);
		messages.setCookiesDisabledURL(null);
		messages.setInternalErrorCaption(messageSource.getMessage("systemmessage.internalErrorCaption", null, locale));
		messages.setInternalErrorMessage(messageSource.getMessage("systemmessage.internalErrorMessage", null, locale));
		messages.setInternalErrorNotificationEnabled(internalErrorNotificationEnabled);
		messages.setInternalErrorURL(null);
		messages.setSessionExpiredCaption(messageSource.getMessage("systemmessage.sessionExpiredCaption", null, locale));
		messages.setSessionExpiredMessage(messageSource.getMessage("systemmessage.sessionExpiredMessage", null, locale));
		messages.setSessionExpiredNotificationEnabled(sessionExpiredNotificationEnabled);
		messages.setSessionExpiredURL(null);
		return messages;
	}
	
	public Boolean getAuthenticationErrorNotificationEnabled() {
		return authenticationErrorNotificationEnabled;
	}

	public void setAuthenticationErrorNotificationEnabled(Boolean authenticationErrorNotificationEnabled) {
		this.authenticationErrorNotificationEnabled = authenticationErrorNotificationEnabled;
	}

	public Boolean getCommunicationErrorNotificationEnabled() {
		return communicationErrorNotificationEnabled;
	}

	public void setCommunicationErrorNotificationEnabled(Boolean communicationErrorNotificationEnabled) {
		this.communicationErrorNotificationEnabled = communicationErrorNotificationEnabled;
	}

	public Boolean getCookiesDisabledNotificationEnabled() {
		return cookiesDisabledNotificationEnabled;
	}

	public void setCookiesDisabledNotificationEnabled(Boolean cookiesDisabledNotificationEnabled) {
		this.cookiesDisabledNotificationEnabled = cookiesDisabledNotificationEnabled;
	}

	public Boolean getInternalErrorNotificationEnabled() {
		return internalErrorNotificationEnabled;
	}

	public void setInternalErrorNotificationEnabled(Boolean internalErrorNotificationEnabled) {
		this.internalErrorNotificationEnabled = internalErrorNotificationEnabled;
	}

	public Boolean getSessionExpiredNotificationEnabled() {
		return sessionExpiredNotificationEnabled;
	}

	public void setSessionExpiredNotificationEnabled(Boolean sessionExpiredNotificationEnabled) {
		this.sessionExpiredNotificationEnabled = sessionExpiredNotificationEnabled;
	}

}
