package com.jianglibo.vaadin.dashboard.view.installationpackages;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;

import com.jianglibo.vaadin.dashboard.config.ApplicationConfig;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class FileUploadReceiver  implements Receiver, SucceededListener {
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private ApplicationConfig applicationConfig;
	
    public File file;

    public OutputStream receiveUpload(String filename,
                                      String mimeType) {
        // Create upload stream
        FileOutputStream fos = null; // Stream to write to
        try {
            // Open the file for writing.
            file = applicationConfig.getUploadDstPath().resolve(filename).toFile();
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
//        image.setVisible(true);
//        image.setSource(new FileResource(file));
        new Notification("success<br/>",
                "",
                Notification.Type.HUMANIZED_MESSAGE)
   .show(Page.getCurrent());
    }
}

