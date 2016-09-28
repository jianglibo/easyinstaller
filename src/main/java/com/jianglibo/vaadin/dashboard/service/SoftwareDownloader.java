package com.jianglibo.vaadin.dashboard.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.io.ByteStreams;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.jianglibo.vaadin.dashboard.Broadcaster;
import com.jianglibo.vaadin.dashboard.Broadcaster.BroadCasterMessage;
import com.jianglibo.vaadin.dashboard.config.ApplicationConfig;
import com.jianglibo.vaadin.dashboard.vo.FileToUploadVo;

/**
 * 
 * @author jianglibo@gmail.com
 *
 */
@Component
public class SoftwareDownloader {

	private static final Logger LOGGER = LoggerFactory.getLogger(SoftwareDownloader.class);

	private ListeningExecutorService service;
	
	private final Path localFolder;

	@Autowired
	public SoftwareDownloader(ApplicationConfig applicationConfig) {
		this.service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
		this.localFolder = applicationConfig.getLocalFolderPath();
	}
	
	public static class DownloadMessage {
		private String fileUrl;
		private String stage = "start";
		private boolean success;
		
		public DownloadMessage(String fileUrl) {
			super();
			this.fileUrl = fileUrl;
		}
		
		public DownloadMessage(String fileUrl, boolean success) {
			super();
			this.fileUrl = fileUrl;
			this.stage = "end";
			this.success = success;
		}
		
		public DownloadMessage(String fileUrl, String stage, boolean success) {
			super();
			this.fileUrl = fileUrl;
			this.stage = stage;
			this.success = success;
		}
		
		public String getFileUrl() {
			return fileUrl;
		}
		public void setFileUrl(String fileUrl) {
			this.fileUrl = fileUrl;
		}
		public String getStage() {
			return stage;
		}
		public void setStage(String stage) {
			this.stage = stage;
		}
		public boolean isSuccess() {
			return success;
		}
		public void setSuccess(boolean success) {
			this.success = success;
		}
	}

	public void submitTasks(FileToUploadVo fvo) {
		if (Files.exists(localFolder.resolve(fvo.getRelative()))) {
			return;
		}
		
		if (fvo.isRemoteFile()) {
			Broadcaster.broadcast(new BroadCasterMessage(new DownloadMessage(fvo.getOrignValue()), Broadcaster.BroadCasterMessageType.DOWNLOAD));
			
			ListenableFuture<Boolean> lf = service.submit(new DownloadOne(fvo));

			Futures.addCallback(lf, new FutureCallback<Boolean>() {
				@Override
				public void onSuccess(Boolean result) {
					Broadcaster.broadcast(new BroadCasterMessage(new DownloadMessage(fvo.getOrignValue(), result), Broadcaster.BroadCasterMessageType.DOWNLOAD));
				}

				@Override
				public void onFailure(Throwable t) {
					Broadcaster.broadcast(new BroadCasterMessage(new DownloadMessage(fvo.getOrignValue(), false), Broadcaster.BroadCasterMessageType.DOWNLOAD));	
				}
			} );
		}
	}

	private class DownloadOne implements Callable<Boolean> {

		private FileToUploadVo fvo;

		public DownloadOne(FileToUploadVo fvo) {
			this.fvo= fvo;
		}

		@Override
		public Boolean call() throws Exception {
			String fn = fvo.getRelative();
			int idx = fn.lastIndexOf('/');
			if (idx != -1) {
				fn = fn.substring(idx);
			}
			if (fn == null) {
				return false;
			}
			Path target = localFolder.resolve(fn);
			try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
				CloseableHttpResponse response = null;
				try {
					Path tmpFile = Files.createTempFile(HttpPageGetter.class.getName(), "");
					HttpGet httpget = new HttpGet(fvo.getOrignValue());
					response = httpclient.execute(httpget);
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						InputStream instream = entity.getContent();
						OutputStream outstream = Files.newOutputStream(tmpFile);
						try {
							ByteStreams.copy(instream, outstream);
							instream.close();
							outstream.flush();
							outstream.close();
							Files.move(tmpFile, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
							return true;
						} finally {
						}
					}
				} catch (IOException e) {
				} finally {
					if (response != null) {
						try {
							response.close();
						} catch (IOException e) {
						}
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return false;
		}
	}
}
