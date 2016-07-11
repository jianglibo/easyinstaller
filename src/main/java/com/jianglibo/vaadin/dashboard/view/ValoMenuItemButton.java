package com.jianglibo.vaadin.dashboard.view;

import com.google.common.eventbus.Subscribe;
import com.jianglibo.vaadin.dashboard.event.DashboardEventBus;
import com.jianglibo.vaadin.dashboard.event.DashboardEvent.PostViewChangeEvent;
import com.vaadin.ui.UI;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;

@SuppressWarnings("serial")
public class ValoMenuItemButton extends Button {

    private static final String STYLE_SELECTED = "selected";

    private final String viewName;

    public ValoMenuItemButton(final String viewName, FontAwesome icon) {
        this.viewName = viewName;
        setPrimaryStyleName("valo-menu-item");
        setIcon(icon);
        setCaption(viewName.substring(0, 1).toUpperCase()
                + viewName.substring(1));
        DashboardEventBus.register(this);
        addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                UI.getCurrent().getNavigator()
                        .navigateTo(viewName);
            }
        });
    }

    @Subscribe
    public void postViewChange(final PostViewChangeEvent event) {
        removeStyleName(STYLE_SELECTED);
        if (viewName.equals(event.getViewName())) {
            addStyleName(STYLE_SELECTED);
        }
    }
}
