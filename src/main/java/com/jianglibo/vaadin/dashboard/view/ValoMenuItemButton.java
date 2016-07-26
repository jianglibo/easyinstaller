package com.jianglibo.vaadin.dashboard.view;

import org.springframework.context.MessageSource;

import com.google.common.eventbus.Subscribe;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEventBus;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEvent.PostViewChangeEvent;
import com.vaadin.ui.UI;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;

@SuppressWarnings("serial")
public class ValoMenuItemButton extends Button {

    private static final String STYLE_SELECTED = "selected";

    private final String viewName;

    public ValoMenuItemButton(final String viewName, FontAwesome icon, MessageSource messageSource) {
        this.viewName = viewName;
        setPrimaryStyleName("valo-menu-item");
        setIcon(icon);
        setCaption(messageSource.getMessage("menu." + viewName, null, UI.getCurrent().getLocale()));
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
