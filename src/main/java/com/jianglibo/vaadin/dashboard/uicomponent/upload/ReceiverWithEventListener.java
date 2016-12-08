package com.jianglibo.vaadin.dashboard.uicomponent.upload;

import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.SucceededEvent;

public interface ReceiverWithEventListener extends Receiver {
	void uploadFailed(FailedEvent event);
	void uploadSucceeded(SucceededEvent event);
	void uploadFinished(FinishedEvent event);
	void uploadStarted(StartedEvent event);
}
