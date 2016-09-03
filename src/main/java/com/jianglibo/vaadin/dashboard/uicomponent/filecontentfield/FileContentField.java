package com.jianglibo.vaadin.dashboard.uicomponent.filecontentfield;

import java.io.OutputStream;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.uicomponent.upload.ImmediateUploader;
import com.jianglibo.vaadin.dashboard.uicomponent.upload.UploadReceiver;
import com.jianglibo.vaadin.dashboard.uicomponent.upload.UploadSuccessEventLinstener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class FileContentField extends CustomField<String> implements UploadReceiver<String>, UploadSuccessEventLinstener<String>{
	
	private TextArea textArea;
	
	private Component uploader;
	
	private final MessageSource messageSource;
	
	public FileContentField(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@Override
	protected Component initContent() {
		this.uploader = new ImmediateUploader(messageSource, this);
		this.textArea = new TextArea();
		this.textArea.setWidth("100%");
		VerticalLayout vl = new VerticalLayout();
		vl.addComponent(textArea);
		vl.addComponent(uploader);
		return vl;
	}

	@Override
	public Class<? extends String> getType() {
		return String.class;
	}

	/* ---------- upload ----------------- */
	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onUploadSuccess(String ufe) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void uploadSuccessed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void uploadNotSuccess() {
		// TODO Auto-generated method stub
		
	}
}
