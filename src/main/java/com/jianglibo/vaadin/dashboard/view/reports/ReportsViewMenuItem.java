package com.jianglibo.vaadin.dashboard.view.reports;

import java.util.Collection;

import com.google.common.eventbus.Subscribe;
import com.jianglibo.vaadin.dashboard.annotation.MainMenu;
import com.jianglibo.vaadin.dashboard.domain.Transaction;
import com.jianglibo.vaadin.dashboard.event.DashboardEventBus;
import com.jianglibo.vaadin.dashboard.event.DashboardEvent.ReportsCountUpdatedEvent;
import com.jianglibo.vaadin.dashboard.event.DashboardEvent.TransactionReportEvent;
import com.jianglibo.vaadin.dashboard.view.DboardViewUtil;
import com.jianglibo.vaadin.dashboard.view.MenuItemWrapper;
import com.jianglibo.vaadin.dashboard.view.ValoMenuItemButton;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.ui.Component;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.AbstractSelect.AcceptItem;
import com.vaadin.ui.DragAndDropWrapper.DragStartMode;

@MainMenu(menuOrder = 10)
public class ReportsViewMenuItem implements MenuItemWrapper {

	public static final String REPORTS_BADGE_ID = "dashboard-menu-reports-badge";

	private Component menuItem;

	private Label reportsBadge;

	public ReportsViewMenuItem() {
		// Add drop target to reports button
		Component menuItemComponent = new ValoMenuItemButton(ReportsView.VIEW_NAME, ReportsView.ICON_VALUE);
		DragAndDropWrapper reports = new DragAndDropWrapper(menuItemComponent);
		reports.setSizeUndefined();
		reports.setDragStartMode(DragStartMode.NONE);
		reports.setDropHandler(new DropHandler() {

			@Override
			public void drop(final DragAndDropEvent event) {
				UI.getCurrent().getNavigator().navigateTo(ReportsView.VIEW_NAME);
				Table table = (Table) event.getTransferable().getSourceComponent();
				DashboardEventBus.post(new TransactionReportEvent((Collection<Transaction>) table.getValue()));
			}

			@Override
			public AcceptCriterion getAcceptCriterion() {
				return AcceptItem.ALL;
			}

		});

		reportsBadge = new Label();
		reportsBadge.setId(REPORTS_BADGE_ID);
		menuItemComponent = reports;

		menuItemComponent = DboardViewUtil.buildBadgeWrapper(menuItemComponent, reportsBadge);

		this.menuItem = menuItemComponent;
		DashboardEventBus.register(this);
	}

	public Component getMenuItem() {
		return menuItem;
	}

	@Subscribe
	public void updateReportsCount(final ReportsCountUpdatedEvent event) {
		reportsBadge.setValue(String.valueOf(event.getCount()));
		reportsBadge.setVisible(event.getCount() > 0);
	}

	@Override
	public void onAttach() {
		// TODO Auto-generated method stub

	}
}
