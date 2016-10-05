package com.jianglibo.vaadin.dashboard;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.jianglibo.vaadin.dashboard.Broadcaster.BroadCasterMessage;
import com.jianglibo.vaadin.dashboard.config.ApplicationConfig;
import com.jianglibo.vaadin.dashboard.domain.BoxHistory;
import com.jianglibo.vaadin.dashboard.domain.Person;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEvent.BrowserResizeEvent;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEvent.CloseOpenWindowsEvent;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEvent.SoftwareNumberChangeEvent;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEvent.UserLoggedOutEvent;
import com.jianglibo.vaadin.dashboard.security.M3958SecurityUtil;
import com.jianglibo.vaadin.dashboard.security.PersonAuthenticationToken;
import com.jianglibo.vaadin.dashboard.service.HttpPageGetter.NewNew;
import com.jianglibo.vaadin.dashboard.service.HttpPageGetter.NewNewsMessage;
import com.jianglibo.vaadin.dashboard.service.HttpPageGetter.NewSoftwareMessage;
import com.jianglibo.vaadin.dashboard.service.SoftwareDownloader.DownloadMessage;
import com.jianglibo.vaadin.dashboard.taskrunner.OneThreadTaskDesc;
import com.jianglibo.vaadin.dashboard.taskrunner.TaskDesc;
import com.jianglibo.vaadin.dashboard.taskrunner.TaskRunner;
import com.jianglibo.vaadin.dashboard.taskrunner.TaskDesc.OneTaskFinishListener;
import com.jianglibo.vaadin.dashboard.uicomponent.tile.TileBase;
import com.jianglibo.vaadin.dashboard.util.NotificationUtil;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEventBus;
import com.jianglibo.vaadin.dashboard.init.AppInitializer;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.view.DashboardMenu;
import com.jianglibo.vaadin.dashboard.view.LoginView;
import com.jianglibo.vaadin.dashboard.view.MainMenuItems;
import com.jianglibo.vaadin.dashboard.view.dashboard.DashboardView;
import com.jianglibo.vaadin.dashboard.view.dashboard.NewNewsTile;
import com.jianglibo.vaadin.dashboard.view.software.SoftwareViewMenuItem;
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
	
	private static Logger LOGGER = LoggerFactory.getLogger(DashboardUI.class);

	private ApplicationContext applicationContext;
	
	private int newSoftwareCountAfterLastStart = 0;
	
	private int fetchNewsCount = 0;
	
	private List<NewNew> news = Lists.newArrayList();

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
	private PersonRepository personRepository;
	
	@Autowired
	private TaskRunner taskRunner;

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
				Person rootp = personRepository.findByEmail(AppInitializer.firstEmail);
				M3958SecurityUtil.doLogin(rootp);
				UI.getCurrent().getPage().reload();
				return;
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
			VaadinSession.getCurrent().setAttribute(PersonAuthenticationToken.class,
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
	}
	
	/**
	 * 
	 * query every backgroup work results, when view changed. 
	 *
	 */
//	private class MyViewChangeListener implements ViewChangeListener {
//
//		@Override
//		public boolean beforeViewChange(ViewChangeEvent event) {
//			return true;
//		}
//
//		@Override
//		public void afterViewChange(ViewChangeEvent event) {
//			LOGGER.info("swithing to view: {}", event.getViewName());
//			if (asyncCaller.getNewSoftwareCountAfterLastStart() > 0) {
//				SoftwareViewMenuItem svmi = (SoftwareViewMenuItem)getDm().getMmis().getMenuMap().get(SoftwareViewMenuItem.class.getName());
//				svmi.updateNotificationsCount(asyncCaller.getNewSoftwareCountAfterLastStart());
//			}
//			
//			if (event.getViewName().startsWith("software")) {
//				if (asyncCaller.getNewSoftwareCountAfterLastStart() > 0) {
//					SoftwareViewMenuItem svmi = (SoftwareViewMenuItem)getDm().getMmis().getMenuMap().get(SoftwareViewMenuItem.class.getName());
//					asyncCaller.resetNewSoftwareCountAfterLastStart();
//					svmi.updateNotificationsCount(0);
//				}
//			}
//		}
//	}
	
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
	
	private DashboardMenu dm;

	public class MainView extends HorizontalLayout {
		public MainView(SpringViewProvider viewProvider) {
			setSizeFull();
			addStyleName("mainview");
			// MainMenuItems must inject this way.
			setDm(new DashboardMenu(messageSource, localeResolver, applicationContext.getBean(MainMenuItems.class)));
			addComponent(getDm());
			ComponentContainer content = new CssLayout();
			content.addStyleName("view-content");
			content.setSizeFull();
			addComponent(content);
			setExpandRatio(content, 1.0f);
			new DashboardNavigator(viewProvider, content);
		}
	}

	@Override
	public void oneTaskFinished(OneThreadTaskDesc ottd, boolean groupFinished) {
		access(new Runnable() {
			@Override
			public void run() {
				if (groupFinished) {
//					BoxHistoryViewMenuItem svmi = (BoxHistoryViewMenuItem)getDm().getMmis().getMenuMap().get(BoxHistoryViewMenuItem.class.getName());
//					if (taskRunner.getBgHistoriesSofar() > clusterhistorymenuitem.getBgHistoriesSofar) {
//						svmi.updateNotificationsCount(nsm.getNewSoftwareCountAfterLastStart() - getNewSoftwareCountAfterLastStart());						
//					}
					push();
				} else {
					NotificationUtil.tray(messageSource, "onetaskfinished", ottd.getBox().getHostname(), ottd.getAction(), ottd.getBoxHistory().isSuccess() ? "success" : "fail");
					push();
				}
			}
		});
	}
	
	
	/**
	 * 
	 */
	@Override
	public void receiveBroadcast(BroadCasterMessage message) {
        // Must lock the session to execute logic safely
        access(new Runnable() {
            @Override
            public void run() {
            	switch (message.getBcmt()) {
				case NEW_SOFTWARE:
					NewSoftwareMessage nsm = (NewSoftwareMessage) message.getBody();
					if (nsm.getNewSoftwareCountAfterLastStart() > getNewSoftwareCountAfterLastStart()) {
						SoftwareViewMenuItem svmi = (SoftwareViewMenuItem)getDm().getMmis().getMenuMap().get(SoftwareViewMenuItem.class.getName());
						svmi.updateNotificationsCount(nsm.getNewSoftwareCountAfterLastStart() - getNewSoftwareCountAfterLastStart());
						setNewSoftwareCountAfterLastStart(nsm.getNewSoftwareCountAfterLastStart());
						push();
					}
					break;
				case NEW_NEWS:
					// Must sure view interested is current view.
//					LOGGER.info("broadcaster received");
					if (getNavigator().getCurrentView() instanceof DashboardView) {
//						LOGGER.info("broadcaster received and in DashboardView.");
						NewNewsMessage nnm = (NewNewsMessage) message.getBody();
						setNews(nnm.getNewNews());
						if (nnm.getFetchCount() > fetchNewsCount) {
							fetchNewsCount = nnm.getFetchCount();
							Optional<TileBase> tbOp = ((DashboardView)getNavigator().getCurrentView()).getTc().findTile(NewNewsTile.class);
							NewNewsTile nnt;
							if (tbOp.isPresent()) {
								((NewNewsTile)tbOp.get()).getNewNewTable().addNews(nnm.getNewNews());
							} else {
								nnt = new NewNewsTile(getNews(), messageSource, "newnews");
								((DashboardView)getNavigator().getCurrentView()).getTc().addTile(nnt);
								nnt.getNewNewTable().addNews(nnm.getNewNews());
							}
							push();
						}
 					}
					break;
				case DOWNLOAD:
					DownloadMessage dm = (DownloadMessage) message.getBody();
					if (dm.getStage() == "start") {
						NotificationUtil.humanized(messageSource, "sd.start", dm.getFileUrl());
					} else {
						if (dm.isSuccess()) {
							NotificationUtil.humanized(messageSource, "sd.success", dm.getFileUrl());
						} else {
							NotificationUtil.humanized(messageSource, "sd.failed", dm.getFileUrl());
						}
					}
					push();
					break;
				default:
					break;
				}
                push();
            }
        });
	}

	public int getNewSoftwareCountAfterLastStart() {
		return newSoftwareCountAfterLastStart;
	}

	public void setNewSoftwareCountAfterLastStart(int newSoftwareCountAfterLastStart) {
		this.newSoftwareCountAfterLastStart = newSoftwareCountAfterLastStart;
	}

	public List<NewNew> getNews() {
		return news;
	}

	public void setNews(List<NewNew> news) {
		this.news = news;
	}

	public void newSoftwareAdded() {
        access(new Runnable() {
            @Override
            public void run() {
            	LOGGER.info("fetch software event posted.");
            	DashboardEventBus.post(new SoftwareNumberChangeEvent());
            	push();
            }
        });
	}

	public DashboardMenu getDm() {
		return dm;
	}

	public void setDm(DashboardMenu dm) {
		this.dm = dm;
	}
}
