package com.jianglibo.vaadin.dashboard.sshrunner;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
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
import com.jianglibo.vaadin.dashboard.sshrunner.SshLs.LsResult;
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
@Component()
public class SshUploadRunner implements BaseRunner {

	@Autowired
	private ApplicationConfig applicationConfig;
	
	@Autowired
	private SshLs sshls;

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
			try {
				for(FileToUploadVo fvo : taskDesc.getSoftware().getFileToUploadVos()) {
					ChannelSftp sftp = null;
					try {
						sftp = jsession.getSftpCh();
						putOneFile(jsession, sftp, fvo);
					} catch (Exception e) {
						bh.appendLogAndSetFailure(ThrowableUtil.printToString(e));
						bh.setSuccess(false);
					} finally {
						if (sftp != null) {
							try {
								sftp.disconnect();
							} catch (Exception e) {
							}
						}
					}
				}
			} catch (Exception e) {
				bh.appendLogAndSetFailure(ThrowableUtil.printToString(e));
			}
		}
	}

	protected boolean putOneFile(JschSession jsession, ChannelSftp sftp, FileToUploadVo fvo) throws JSchException, SftpException {
		Path fileToUploadPath = applicationConfig.getLocalFolderPath().resolve(fvo.getRelative()).toAbsolutePath(); 
		String fileToUpload = fileToUploadPath.toString().replace("\\\\", "/");
		String targetFile = applicationConfig.getRemoteFolder() + fvo.getRelative().replaceAll("\\\\", "/");
		List<LsResult> lsrs = sshls.ls(jsession, targetFile);
		Optional<LsResult> lsr = lsrs.stream().filter(one -> fileToUploadPath.getFileName().toString().equals(one.getFilename())).findAny();
		if (lsr.isPresent()) {
			if (lsr.get().getLength() == fileToUploadPath.toFile().length()) {
				return false;
			}
		}
		sftp.connect();
		int idx = targetFile.lastIndexOf('/');
		String targetFolder = targetFile.substring(0, idx);
		
		try {
			sftp.mkdir(targetFolder);
		} catch (Exception e) {
			// will throw exception if exists.
			e.printStackTrace();
		}

		sftp.put(fileToUpload, targetFile, ChannelSftp.OVERWRITE);
		return true;
	}
}
