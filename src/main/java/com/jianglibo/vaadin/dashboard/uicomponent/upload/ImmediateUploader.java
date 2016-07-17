package com.jianglibo.vaadin.dashboard.uicomponent.upload;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
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
public class ImmediateUploader extends VerticalLayout {

	private Label status = new Label("Please select a file to upload");

	private ProgressBar progressBar = new ProgressBar(new Float(0.3));

	private UploadReceiver receiver = new UploadReceiver();

	private VerticalLayout progressLayout = new VerticalLayout();

	private Upload upload = new Upload("", receiver);

	public ImmediateUploader() {
		setSpacing(true);

		// Slow down the upload
		receiver.setSlow(true);

		addComponent(status);
		addComponent(upload);
		addComponent(progressLayout);

		// Make uploading start immediately when file is selected
		upload.setImmediate(true);
		upload.setButtonCaption("Select file");

		progressLayout.setSpacing(true);
		progressLayout.setVisible(true);
		progressLayout.addComponent(progressBar);
		progressBar.setCaption("进度");
		progressBar.setEnabled(true);
		progressLayout.setComponentAlignment(progressBar, Alignment.MIDDLE_LEFT);

		final Button cancelBtn = new Button("Cancel");
		cancelBtn.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				upload.interruptUpload();
			}
		});
		cancelBtn.setStyleName("small");
		progressLayout.addComponent(cancelBtn);

		/**
		 * =========== Add needed listener for the upload component: start,
		 * progress, finish, success, fail ===========
		 */

		upload.addStartedListener(new StartedListener() {
			
			@Override
			public void uploadStarted(StartedEvent event) {
				upload.setVisible(false);
				progressLayout.setVisible(true);
				progressBar.setValue(0.5f);
				status.setValue("Uploading file \"" + event.getFilename() + "\"");
				
			}
		});

		upload.addProgressListener(new ProgressListener() {
			
			@Override
			public void updateProgress(long readBytes, long contentLength) {
					// This method gets called several times during the update
					progressBar.setValue(new Float(readBytes / (float) contentLength));
			}
		});

		upload.addSucceededListener(new SucceededListener() {
			
			@Override
			public void uploadSucceeded(SucceededEvent event) {
				status.setValue("Uploading file \"" + event.getFilename() + "\" succeeded");
			}
		});

		
		upload.addFailedListener(new FailedListener() {
			@Override
			public void uploadFailed(FailedEvent event) {
				status.setValue("Uploading interrupted");
				
			}
		});

		
		upload.addFinishedListener(new FinishedListener() {
			@Override
			public void uploadFinished(FinishedEvent event) {
				progressLayout.setVisible(false);
				upload.setVisible(true);
				upload.setCaption("Select another file");
			}
		});
	}
}
