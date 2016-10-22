package com.jianglibo.vaadin.dashboard.uicomponent.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.jianglibo.vaadin.dashboard.domain.PkSource;
import com.jianglibo.vaadin.dashboard.repositories.PkSourceRepository;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;


@SuppressWarnings("serial")
public class PkSourceUploadReceiver implements UploadReceiver<PkSourceUploadFinishResult>  {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(PkSourceUploadReceiver.class);
	
	private final MessageSource messageSource;

	private Path uploadDstPath;
	
	private final PkSourceRepository pkSourceRepository;

	public File file;
	
	public String filename;
	
	private String mimeType;
	
	private UploadSuccessEventLinstener<PkSourceUploadFinishResult> ufeListener;
	
	public PkSourceUploadReceiver(MessageSource messageSource,Path uploadDstPath, PkSourceRepository pkSourceRepository, UploadSuccessEventLinstener<PkSourceUploadFinishResult> ufeListener) {
		this.messageSource = messageSource;
		this.pkSourceRepository = pkSourceRepository;
		this.ufeListener = ufeListener;
		this.uploadDstPath = uploadDstPath;
	}

	public OutputStream receiveUpload(String filename, String mimeType) {
		// Create upload stream
		
		this.filename = filename;
		this.mimeType = mimeType;
		FileOutputStream fos = null; // Stream to write to
		try {
			// Open the file for writing.
			String uuid = UUID.randomUUID().toString();
			file = uploadDstPath.resolve(uuid).toFile();
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
