package com.jianglibo.vaadin.dashboard.view.installationpackages;

import com.jianglibo.vaadin.dashboard.DashboardUI;
import com.jianglibo.vaadin.dashboard.component.MovieDetailsWindow;
import com.vaadin.data.Item;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public class PkSourceActionHandler implements Handler {
	
    private final Action report = new Action("Create Report");

    private final Action discard = new Action("Discard");

    private final Action details = new Action("Movie details");

    @Override
    public void handleAction(final Action action, final Object sender,
            final Object target) {
        if (action == report) {
//            createNewReportFromSelection();
        } else if (action == discard) {
            Notification.show("Not implemented in this demo");
        } else if (action == details) {
            Item item = ((Table) sender).getItem(target);
            if (item != null) {
                Long movieId = (Long) item.getItemProperty("movieId")
                        .getValue();
                MovieDetailsWindow.open(DashboardUI.getDataProvider()
                        .getMovie(movieId), null, null);
            }
        }
    }

    @Override
    public Action[] getActions(final Object target, final Object sender) {
        return new Action[] { details, report, discard };
    }

}
