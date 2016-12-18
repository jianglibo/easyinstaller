package com.jianglibo.vaadin.dashboard.sshrunner;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.jianglibo.vaadin.dashboard.config.ApplicationConfig;
import com.jianglibo.vaadin.dashboard.domain.BoxHistory;
import com.jianglibo.vaadin.dashboard.ssh.JschSession;
import com.jianglibo.vaadin.dashboard.taskrunner.OneThreadTaskDesc;
import com.jianglibo.vaadin.dashboard.util.ThrowableUtil;
import com.jianglibo.vaadin.dashboard.vo.FileToUploadVo;

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

	public void download(JschSession jsession,  OneThreadTaskDesc taskDesc, String remoteFilePath, String localFilePath) {

		Path dstFile = applicationConfig.getUploadDstPath().resolve(localFilePath);
		
//		ChannelSftp sftp = null;
//		try {
//			sftp = jsession.getSftpCh();
//			putOneFile(sftp, fvo);
//		} catch (Exception e) {
//			bh.appendLogAndSetFailure(ThrowableUtil.printToString(e));
//			bh.setSuccess(false);
//		} finally {
//			if (sftp != null) {
//				sftp.disconnect();
//			}
//		}
		
	}

	private void putOneFile(ChannelSftp sftp, String remoteFile, String localFile) throws JSchException, SftpException {
		sftp.connect();
		sftp.get(remoteFile, localFile);
	}
}
