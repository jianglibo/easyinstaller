package com.jianglibo.vaadin.dashboard.uicomponent.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;

import com.google.common.eventbus.EventBus;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.FinishedListener;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class ImmediateUploader extends HorizontalLayout {

	 private Label status = new Label("");

	// private ProgressBar progressBar = new ProgressBar(new Float(0.3));

	@Autowired
	private UploadReceiver receiver;

	// private VerticalLayout progressLayout = new VerticalLayout();
	
	@Autowired
	private MessageSource messageSource;

	private Upload upload;

	private Button cancelBtn;

	public Component afterInjection(EventBus eventBus) {
		setSpacing(true);
		this.upload = new Upload("", receiver.afterInjection(eventBus));
		this.upload.addStyleName("uploadwrapper");

		// addComponent(status);
		addComponent(status);
		addComponent(upload);
		// addComponent(progressLayout);
		
		setComponentAlignment(status, Alignment.MIDDLE_RIGHT);

		// Make uploading start immediately when file is selected
		upload.setImmediate(true);
		upload.setButtonCaption(messageSource.getMessage("component.upload.selectfile", null, UI.getCurrent().getLocale()));

		cancelBtn = new Button(messageSource.getMessage("component.upload.cancel", null, UI.getCurrent().getLocale()));
		cancelBtn.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				upload.interruptUpload();
				new Notification(messageSource.getMessage("component.upload.interrupt", null, UI.getCurrent().getLocale()), "", Notification.Type.WARNING_MESSAGE)
				.show(Page.getCurrent());

			}
		});
		addComponent(cancelBtn);
		cancelBtn.setVisible(false);

		/**
		 * =========== Add needed listener for the upload component: start,
		 * progress, finish, success, fail ===========
		 */

		upload.addStartedListener(new StartedListener() {
			@Override
			public void uploadStarted(StartedEvent event) {
				upload.setVisible(false);
				cancelBtn.setVisible(true);
				status.setValue(event.getFilename());
			}
		});

		upload.addProgressListener(new ProgressListener() {
			@Override
			public void updateProgress(long readBytes, long contentLength) {
				double d = (double)readBytes/contentLength;
				String per = Math.round(d * 100) + "%";
				cancelBtn.setCaption(messageSource.getMessage("component.upload.cancel", null, UI.getCurrent().getLocale()) + " " + per);
			}
		});

		upload.addSucceededListener(new SucceededListener() {
			@Override
			public void uploadSucceeded(SucceededEvent event) {
				receiver.uploadSuccessed();
				new Notification(messageSource.getMessage("component.upload.success", new String[]{event.getFilename()}, UI.getCurrent().getLocale()), "", Notification.Type.TRAY_NOTIFICATION)
				.show(Page.getCurrent());
			}
		});
		

		upload.addFailedListener(new FailedListener() {
			@Override
			public void uploadFailed(FailedEvent event) {
				receiver.uploadNotSuccess();
				new Notification(messageSource.getMessage("component.upload.fail", new String[]{event.getFilename()}, UI.getCurrent().getLocale()), "", Notification.Type.ERROR_MESSAGE)
				.show(Page.getCurrent());
			}
		});

		upload.addFinishedListener(new FinishedListener() {
			@Override
			public void uploadFinished(FinishedEvent event) {
				status.setValue("");
				cancelBtn.setVisible(false);
				upload.setVisible(true);
			}
		});
		return this;
	}

//	class WorkThread extends Thread {
//		// Volatile because read in another thread in access()
//		volatile double current = 0.0;
//
//		@Override
//		public void run() {
//			// Count up until 1.0 is reached
//			while (current < 1.0) {
//				current += 0.01;
//				// Do some "heavy work"
//				try {
//					sleep(50); // Sleep for 50 milliseconds
//				} catch (InterruptedException e) {
//				}
//
//				// Update the UI thread-safely
//				UI.getCurrent().access(new Runnable() {
//					@Override
//					public void run() {
//						// progress.setValue(new Float(current));
//						if (current < 1.0)
//							status.setValue("" + ((int) (current * 100)) + "% done");
//						else
//							status.setValue("all done");
//					}
//				});
//			}
//
//			// Show the "all done" for a while
//			try {
//				sleep(2000); // Sleep for 2 seconds
//			} catch (InterruptedException e) {
//			}
//
//			// Update the UI thread-safely
//			UI.getCurrent().access(new Runnable() {
//				@Override
//				public void run() {
//					// Restore the state to initial
//					// progress.setValue(new Float(0.0));
//					// progress.setEnabled(false);
//
//					// Stop polling
//					UI.getCurrent().setPollInterval(-1);
//
//					// button.setEnabled(true);
//					status.setValue("not running");
//				}
//			});
//		}
//	}
}
