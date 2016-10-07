package com.jianglibo.vaadin.dashboard.sshrunner;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

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
import com.jianglibo.vaadin.dashboard.vo.FileToUploadVo;

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
		
		Set<FileToUploadVo> ftuvos = taskDesc.getSoftware().getFileToUploadVos();
		
		ftuvos.forEach(fvo -> {
			Path sourceFile = applicationConfig.getLocalFolderPath().resolve(fvo.getRelative()); 
			if (!Files.exists(sourceFile)) {
				noneExistsFiles.add(sourceFile.toAbsolutePath().toString());
			}
		});
		
		BoxHistory bh = taskDesc.getBoxHistory();
		if (noneExistsFiles.size() > 0) {
			String log = noneExistsFiles.stream().reduce("", (result, l) -> result + l);
			bh.appendLogAndSetFailure(log);
		} else {
			ChannelSftp sftp = null;
			try {
				sftp = jsession.getSftpCh();
				try {
					for(FileToUploadVo fvo : taskDesc.getSoftware().getFileToUploadVos()) {
						String fileToUpload = applicationConfig.getLocalFolderPath().resolve(fvo.getRelative()).toAbsolutePath().toString().replace("\\\\", "/");
						String targetFile = applicationConfig.getRemoteFolder() + fvo.getRelative().replaceAll("\\\\", "/");
						
						sftp.connect();
						int idx = targetFile.lastIndexOf('/');
						String targetFolder = targetFile.substring(0, idx);
						
						try {
							sftp.mkdir(targetFolder);
						} catch (Exception e) {
							e.printStackTrace();
						}

						sftp.put(fileToUpload, targetFile, ChannelSftp.OVERWRITE);
					}
				} catch (Exception e) {
					bh.appendLogAndSetFailure(e.getMessage());
				}
			} catch (Exception e) {
				bh.appendLogAndSetFailure(e.getMessage());
				bh.setSuccess(false);
			} finally {
				if (sftp != null) {
					sftp.disconnect();
				}
			}
		}
	}
}
