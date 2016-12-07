package com.jianglibo.vaadin.dashboard.uicomponent.upload;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import com.google.common.io.Files;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.SucceededEvent;

@SuppressWarnings("serial")
public class TextContentReceiver implements ReceiverWithEventListener {
		
		private String content;
		
		private boolean success = false;
		
		private ByteArrayOutputStream baos;
		
		private String mimeType;
		
		private final SimplifiedUploadResultLinstener<String, TextUploadResult> usel;
		
		public TextContentReceiver(SimplifiedUploadResultLinstener<String, TextUploadResult> usel) {
			this.usel = usel;
		}
		
		@Override
		public OutputStream receiveUpload(String filename, String mimeType) {
			this.setMimeType(mimeType);
			if (!"zip".equalsIgnoreCase(Files.getFileExtension(filename))) {
				return null;
			} else {
				baos = new ByteArrayOutputStream();
				return baos;
			}
		}

		private String guessCharset() {
			return StandardCharsets.UTF_8.name();
		}


		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getMimeType() {
			return mimeType;
		}

		public void setMimeType(String mimeType) {
			this.mimeType = mimeType;
		}

		@Override
		public void uploadFailed(FailedEvent event) {
			this.setSuccess(false);
			this.baos = null;
			this.usel.onUploadResult(TextUploadResult.createFailed(event.getReason().getMessage()));
		}

		@Override
		public void uploadSucceeded(SucceededEvent event) {
			try {
				this.setContent(baos.toString(guessCharset()));
				this.usel.onUploadResult(TextUploadResult.createSuccessed(getContent()));
				this.setSuccess(true);
			} catch (UnsupportedEncodingException e) {
				this.usel.onUploadResult(TextUploadResult.createFailed("UnsupportedEncodingException"));
				this.setSuccess(false);
			}
		}

		@Override
		public void uploadFinished(FinishedEvent event) {
			// TODO Auto-generated method stub
			
		}
}
