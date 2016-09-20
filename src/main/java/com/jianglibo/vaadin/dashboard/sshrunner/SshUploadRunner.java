package com.jianglibo.vaadin.dashboard.sshrunner;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.jcraft.jsch.ChannelSftp;
import com.jianglibo.vaadin.dashboard.config.ApplicationConfig;
import com.jianglibo.vaadin.dashboard.domain.BoxHistory;
import com.jianglibo.vaadin.dashboard.ssh.JschSession;
import com.jianglibo.vaadin.dashboard.taskrunner.OneThreadTaskDesc;

/**
 * ApplicationConfig has a configurable remoteFolder property. Files upload to
 * that folder.
 * 
 * @author jianglibo@gmail.com
 *
 */
@Component()
public class SshUploadRunner implements BaseRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(SshUploadRunner.class);

	@Autowired
	private ApplicationConfig applicationConfig;

	@Override
	public void run(JschSession jsession,  OneThreadTaskDesc taskDesc) {

		// confirm all files exists in local.
		List<String> noneExistsFiles = Lists.newArrayList();
		for(String fileToUpload : taskDesc.getSoftware().getFilesToUpload()) {
			Path sourceFile = applicationConfig.getLocalFolderPath().resolve(fileToUpload); 
			if (!Files.exists(sourceFile)) {
				noneExistsFiles.add(sourceFile.toAbsolutePath().toString());
			}
		}
		BoxHistory bh = taskDesc.getBoxHistory();
		if (noneExistsFiles.size() > 0) {
			String log = noneExistsFiles.stream().reduce("", (result, l) -> result + l);
			bh.appendLog(log);
		} else {
			ChannelSftp sftp = null;
			try {
				sftp = jsession.getSftpCh();
				try {
					for(String fileToUpload : taskDesc.getSoftware().getFilesToUpload()) {
						String targetFile = applicationConfig.getRemoteFolder() + fileToUpload.replaceAll("\\\\", "/");
						sftp.put(fileToUpload, targetFile, ChannelSftp.OVERWRITE);
					}
				} catch (Exception e) {
					bh.appendLog(e.getMessage());
				}
			} catch (Exception e) {
				bh.appendLog(e.getMessage());
				bh.setSuccess(false);
			} finally {
				if (sftp != null) {
					sftp.disconnect();
				}
			}
		}
	}
}
