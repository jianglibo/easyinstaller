package com.jianglibo.vaadin.dashboard.sshrunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.base.Joiner;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.jianglibo.vaadin.dashboard.domain.BoxHistory;
import com.jianglibo.vaadin.dashboard.ssh.JschSession;
import com.jianglibo.vaadin.dashboard.taskrunner.OneThreadTaskDesc;
import com.jianglibo.vaadin.dashboard.util.ThrowableUtil;

/**
 * ApplicationConfig has a configurable remoteFolder property. Files upload to
 * that folder.
 * 
 * @author jianglibo@gmail.com
 *
 */
@Component()
public class SshCopyToRemote implements BaseRunner {
	
	@Override
	public void run(JschSession jsession,  OneThreadTaskDesc taskDesc) {
		
		String fileToCopies = taskDesc.getTaskDesc().getRemainParameters();
		BoxHistory bh = taskDesc.getBoxHistory();
		
		if (fileToCopies == null || fileToCopies.trim().isEmpty()) {
			bh.appendLogAndSetFailure("no file to upload.");
		} else {
			String[] files = fileToCopies.split("\\r?\\n");
			if (files.length != 2) {
				bh.appendLogAndSetFailure("only one file can upload a time. First line is localfile path, second line is serverside path.");
			} else {
				Path localSrc = Paths.get(files[0]);
				String remoteTarget = files[1];
				if (!Files.exists(localSrc) || !localSrc.isAbsolute() || !remoteTarget.startsWith("/")) {
					bh.appendLogAndSetFailure("no file to upload. all pathes must be absolutely.");
				} else {
					ChannelSftp sftp = null;
					try {
						sftp = jsession.getSftpCh();
						putOneFile(sftp, localSrc, remoteTarget);
					} catch (Exception e) {
						bh.appendLogAndSetFailure(ThrowableUtil.printToString(e));
						bh.setSuccess(false);
					} finally {
						if (sftp != null) {
							sftp.disconnect();
						}
					}
				}
			}
		}
	}

	private void putOneFile(ChannelSftp sftp, Path localSrc, String remoteTarget) throws JSchException, SftpException, IOException {
		sftp.connect();
		int idx = remoteTarget.lastIndexOf('/');
		String targetFolder = remoteTarget.substring(0, idx);
		
		try {
			sftp.mkdir(targetFolder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<String> lines = Files.readAllLines(localSrc);
		String linesstr = Joiner.on("\n").join(lines);
		sftp.put(new ByteArrayInputStream(linesstr.getBytes()), remoteTarget,ChannelSftp.OVERWRITE);
	}
}
