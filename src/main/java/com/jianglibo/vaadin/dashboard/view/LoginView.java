package com.jianglibo.vaadin.dashboard.view;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.LocaleResolver;

import com.jianglibo.vaadin.dashboard.vaadinerrors.LoginError;
import com.jianglibo.vaadin.dashboard.window.localeselector.LocaleSelectorWindow;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class LoginView extends VerticalLayout {
	
	private static Logger LOGGER = LoggerFactory.getLogger(LoginView.class);
	
	private boolean loginFailed = false;

	private int noticeHasShown = 0;
	
	private final MessageSource messageSource;
	
	private final LocaleResolver localeResolver;
    
    private final LoginAttemptListener loginAttemptListener;
	
	
    @Autowired
	public LoginView(MessageSource messageResource, LocaleResolver localeResolver, LoginAttemptListener loginAttemptListener) {
		this.messageSource = messageResource;
		this.localeResolver = localeResolver;
		this.loginAttemptListener = loginAttemptListener;
        setSizeFull();
        Component loginForm = buildLoginForm(loginFailed);
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
	}
	
	@Override
	public Locale getLocale() {
		return UI.getCurrent().getLocale();
	}

	private Component buildLoginForm(boolean loginFailed) {
        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setSpacing(true);
        Responsive.makeResponsive(loginPanel);
        loginPanel.addStyleName("login-panel");

        loginPanel.addComponent(buildLabels());
        loginPanel.addComponent(buildFields());
//        getLocal() is null, because form has not attach to parent.
        LOGGER.info("current locale is: {}", getLocale().getLanguage());
        String msg = messageSource.getMessage("login.rememberme", null, getLocale());
        LOGGER.info("current msg is: {}", msg);
        loginPanel.addComponent(new CheckBox(msg, true));
//        llabel.addStyleName(ValoTheme.LABEL_H4);
        
        Button btn = new Button(messageSource.getMessage("lanselector.btn", null, UI.getCurrent().getLocale()));
		btn.setStyleName(Reindeer.BUTTON_LINK);
		btn.setIcon(FontAwesome.LANGUAGE);
		
		btn.addClickListener(ce -> {
			UI.getCurrent().addWindow(new LocaleSelectorWindow(messageSource, localeResolver));
		});
		
        loginPanel.addComponent(btn);
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
            	loginAttemptListener.tryLogin(username.getValue(), password.getValue());
            }
        });
        return fields;
    }

    private Component buildLabels() {
        CssLayout labels = new CssLayout(){
        	@Override
        	protected String getCss(Component c) {
        		String css =  super.getCss(c);
//        		if (c.getIcon() != null) {
//        			return "display: inline-block; margin-left: 8px";
//        		}
//        		LOGGER.info("css: {}", css);
        		return css;
        	}
        };
        labels.addStyleName("labels");

        

        Label welcome = new Label(messageSource.getMessage("login.welcome", null, getLocale()));
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);

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
	
	public static interface LoginAttemptListener {
		public void tryLogin(String username, String password);
	}

}
