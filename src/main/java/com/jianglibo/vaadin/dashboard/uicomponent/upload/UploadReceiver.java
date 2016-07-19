package com.jianglibo.vaadin.dashboard.uicomponent.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;

import com.jianglibo.vaadin.dashboard.config.ApplicationConfig;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload.Receiver;


@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class UploadReceiver implements Receiver {
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ApplicationConfig applicationConfig;

	public File file;

	public OutputStream receiveUpload(String filename, String mimeType) {
		// Create upload stream
		FileOutputStream fos = null; // Stream to write to
		try {
			// Open the file for writing.
			file = applicationConfig.getUploadDstPath().resolve(filename).toFile();
			fos = new FileOutputStream(file);
		} catch (final java.io.FileNotFoundException e) {
			new Notification(messageSource.getMessage("component.upload.cantopenfile", new String[]{file.toString()}, UI.getCurrent().getLocale()), "", Notification.Type.ERROR_MESSAGE)
					.show(Page.getCurrent());
			return null;
		}
		return fos;
	}

}
