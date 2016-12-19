package com.jianglibo.vaadin.dashboard.sshrunner;

import java.nio.file.Path;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.ChannelSftp;
import com.jianglibo.vaadin.dashboard.config.ApplicationConfig;
import com.jianglibo.vaadin.dashboard.ssh.JschSession;

/**
 * ApplicationConfig has a configurable remoteFolder property. Files upload to
 * that folder.
 * 
 * @author jianglibo@gmail.com
 *
 */
@Component
public class SshDownloader {

	@Autowired
	private ApplicationConfig applicationConfig;

	public Path download(JschSession jsession, String remoteFilePath, String localFilePath) {

		String uuid = UUID.randomUUID().toString();
		Path dstFile = applicationConfig.getUploadDstPath().resolve(uuid);
		
		ChannelSftp sftp = null;
		try {
			sftp = jsession.getSftpCh();
			sftp.connect();
			sftp.get(remoteFilePath, dstFile.toAbsolutePath().toString());
			return dstFile;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sftp != null) {
				sftp.disconnect();
			}
		}
		return null;
	}
}
