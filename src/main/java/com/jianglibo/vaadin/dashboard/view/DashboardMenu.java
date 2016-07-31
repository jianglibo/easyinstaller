package com.jianglibo.vaadin.dashboard.view;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;

import com.google.common.eventbus.Subscribe;
import com.jianglibo.vaadin.dashboard.DashboardNavigator;
import com.jianglibo.vaadin.dashboard.component.ProfilePreferencesWindow;
import com.jianglibo.vaadin.dashboard.domain.User;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEventBus;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEvent.PostViewChangeEvent;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEvent.ProfileUpdatedEvent;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEvent.UserLoggedOutEvent;
import com.jianglibo.vaadin.dashboard.view.dashboard.DashboardViewMenuItem;
import com.jianglibo.vaadin.dashboard.view.reports.ReportsViewMenuItem;
import com.jianglibo.vaadin.dashboard.view.schedule.ScheduleViewMenuItem;
import com.jianglibo.vaadin.dashboard.view.transactions.TransactionsViewMenuItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
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
@SpringComponent
@Scope("prototype")
public final class DashboardMenu extends CustomComponent {


	public static final String ID = "dashboard-menu";
	public static final String REPORTS_BADGE_ID = "dashboard-menu-reports-badge";
	public static final String NOTIFICATIONS_BADGE_ID = "dashboard-menu-notifications-badge";
	private static final String STYLE_VISIBLE = "valo-menu-visible";
	
	@Autowired
	private MainMenuItems mmis;
	
	@Autowired
	private MessageSource messageSource;

	private MenuItem settingsItem;
	
//	private DashboardViewMenuItem dashboardViewMenuItem;
//	private ReportsViewMenuItem reportsViewMenuItem;
//	private ScheduleViewMenuItem scheduleViewMenuItem;
//	private TransactionsViewMenuItem transactionsViewMenuItem;
	
	public void setup(){
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

	private User getCurrentUser() {
		return (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
	}
			
	private Component buildUserMenu() {
		final MenuBar settings = new MenuBar();
		settings.addStyleName("user-menu");
		final User user = getCurrentUser();
		settingsItem = settings.addItem("", new ThemeResource("img/profile-pic-300px.jpg"), null);
		updateUserName(null);
		settingsItem.addItem(messageSource.getMessage("dmenu.dropdown.editprofile", null, UI.getCurrent().getLocale()), new Command() {
			@Override
			public void menuSelected(final MenuItem selectedItem) {
				ProfilePreferencesWindow.open(user, false);
			}
		});
		settingsItem.addItem(messageSource.getMessage("dmenu.dropdown.preferences", null, UI.getCurrent().getLocale()), new Command() {
			@Override
			public void menuSelected(final MenuItem selectedItem) {
				ProfilePreferencesWindow.open(user, true);
			}
		});
		settingsItem.addSeparator();
		settingsItem.addItem(messageSource.getMessage("dmenu.dropdown.signout", null, UI.getCurrent().getLocale()), new Command() {
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
//		dashboardViewMenuItem = new DashboardViewMenuItem();
//		menuItemsLayout.addComponent(dashboardViewMenuItem.getMenuItem());
//		
//		reportsViewMenuItem = new ReportsViewMenuItem();
//		menuItemsLayout.addComponent(reportsViewMenuItem.getMenuItem());
//		
//		scheduleViewMenuItem = new ScheduleViewMenuItem();
//		menuItemsLayout.addComponent(scheduleViewMenuItem.getMenuItem());
//		
//		transactionsViewMenuItem = new TransactionsViewMenuItem(); 
//		menuItemsLayout.addComponent(transactionsViewMenuItem.getMenuItem());

		// for (final DashboardViewType view : DashboardViewType.values()) {
		// Component menuItemComponent = new ValoMenuItemButton(view);
		//
		// if (view == DashboardViewType.REPORTS) {
		// // Add drop target to reports button
		// DragAndDropWrapper reports = new DragAndDropWrapper(
		// menuItemComponent);
		// reports.setSizeUndefined();
		// reports.setDragStartMode(DragStartMode.NONE);
		// reports.setDropHandler(new DropHandler() {
		//
		// @Override
		// public void drop(final DragAndDropEvent event) {
		// UI.getCurrent()
		// .getNavigator()
		// .navigateTo(
		// DashboardViewType.REPORTS.getViewName());
		// Table table = (Table) event.getTransferable()
		// .getSourceComponent();
		// DashboardEventBus.post(new TransactionReportEvent(
		// (Collection<Transaction>) table.getValue()));
		// }
		//
		// @Override
		// public AcceptCriterion getAcceptCriterion() {
		// return AcceptItem.ALL;
		// }
		//
		// });
		// menuItemComponent = reports;
		// }
		//
		// if (view == DashboardViewType.DASHBOARD) {
		// notificationsBadge = new Label();
		// notificationsBadge.setId(NOTIFICATIONS_BADGE_ID);
		// menuItemComponent = buildBadgeWrapper(menuItemComponent,
		// notificationsBadge);
		// }
		// if (view == DashboardViewType.REPORTS) {
		// reportsBadge = new Label();
		// reportsBadge.setId(REPORTS_BADGE_ID);
		// menuItemComponent = buildBadgeWrapper(menuItemComponent,
		// reportsBadge);
		// }
		//
		// menuItemsLayout.addComponent(menuItemComponent);
		// }
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
		User user = getCurrentUser();
		settingsItem.setText(user.getFirstName() + " " + user.getLastName());
	}

	// public final class ValoMenuItemButton extends Button {
	//
	// private static final String STYLE_SELECTED = "selected";
	//
	// private final DashboardViewType view;
	//
	// public ValoMenuItemButton(final DashboardViewType view) {
	// this.view = view;
	// setPrimaryStyleName("valo-menu-item");
	// setIcon(view.getIcon());
	// setCaption(view.getViewName().substring(0, 1).toUpperCase()
	// + view.getViewName().substring(1));
	// DashboardEventBus.register(this);
	// addClickListener(new ClickListener() {
	// @Override
	// public void buttonClick(final ClickEvent event) {
	// UI.getCurrent().getNavigator()
	// .navigateTo(view.getViewName());
	// }
	// });
	//
	// }
	//
	// @Subscribe
	// public void postViewChange(final PostViewChangeEvent event) {
	// removeStyleName(STYLE_SELECTED);
	// if (event.getView() == view) {
	// addStyleName(STYLE_SELECTED);
	// }
	// }
	// }
	
}
