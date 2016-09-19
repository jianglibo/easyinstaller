package com.jianglibo.vaadin.dashboard.view;

import org.springframework.context.MessageSource;
import org.springframework.web.servlet.LocaleResolver;

import com.google.common.eventbus.Subscribe;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEvent.PostViewChangeEvent;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEvent.ProfileUpdatedEvent;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEvent.UserLoggedOutEvent;
import com.jianglibo.vaadin.dashboard.security.M3958SecurityUtil;
import com.jianglibo.vaadin.dashboard.security.PersonVo;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEventBus;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.jianglibo.vaadin.dashboard.window.localeselector.LocaleSelectorWindow;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

/**
 * A responsive menu component providing user information and the controls for
 * primary navigation between the views.
 */
@SuppressWarnings({ "serial" })
public final class DashboardMenu extends CustomComponent {


	public static final String ID = "dashboard-menu";
	public static final String REPORTS_BADGE_ID = "dashboard-menu-reports-badge";
	public static final String NOTIFICATIONS_BADGE_ID = "dashboard-menu-notifications-badge";
	private static final String STYLE_VISIBLE = "valo-menu-visible";
	
	private final MainMenuItems mmis;
	
	private final LocaleResolver localeResolver;
	
	private final MessageSource messageSource;

	private MenuItem settingsItem;
	
	public DashboardMenu(MessageSource messageSource, LocaleResolver localeResolver, MainMenuItems mmis) {
		this.messageSource = messageSource;
		this.localeResolver = localeResolver;
		this.mmis = mmis;
		setPrimaryStyleName("valo-menu");
		setId(ID);
		setSizeUndefined();

		// There's only one DashboardMenu per UI so this doesn't need to be
		// unregistered from the UI-scoped DashboardEventBus.
		DashboardEventBus.register(this);
		setCompositionRoot(buildContent());
	}

	private Component buildContent() {
		final CssLayout menuContent = new CssLayout();
		menuContent.addStyleName("sidebar");
		menuContent.addStyleName(ValoTheme.MENU_PART);
		menuContent.addStyleName("no-vertical-drag-hints");
		menuContent.addStyleName("no-horizontal-drag-hints");
		menuContent.setWidth(null);
		menuContent.setHeight("100%");

		menuContent.addComponent(buildTitle());
		menuContent.addComponent(buildUserMenu());
		menuContent.addComponent(buildToggleButton());
		menuContent.addComponent(buildMenuItems());

		return menuContent;
	}

	private Component buildTitle() {
		Label logo = new Label(messageSource.getMessage("dmenu.title", null, UI.getCurrent().getLocale()), ContentMode.HTML);
		logo.setSizeUndefined();
		HorizontalLayout logoWrapper = new HorizontalLayout(logo);
		logoWrapper.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
		logoWrapper.addStyleName("valo-menu-title");
		return logoWrapper;
	}

//	private User getCurrentUser() {
//		return (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
//	}
			
	private Component buildUserMenu() {
		final MenuBar settings = new MenuBar();
		settings.addStyleName("user-menu");
		PersonVo pvo = M3958SecurityUtil.getLoginPersonVo().get();
//		settingsItem = settings.addItem("", new ThemeResource("img/profile-pic-300px.jpg"), null);
		settingsItem = settings.addItem("", new ExternalResource(pvo.getAvatar()), null);
		updateUserName(null);
		settingsItem.addItem(MsgUtil.getDropDownMenuMsg(messageSource, "swithLang"),
				new Command() {
					@Override
					public void menuSelected(final MenuItem selectedItem) {
						UI.getCurrent().addWindow(new LocaleSelectorWindow(messageSource, localeResolver));
					}
				});
		settingsItem.addSeparator();
		settingsItem.addItem(MsgUtil.getDropDownMenuMsg(messageSource, "signout"), new Command() {
			@Override
			public void menuSelected(final MenuItem selectedItem) {
				DashboardEventBus.post(new UserLoggedOutEvent());
			}
		});
		return settings;
	}

	private Component buildToggleButton() {
		Button valoMenuToggleButton = new Button("Menu", new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				if (getCompositionRoot().getStyleName().contains(STYLE_VISIBLE)) {
					getCompositionRoot().removeStyleName(STYLE_VISIBLE);
				} else {
					getCompositionRoot().addStyleName(STYLE_VISIBLE);
				}
			}
		});
		valoMenuToggleButton.setIcon(FontAwesome.LIST);
		valoMenuToggleButton.addStyleName("valo-menu-toggle");
		valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_SMALL);
		return valoMenuToggleButton;
	}

	private Component buildMenuItems() {
		CssLayout menuItemsLayout = new CssLayout();
		menuItemsLayout.addStyleName("valo-menuitems");
		
		mmis.getItems().values().forEach(mw -> {
			menuItemsLayout.addComponent(mw.getMenuItem());
		});
		return menuItemsLayout;
	}

	@Override
	public void attach() {
		super.attach();
		mmis.getItems().values().forEach(mw -> {
			mw.onAttach();
		});

	}

	@Subscribe
	public void postViewChange(final PostViewChangeEvent event) {
		// After a successful view change the menu can be hidden in mobile view.
		getCompositionRoot().removeStyleName(STYLE_VISIBLE);
	}


	@Subscribe
	public void updateUserName(final ProfileUpdatedEvent event) {
		PersonVo pvo = M3958SecurityUtil.getLoginPersonVo().get();
		settingsItem.setText(pvo.getName());
	}
}
