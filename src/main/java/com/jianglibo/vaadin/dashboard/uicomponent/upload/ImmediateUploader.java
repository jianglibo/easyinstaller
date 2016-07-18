package com.jianglibo.vaadin.dashboard.uicomponent.upload;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
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
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ImmediateUploader extends HorizontalLayout {

	 private Label status = new Label("Please select a file to upload");

	// private ProgressBar progressBar = new ProgressBar(new Float(0.3));

	private UploadReceiver receiver = new UploadReceiver();

	// private VerticalLayout progressLayout = new VerticalLayout();

	private Upload upload;

	private Button cancelBtn;

	public ImmediateUploader() {
		setSpacing(true);

		// Slow down the upload
		receiver.setSlow(true);

		upload = new Upload("", receiver);
		upload.addStyleName("uploadwrapper");
		

		// addComponent(status);
		addComponent(status);
		addComponent(upload);
		// addComponent(progressLayout);
		
		setComponentAlignment(status, Alignment.MIDDLE_RIGHT);

		// Make uploading start immediately when file is selected
		upload.setImmediate(true);
		upload.setButtonCaption("Select file");

		// progressLayout.setSpacing(true);
		// progressLayout.setVisible(true);
		// progressLayout.addComponent(progressBar);
		// progressBar.setCaption("进度");
		// progressBar.setEnabled(true);
		// progressLayout.setComponentAlignment(progressBar,
		// Alignment.MIDDLE_LEFT);

		cancelBtn = new Button("Cancel");
		cancelBtn.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				upload.interruptUpload();
			}
		});
		// cancelBtn.setStyleName("small");
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
				// progressBar.setValue(0.5f);
				upload.setCaption("Uploading file \"" + event.getFilename() + "\"");

			}
		});

		upload.addProgressListener(new ProgressListener() {
			@Override
			public void updateProgress(long readBytes, long contentLength) {
				// This method gets called several times during the update
				// progressBar.setValue(new Float(readBytes / (float)
				// contentLength));
			}
		});

		upload.addSucceededListener(new SucceededListener() {
			@Override
			public void uploadSucceeded(SucceededEvent event) {
				upload.setCaption("Uploading file \"" + event.getFilename() + "\" succeeded");
			}
		});

		upload.addFailedListener(new FailedListener() {
			@Override
			public void uploadFailed(FailedEvent event) {
				upload.setCaption("Uploading interrupted");

			}
		});

		upload.addFinishedListener(new FinishedListener() {
			@Override
			public void uploadFinished(FinishedEvent event) {
				cancelBtn.setVisible(false);
				upload.setVisible(true);
				upload.setCaption("Select another file");
			}
		});
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
