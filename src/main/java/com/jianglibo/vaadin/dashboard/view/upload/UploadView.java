package com.jianglibo.vaadin.dashboard.view.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings({ "serial"})
@SpringView(name=UploadView.VIEW_NAME)
public class UploadView extends VerticalLayout implements View {
	public static final String VIEW_NAME = "uploader";
	
	@Autowired
	private MessageSource messageSource;
	
	private Upload upload;
	
	public UploadView() {
		addComponent(buildToolbar());
	}

    private Component buildToolbar() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);
        Responsive.makeResponsive(header);

        Label title = new Label("Latest Transactions");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(title);

        upload = new Upload("Upload it here", new ImageUploader());
//        HorizontalLayout tools = new HorizontalLayout(buildFilter(),
//                createReport);
//        tools.setSpacing(true);
//        tools.addStyleName("toolbar");
//        header.addComponent(tools);
        header.addComponent(upload);

        return header;
    }
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
	public static class ImageUploader implements Receiver, SucceededListener {
	    public File file;

	    public OutputStream receiveUpload(String filename,
	                                      String mimeType) {
	        // Create upload stream
	        FileOutputStream fos = null; // Stream to write to
	        try {
	            // Open the file for writing.
	            file = new File("/tmp/uploads/" + filename);
	            fos = new FileOutputStream(file);
	        } catch (final java.io.FileNotFoundException e) {
	            new Notification("Could not open file<br/>",
	                             e.getMessage(),
	                             Notification.Type.ERROR_MESSAGE)
	                .show(Page.getCurrent());
	            return null;
	        }
	        return fos; // Return the output stream to write to
	    }

	    public void uploadSucceeded(SucceededEvent event) {
	        // Show the uploaded file in the image viewer
//	        image.setVisible(true);
//	        image.setSource(new FileResource(file));
            new Notification("success<br/>",
                    "",
                    Notification.Type.ERROR_MESSAGE)
       .show(Page.getCurrent());
	    }
	};

}
