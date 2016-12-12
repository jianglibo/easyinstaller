package com.jianglibo.vaadin.dashboard.service;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.jianglibo.vaadin.dashboard.Broadcaster;
import com.jianglibo.vaadin.dashboard.Broadcaster.BroadCasterMessage;
import com.jianglibo.vaadin.dashboard.Broadcaster.BroadCasterMessageBody;
import com.jianglibo.vaadin.dashboard.Broadcaster.BroadCasterMessageType;
import com.jianglibo.vaadin.dashboard.config.ApplicationConfig;
import com.jianglibo.vaadin.dashboard.repositories.SoftwareRepository;
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
	
	private final SoftwareRepository softwareRepository;
	
	private Set<String> downloadings = Sets.newHashSet();

	@Autowired
	public SoftwareDownloader(ApplicationConfig applicationConfig, SoftwareRepository softwareRepository) {
		this.service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
		this.softwareRepository = softwareRepository;
		this.localFolder = applicationConfig.getLocalFolderPath();
	}
	
	@PostConstruct
	public void postContruct() {
		softwareRepository.findAll().stream().map(sw -> sw.getFilesToUpload()).flatMap(fs -> fs.stream()).map(FileToUploadVo::new).filter(FileToUploadVo::isRemoteFile).forEach(ftu -> {
			submitTasks(ftu);
		});
	}
	
	public static class DownloadMessage implements BroadCasterMessageBody {
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

		@Override
		public BroadCasterMessageType getBroadCasterMessageType() {
			return BroadCasterMessageType.DOWNLOAD;
		}
	}
	
	private synchronized boolean detectDownloading(String value, boolean remove) {
		if (remove) {
			if (downloadings.contains(value)) {
				downloadings.remove(value);
				return true;
			}
		} else {
			if (downloadings.contains(value)) {
				LOGGER.info("downloading {} already submited, skip it.", value);
				return true;
			}
		}
		return false;
	}

	public void submitTasks(FileToUploadVo fvo) {
		if (Files.exists(localFolder.resolve(fvo.getRelative()))) {
			return;
		}
		
		if (fvo.isRemoteFile()) {
			if (detectDownloading(fvo.getOrignValue(), false)) {
				return;
			}
			LOGGER.info("start download file from {}", fvo.getOrignValue());
			Broadcaster.broadcast(new BroadCasterMessage(new DownloadMessage(fvo.getOrignValue())));
			
			ListenableFuture<Boolean> lf = service.submit(new DownloadOne(fvo));

			Futures.addCallback(lf, new FutureCallback<Boolean>() {
				@Override
				public void onSuccess(Boolean result) {
					detectDownloading(fvo.getOrignValue(), true);
					Broadcaster.broadcast(new BroadCasterMessage(new DownloadMessage(fvo.getOrignValue(), result)));
				}

				@Override
				public void onFailure(Throwable t) {
					detectDownloading(fvo.getOrignValue(), true);
					Broadcaster.broadcast(new BroadCasterMessage(new DownloadMessage(fvo.getOrignValue(), false)));	
				}
			} );
		}
	}

	private class DownloadOne implements Callable<Boolean> {

		private FileToUploadVo fvo;
		
		private static final int BUF_SIZE = 0x1000;

		public DownloadOne(FileToUploadVo fvo) {
			this.fvo= fvo;
		}
		
		private long copy(InputStream from, OutputStream to)
			      throws IOException {
		    checkNotNull(from);
		    checkNotNull(to);
		    byte[] buf = new byte[BUF_SIZE];
		    long total = 0;
		    long lastPosition = 0;
		    while (true) {
		      int r = from.read(buf);
		      if (r == -1) {
		        break;
		      }
		      to.write(buf, 0, r);
		      total += r;
		      if ((total - lastPosition) > 512000) {
		    	  lastPosition = total;
		    	  LOGGER.info(fvo.getOrignValue() + " has download " + (total / 1024) + "K till now.");
		      }
		    }
		    return total;
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
							copy(instream, outstream);
							instream.close();
							outstream.flush();
							outstream.close();
							Files.move(tmpFile, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
							return true;
						} catch(Exception e) {
							Files.copy(tmpFile, target, StandardCopyOption.REPLACE_EXISTING);
							Files.delete(tmpFile);
						} finally {
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (response != null) {
						try {
							response.close();
						} catch (IOException e) {
						}
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return false;
		}
	}
}
