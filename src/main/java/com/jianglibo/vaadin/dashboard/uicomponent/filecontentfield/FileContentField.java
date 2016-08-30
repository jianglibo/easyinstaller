package com.jianglibo.vaadin.dashboard.uicomponent.filecontentfield;

import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import com.jianglibo.vaadin.dashboard.uicomponent.upload.ImmediateUploader;
import com.jianglibo.vaadin.dashboard.uicomponent.upload.UploadReceiver;
import com.jianglibo.vaadin.dashboard.uicomponent.upload.UploadSuccessEventLinstener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class FileContentField extends CustomField<String> implements UploadReceiver<String>, UploadSuccessEventLinstener<String>{
	
	private TextArea textArea;
	
	@Autowired
	private ApplicationContext applicationContext;
	private Component uploader;
	
	@Override
	protected Component initContent() {
		this.uploader = applicationContext.getBean(ImmediateUploader.class).afterInjection(this);
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
	public UploadReceiver<String> afterInjection(UploadSuccessEventLinstener<String> listener) {
		return this;
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
