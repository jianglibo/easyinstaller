package com.jianglibo.vaadin.dashboard;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.google.common.base.Strings;
import com.google.common.eventbus.Subscribe;
import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.config.ApplicationConfig;
import com.jianglibo.vaadin.dashboard.domain.BoxHistory;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEvent.BrowserResizeEvent;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEvent.CloseOpenWindowsEvent;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEvent.NewSoftwareAddedEvent;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEvent.UserLoggedOutEvent;
import com.jianglibo.vaadin.dashboard.security.M3958SecurityUtil;
import com.jianglibo.vaadin.dashboard.taskrunner.OneThreadTaskDesc;
import com.jianglibo.vaadin.dashboard.taskrunner.TaskDesc;
import com.jianglibo.vaadin.dashboard.taskrunner.TaskDesc.OneTaskFinishListener;
import com.jianglibo.vaadin.dashboard.util.HttpPageGetter;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEventBus;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.view.DashboardMenu;
import com.jianglibo.vaadin.dashboard.view.LoginView;
import com.jianglibo.vaadin.dashboard.view.MainMenuItems;
import com.jianglibo.vaadin.dashboard.view.dashboard.DashboardView;
import com.jianglibo.vaadin.dashboard.window.localeselector.LocaleSelector;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Page;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServletRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@Theme("dashboard")
@Widgetset("com.jianglibo.vaadin.dashboard.DashboardWidgetSet")
@Title("EasyInstaller Dashboard")
@SuppressWarnings("serial")
@SpringUI(path = "/")
@Push(PushMode.MANUAL)
public final class DashboardUI extends UI implements ApplicationContextAware, OneTaskFinishListener, Broadcaster.BroadcastListener {

	private ApplicationContext applicationContext;

	@Autowired
	private SpringViewProvider viewProvider;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private LocaleResolver localeResolver;

	@Autowired
	private AuthenticationManager am;

	@Autowired
	private ApplicationConfig applicationConfig;
	
	@Autowired
	private HttpPageGetter httpPageGetter;

	@Autowired
	private PersonRepository personRepository;

	private final DashboardEventBus dashboardEventbus = new DashboardEventBus();

	private final List<BoxHistory> boxHistories = Lists.newArrayList();

	@Override
	protected void init(final VaadinRequest request) {
		VaadinServletRequest vsr = (VaadinServletRequest) request;
		Locale lo = LocaleSelector.getLocaleSupported(RequestContextUtils.getLocale(vsr.getHttpServletRequest()));

		setLocale(lo);
		DashboardEventBus.register(this);
		Responsive.makeResponsive(this);
		addStyleName(ValoTheme.UI_WITH_MENU);

		// Some views need to be aware of browser resize events so a
		// BrowserResizeEvent gets fired to the event bus on every occasion.
		Page.getCurrent().addBrowserWindowResizeListener(new BrowserWindowResizeListener() {
			@Override
			public void browserWindowResized(final BrowserWindowResizeEvent event) {
				DashboardEventBus.post(new BrowserResizeEvent());
			}
		});

		boolean showMainView = false;

		if (!M3958SecurityUtil.isLogined()) {
			if (applicationConfig.isAutoLogin()) {
				// UI.getCurrent().getPage().setLocation("/autologin");
				// return;
				UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("root",
						"root");
				WebAuthenticationDetails wd = new WebAuthenticationDetailsSource()
						.buildDetails(vsr.getHttpServletRequest());
				authRequest.setDetails(wd);
				Authentication an = am.authenticate(authRequest);
				SecurityContextHolder.getContext().setAuthentication(an);
				// M3958SecurityUtil.doLogin(personRepository.findByEmail(AppInitializer.firstEmail));
				// after login, spring security change the sessionid, so must
				// reload it.
				UI.getCurrent().getPage().reload();
				return;
				// showMainView = true;
			} else {
				LoginView lv = new LoginView(messageSource, localeResolver, (username, password) -> {
					UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username,
							password);
					WebAuthenticationDetails wd = new WebAuthenticationDetailsSource()
							.buildDetails(vsr.getHttpServletRequest());
					authRequest.setDetails(wd);
					Authentication an = am.authenticate(authRequest);
					SecurityContextHolder.getContext().setAuthentication(an);
					String v = Strings.isNullOrEmpty(getNavigator().getState()) ? DashboardView.VIEW_NAME
							: getNavigator().getState();
					getNavigator().navigateTo(v);
				});
				setContent(lv);
				addStyleName("loginview");
			}
		} else {
			VaadinSession.getCurrent().setAttribute(Authentication.class,
					M3958SecurityUtil.getLoginAuthentication());
			showMainView = true;
		}
		if (showMainView) {
			MainView mv = new MainView(viewProvider);
			setContent(mv);
			removeStyleName("loginview");
			String v = Strings.isNullOrEmpty(getNavigator().getState()) ? DashboardView.VIEW_NAME
					: getNavigator().getState();
			getNavigator().navigateTo(v);
		}
		Broadcaster.register(this);
		// every user will trigger this method. But It's a special web application, usually only one user on line.
		httpPageGetter.fetchSoftwareLists(() -> {
			dashboardEventbus.getEventBus().post(new NewSoftwareAddedEvent());
		});
	}
	
    @Override
    public void detach() {
        Broadcaster.unregister(this);
        super.detach();
    }

	public void notifyProgress(TaskDesc taskDesc) {
		access(new Runnable() {
			@Override
			public void run() {

			}
		});
	}

//	class FeederThread extends Thread {
//		int count = 0;
//
//		@Override
//		public void run() {
//			try {
//				// Update the data for a while
//				while (count < 2) {
//					Thread.sleep(1000);
//
//					access(new Runnable() {
//						@Override
//						public void run() {
//							double y = Math.random();
//							Notification.show(count++ + ", item", Type.TRAY_NOTIFICATION);
//							push();
//						}
//					});
//				}
//
//				// Inform that we have stopped running
//				access(new Runnable() {
//					@Override
//					public void run() {
//						// setContent(new Label("Done!"));
//					}
//				});
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//	}

	// /**
	// *
	// * do nothing here.
	// *
	// */
	// private void updateContent(boolean loginFailed, VaadinRequest request) {
	// User user = (User)
	// VaadinSession.getCurrent().setAttribute(User.class.getName(), new
	// User());
	//
	// if (user != null && "admin".equals(user.getRole())) {
	// MainView mv = new MainView(viewProvider);
	// setContent(mv);
	// removeStyleName("loginview");
	// String v = Strings.isNullOrEmpty(getNavigator().getState()) ?
	// DashboardView.VIEW_NAME : getNavigator().getState();
	// getNavigator().navigateTo(v);
	// } else {
	// LoginView lv = new LoginView(messageSource, localeResolver);
	// lv.setup(loginFailed, noticeHasShown);
	// setContent(lv);
	// noticeHasShown++;
	// addStyleName("loginview");
	// }
	// }

	// @Subscribe
	// public void userLoginRequested(final UserLoginRequestedEvent event) {
	// User user = getDataProvider().authenticate(event.getUserName(),
	// event.getPassword());
	// VaadinSession.getCurrent().setAttribute(User.class.getName(), user);
	// updateContent(true);
	// }

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

	public static List<BoxHistory> getBoxHistories() {
		return ((DashboardUI) getCurrent()).boxHistories;
	}

	public static DashboardEventBus getDashboardEventbus() {
		return ((DashboardUI) getCurrent()).dashboardEventbus;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public class MainView extends HorizontalLayout {
		public MainView(SpringViewProvider viewProvider) {
			setSizeFull();
			addStyleName("mainview");
			// MainMenuItems must inject this way.
			addComponent(
					new DashboardMenu(messageSource, localeResolver, applicationContext.getBean(MainMenuItems.class)));
			ComponentContainer content = new CssLayout();
			content.addStyleName("view-content");
			content.setSizeFull();
			addComponent(content);
			setExpandRatio(content, 1.0f);
			new DashboardNavigator(viewProvider, content);
		}
	}

	@Override
	public void OneTaskFinished(OneThreadTaskDesc ottd) {
		access(new Runnable() {
			@Override
			public void run() {
				// setContent(new Label("Done!"));
			}
		});

	}
	
	
	/**
	 * We don't care of current Thread, But track for this UI instance, If instance is right, dashboardEventbus must be right.
	 */
	@Override
	public void receiveBroadcast(String message) {
        // Must lock the session to execute logic safely
        access(new Runnable() {
            @Override
            public void run() {
            	switch (message) {
				case "NewSoftwareAddedEvent":
					dashboardEventbus.getEventBus().post(new NewSoftwareAddedEvent());
					break;
				default:
					break;
				}
                push();
            }
        });
	}
}
