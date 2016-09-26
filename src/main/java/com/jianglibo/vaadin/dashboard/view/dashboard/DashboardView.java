package com.jianglibo.vaadin.dashboard.view.dashboard;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEvent.CloseOpenWindowsEvent;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEventBus;
import com.jianglibo.vaadin.dashboard.uicomponent.button.ButtonWillPopupWindow;
import com.jianglibo.vaadin.dashboard.uicomponent.button.NotificationsButton;
import com.jianglibo.vaadin.dashboard.uicomponent.tile.HtmlContentTile;
import com.jianglibo.vaadin.dashboard.uicomponent.tile.NotesTile;
import com.jianglibo.vaadin.dashboard.uicomponent.tile.TileContainer;
import com.jianglibo.vaadin.dashboard.uicomponent.tile.TopTenTile;
import com.jianglibo.vaadin.dashboard.view.dashboard.DashboardEdit.DashboardEditListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@SpringView(name=DashboardView.VIEW_NAME)
public final class DashboardView extends Panel implements View,
        DashboardEditListener {

    public static final String EDIT_ID = "dashboard-edit";
    public static final String TITLE_ID = "dashboard-title";
    
    public static final String VIEW_NAME = "dashboard";
    
    public static final FontAwesome ICON_VALUE = FontAwesome.HOME;

    private Label titleLabel;
    
//    private ButtonWillPopupWindow notificationsButton;
    
//    private CssLayout dashboardPanels;
    
    private final VerticalLayout root;
    
    private TileContainer tc = new TileContainer();

    @Autowired
    public DashboardView(Domains domains, MessageSource messageSource) {
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);

        root = new VerticalLayout();
        root.setSizeFull();
        root.setMargin(true);
        root.addStyleName("dashboard-view");
        setContent(root);
        Responsive.makeResponsive(root);

        root.addComponent(buildHeader());
        
        tc.AddTileMenuClickListener((panel, menuitem, b) -> {
        	toggleMaximized(panel, b);
        });
        
        tc.addTile(new HtmlContentTile(messageSource, "about").setupAndReturnSelf());
        tc.addTile(new NewNewsTile(messageSource, "newnews").setupAndReturnSelf());
        
//        dashboardPanels = content;
        root.addComponent(tc);
        root.setExpandRatio(tc, 1);

        // All the open sub-windows should be closed whenever the root layout
        // gets clicked.
        root.addLayoutClickListener(new LayoutClickListener() {
            @Override
            public void layoutClick(final LayoutClickEvent event) {
                DashboardEventBus.post(new CloseOpenWindowsEvent());
            }
        });
    }



    private Component buildHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);

        titleLabel = new Label("Dashboard");
        titleLabel.setId(TITLE_ID);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

//        notificationsButton = new NotificationsButton();
        
        
//        Component edit = buildEditButton();
//        HorizontalLayout tools = new HorizontalLayout(notificationsButton);
//        tools.setSpacing(true);
//        tools.addStyleName("toolbar");
//        header.addComponent(tools);
//
        return header;
    }

//    private Component buildEditButton() {
//        Button result = new Button();
//        result.setId(EDIT_ID);
//        result.setIcon(FontAwesome.EDIT);
//        result.addStyleName("icon-edit");
//        result.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
//        result.setDescription("Edit Dashboard");
//        result.addClickListener(new ClickListener() {
//            @Override
//            public void buttonClick(final ClickEvent event) {
//                getUI().addWindow(
//                        new DashboardEdit(DashboardView.this, titleLabel
//                                .getValue()));
//            }
//        });
//        return result;
//    }




    @Override
    public void enter(final ViewChangeEvent event) {
//        notificationsButton.updateNotificationsCount(null);
    }

    public TileContainer getTc() {
		return tc;
	}

	@Override
    public void dashboardNameEdited(final String name) {
        titleLabel.setValue(name);
    }

    private void toggleMaximized(final Component panel, final boolean maximized) {
        for (Iterator<Component> it = root.iterator(); it.hasNext();) {
            it.next().setVisible(!maximized);
        }
        tc.setVisible(true);

        for (Iterator<Component> it = tc.iterator(); it.hasNext();) {
            Component c = it.next();
            c.setVisible(!maximized);
        }

        if (maximized) {
            panel.setVisible(true);
            panel.addStyleName("max");
        } else {
            panel.removeStyleName("max");
        }
    }
}
