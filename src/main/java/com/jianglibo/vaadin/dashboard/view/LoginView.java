package com.jianglibo.vaadin.dashboard.view;

import com.jianglibo.vaadin.dashboard.event.DashboardEventBus;
import com.jianglibo.vaadin.dashboard.vaadinerrors.LoginError;
import com.jianglibo.vaadin.dashboard.window.localeselector.LocaleSelector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.jianglibo.vaadin.dashboard.event.DashboardEvent.UserLoginRequestedEvent;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class LoginView extends VerticalLayout {
	
	private boolean loginFailed = false;

	private int noticeHasShown = 0;
	
	@Autowired
	private MessageSource messageSource;

//    public LoginView() {
//    	this.setup();
//    }

//    public LoginView(boolean loginFailed, int noticeHasShown) {
//    	this.loginFailed = loginFailed;
//    	this.noticeHasShown = noticeHasShown;
//    	this.setup();
//	}
    
    public void setup(boolean loginFailed, int noticeHasShown) {
    	setLoginFailed(loginFailed);
    	setNoticeHasShown(noticeHasShown);
        setSizeFull();

        Component loginForm = buildLoginForm(loginFailed);
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
        
        if (noticeHasShown == 0) {
            Notification notification = new Notification(
                    "Welcome to Dashboard Demo");
            notification
                    .setDescription("<span>This application is not real, it only demonstrates an application built with the <a href=\"https://vaadin.com\">Vaadin framework</a>.</span> <span>No username or password is required, just click the <b>Sign In</b> button to continue.</span>");
            notification.setHtmlContentAllowed(true);
            notification.setStyleName("tray dark small closable login-help");
            notification.setPosition(Position.BOTTOM_CENTER);
            notification.setDelayMsec(20000);
            notification.show(Page.getCurrent());
        }

    }

	private Component buildLoginForm(boolean loginFailed) {
        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setSpacing(true);
        Responsive.makeResponsive(loginPanel);
        loginPanel.addStyleName("login-panel");

        loginPanel.addComponent(buildLabels());
        loginPanel.addComponent(buildFields());
        loginPanel.addComponent(new CheckBox(messageSource.getMessage("login.rememberme", null, getLocale()), true));
        	
        return loginPanel;
    }

    private Component buildFields() {
        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.addStyleName("fields");

        final TextField username = new TextField(messageSource.getMessage("login.username", null, getLocale()));
        if (loginFailed) {
        	username.setComponentError(new LoginError());
        }

        username.setIcon(FontAwesome.USER);
        username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final PasswordField password = new PasswordField(messageSource.getMessage("login.password", null, getLocale()));
        if (loginFailed) {
        	password.setComponentError(new LoginError());
        }
        password.setIcon(FontAwesome.LOCK);
        password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final Button signin = new Button(messageSource.getMessage("login.signin", null, getLocale()));
        signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signin.setClickShortcut(KeyCode.ENTER);
        signin.focus();

        fields.addComponents(username, password, signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

        signin.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                DashboardEventBus.post(new UserLoginRequestedEvent(username
                        .getValue(), password.getValue()));
            }
        });
        return fields;
    }

    private Component buildLabels() {
        CssLayout labels = new CssLayout();
        labels.addStyleName("labels");

        LocaleSelector ls = new LocaleSelector();

        Label welcome = new Label(messageSource.getMessage("login.welcome", null, getLocale()));
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);
        
        labels.addComponent(ls.unwrap());

        Label title = new Label( messageSource.getMessage("login.dashboard", new String[]{"EasyInstaller"}, getLocale()));
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_LIGHT);
        labels.addComponent(title);
        return labels;
    }
    
	public boolean isLoginFailed() {
		return loginFailed;
	}

	public void setLoginFailed(boolean loginFailed) {
		this.loginFailed = loginFailed;
	}

	public int getNoticeHasShown() {
		return noticeHasShown;
	}

	public void setNoticeHasShown(int noticeHasShown) {
		this.noticeHasShown = noticeHasShown;
	}

}
