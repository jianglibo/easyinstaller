package com.jianglibo.vaadin.dashboard.uicomponent.button;

import com.google.common.eventbus.Subscribe;
import com.jianglibo.vaadin.dashboard.DashboardUI;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEvent.NotificationsCountUpdatedEvent;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEventBus;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * If need a number icon at up right corner, button must has "notifications" and "unread" two styles.
 * 
 * @author jianglibo@gmail.com
 *
 */
@SuppressWarnings("serial")
public abstract class ButtonWillPopupWindow extends Button {
	
        private static final String STYLE_UNREAD = "unread";
        
        public static final String ID = "dashboard-notifications";
        
        private Window popupWindow;
        
        private boolean showNumberIcon;

        public ButtonWillPopupWindow(boolean showNumberIcon, Resource icon) {
        	this.showNumberIcon = showNumberIcon;
            setIcon(icon);
            setId(ID);
            
            if (showNumberIcon) {
            	addStyleName("notifications");
            }
            
            addStyleName(ValoTheme.BUTTON_ICON_ONLY);
            
            addClickListener(new ClickListener() {
                @Override
                public void buttonClick(final ClickEvent event) {
                    openNotificationsPopup(event);
                }
            });
            DashboardEventBus.register(this);
        }

        @Subscribe
        public void updateNotificationsCount(
                final NotificationsCountUpdatedEvent event) {
            setUnreadCount(DashboardUI.getDataProvider()
                    .getUnreadNotificationsCount());
        }

        public void setUnreadCount(final int count) {
            setCaption(String.valueOf(count));

            String description = "Notifications";
            if (count > 0) {
                addStyleName(STYLE_UNREAD);
                description += " (" + count + " unread)";
            } else {
                removeStyleName(STYLE_UNREAD);
            }
            setDescription(description);
        }
        
        private void openNotificationsPopup(final ClickEvent event) {
            if (popupWindow == null) {
                popupWindow = new Window();
                popupWindow.setWidth(300.0f, Unit.PIXELS);
                popupWindow.addStyleName("notifications");
                popupWindow.setClosable(false);
                popupWindow.setResizable(false);
                popupWindow.setDraggable(false);
                popupWindow.addCloseShortcut(KeyCode.ESCAPE, null);
                customizeWindow(popupWindow);
                popupWindow.setContent(getWindowContent());
            }

            if (!popupWindow.isAttached()) {
                popupWindow.setPositionY(event.getClientY()
                        - event.getRelativeY() + 40);
                getUI().addWindow(popupWindow);
                popupWindow.focus();
            } else {
                popupWindow.close();
            }
        }
        
        protected abstract void customizeWindow(Window popupWindow);

        protected abstract VerticalLayout getWindowContent();

    }

