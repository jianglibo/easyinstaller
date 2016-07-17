package com.jianglibo.vaadin.dashboard;


import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.server.SystemMessagesProvider;
import com.vaadin.spring.server.SpringVaadinServlet;

@SuppressWarnings("serial")
public class DashboardServlet extends SpringVaadinServlet {
	
	@Autowired
	private SystemMessagesProvider provider;

    @Override
    protected final void servletInitialized() throws ServletException {
        super.servletInitialized();
        getService().addSessionInitListener(new DashboardSessionInitListener());
        getService().setSystemMessagesProvider(provider
//        	    new SystemMessagesProvider() {
//        	    @Override
//        	    public SystemMessages getSystemMessages(
//        	        SystemMessagesInfo systemMessagesInfo) {
//        	        CustomizedSystemMessages messages =
//        	                new CustomizedSystemMessages();
//        	        messages.setCommunicationErrorCaption("Comm Err");
//        	        messages.setCommunicationErrorMessage("This is bad.");
//        	        messages.setCommunicationErrorNotificationEnabled(true);
//        	        messages.setCommunicationErrorURL("http://vaadin.com/");
//        	        return messages;
//        	    }
//        	}
        	    );
    }
}