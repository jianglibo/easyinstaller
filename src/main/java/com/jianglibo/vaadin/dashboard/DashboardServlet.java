package com.jianglibo.vaadin.dashboard;


import javax.servlet.ServletException;

import com.vaadin.spring.server.SpringVaadinServlet;

@SuppressWarnings("serial")
public class DashboardServlet extends SpringVaadinServlet {

    @Override
    protected final void servletInitialized() throws ServletException {
        super.servletInitialized();
        getService().addSessionInitListener(new DashboardSessionInitListener());
    }
}