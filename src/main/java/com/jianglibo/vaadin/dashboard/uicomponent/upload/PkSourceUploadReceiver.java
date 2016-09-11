package com.jianglibo.vaadin.dashboard.uicomponent.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.jianglibo.vaadin.dashboard.config.ApplicationConfig;
import com.jianglibo.vaadin.dashboard.domain.PkSource;
import com.jianglibo.vaadin.dashboard.repositories.PkSourceRepository;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;


@SuppressWarnings("serial")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PkSourceUploadReceiver implements UploadReceiver<PkSourceUploadFinishResult>  {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(PkSourceUploadReceiver.class);
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ApplicationConfig applicationConfig;
	
	@Autowired
	private PkSourceRepository pkSourceRepository;

	public File file;
	
	public String filename;
	
	private String mimeType;
	
	private UploadSuccessEventLinstener<PkSourceUploadFinishResult> ufeListener;
	
	public PkSourceUploadReceiver afterInjection(UploadSuccessEventLinstener<PkSourceUploadFinishResult> ufeListener) {
		this.ufeListener = ufeListener;
		return this;
	}

	public OutputStream receiveUpload(String filename, String mimeType) {
		// Create upload stream
		
		this.filename = filename;
		this.mimeType = mimeType;
		FileOutputStream fos = null; // Stream to write to
		try {
			// Open the file for writing.
			String uuid = UUID.randomUUID().toString();
			file = applicationConfig.getUploadDstPath().resolve(uuid).toFile();
			fos = new FileOutputStream(file);
		} catch (final java.io.FileNotFoundException e) {
			new Notification(messageSource.getMessage("component.upload.cantopenfile", new String[]{file.toString()}, UI.getCurrent().getLocale()), "", Notification.Type.ERROR_MESSAGE)
					.show(Page.getCurrent());
			return null;
		}
		return fos;
	}

	@Override
	public void uploadSuccessed() {
		try {
			String md5 = Files.asByteSource(file).hash(Hashing.md5()).toString();
			PkSource ps = pkSourceRepository.findByFileMd5(md5);
			if (ps == null) {
				String extNoDot = Files.getFileExtension(filename);
				File nf = new File(file.getParentFile(), md5 + "." + extNoDot);
				if (!nf.exists()) {
					Files.move(file, nf);
				}
				ps = new PkSource.PkSourceBuilder(md5, filename, nf.length(), extNoDot, mimeType).build();
				pkSourceRepository.save(ps);
				ufeListener.onUploadSuccess(new PkSourceUploadFinishResult(ps));
			} else {
				ufeListener.onUploadSuccess(new PkSourceUploadFinishResult(ps));
				new Notification(messageSource.getMessage("component.upload.duplicated", new String[]{filename}, UI.getCurrent().getLocale()), "", Notification.Type.ERROR_MESSAGE)
				.show(Page.getCurrent());
			}
			if (file.exists()) {
				file.delete();
			}
		} catch (IOException e) {
			new Notification(messageSource.getMessage("component.upload.hashing", new String[]{filename}, UI.getCurrent().getLocale()), "", Notification.Type.ERROR_MESSAGE)
			.show(Page.getCurrent());
			LOGGER.error("hashing {} failed.", filename);
		}
	}
	
	public void uploadNotSuccess() {
		if (file != null) {
			file.delete();
		}
	}
}
