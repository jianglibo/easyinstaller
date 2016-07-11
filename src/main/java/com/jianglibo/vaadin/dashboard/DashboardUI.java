package com.jianglibo.vaadin.dashboard;

import java.util.Locale;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.common.eventbus.Subscribe;
import com.jianglibo.vaadin.dashboard.data.DataProvider;
import com.jianglibo.vaadin.dashboard.data.dummy.DummyDataProvider;
import com.jianglibo.vaadin.dashboard.domain.User;
import com.jianglibo.vaadin.dashboard.event.DashboardEventBus;
import com.jianglibo.vaadin.dashboard.event.DashboardEvent.BrowserResizeEvent;
import com.jianglibo.vaadin.dashboard.event.DashboardEvent.CloseOpenWindowsEvent;
import com.jianglibo.vaadin.dashboard.event.DashboardEvent.UserLoggedOutEvent;
import com.jianglibo.vaadin.dashboard.event.DashboardEvent.UserLoginRequestedEvent;
import com.jianglibo.vaadin.dashboard.view.LoginView;
import com.jianglibo.vaadin.dashboard.view.MainView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Page;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@Theme("dashboard")
@Widgetset("com.jianglibo.vaadin.dashboard.DashboardWidgetSet")
@Title("EasyInstaller Dashboard")
@SuppressWarnings("serial")
@SpringUI(path = "/")
public final class DashboardUI extends UI implements ApplicationContextAware {
	
	private int noticeHasShown = 0;
	
	private ApplicationContext applicationContext;

	/*
	 * This field stores an access to the dummy backend layer. In real
	 * applications you most likely gain access to your beans trough lookup or
	 * injection; and not in the UI but somewhere closer to where they're
	 * actually accessed.
	 */
	private final DataProvider dataProvider = new DummyDataProvider();
	private final DashboardEventBus dashboardEventbus = new DashboardEventBus();

	@Override
	protected void init(final VaadinRequest request) {
		setLocale(Locale.US);

		DashboardEventBus.register(this);
		Responsive.makeResponsive(this);
		addStyleName(ValoTheme.UI_WITH_MENU);

		updateContent(false);

		// Some views need to be aware of browser resize events so a
		// BrowserResizeEvent gets fired to the event bus on every occasion.
		Page.getCurrent().addBrowserWindowResizeListener(new BrowserWindowResizeListener() {
			@Override
			public void browserWindowResized(final BrowserWindowResizeEvent event) {
				DashboardEventBus.post(new BrowserResizeEvent());
			}
		});
	}

	/**
	 * Updates the correct content for this UI based on the current user status.
	 * If the user is logged in with appropriate privileges, main view is shown.
	 * Otherwise login view is shown.
	 */
	private void updateContent(boolean loginFailed) {
		User user = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		if (user != null && "admin".equals(user.getRole())) {
			// Authenticated user
//			setContent(new MainView());
			MainView mv = applicationContext.getBean(MainView.class);
			setContent(mv);
			removeStyleName("loginview");
			getNavigator().navigateTo(getNavigator().getState());
		} else {
			LoginView lv = applicationContext.getBean(LoginView.class);
			lv.setup(loginFailed, noticeHasShown);
			setContent(lv);
			noticeHasShown++;
			addStyleName("loginview");
		}
	}

	@Subscribe
	public void userLoginRequested(final UserLoginRequestedEvent event) {
		User user = getDataProvider().authenticate(event.getUserName(), event.getPassword());
		VaadinSession.getCurrent().setAttribute(User.class.getName(), user);
		updateContent(true);
	}

	@Subscribe
	public void userLoggedOut(final UserLoggedOutEvent event) {
		// When the user logs out, current VaadinSession gets closed and the
		// page gets reloaded on the login screen. Do notice the this doesn't
		// invalidate the current HttpSession.
		VaadinSession.getCurrent().close();
		Page.getCurrent().reload();
	}

	@Subscribe
	public void closeOpenWindows(final CloseOpenWindowsEvent event) {
		for (Window window : getWindows()) {
			window.close();
		}
	}

	/**
	 * @return An instance for accessing the (dummy) services layer.
	 */
	public static DataProvider getDataProvider() {
		return ((DashboardUI) getCurrent()).dataProvider;
	}

	public static DashboardEventBus getDashboardEventbus() {
		return ((DashboardUI) getCurrent()).dashboardEventbus;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
