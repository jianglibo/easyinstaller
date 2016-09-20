package com.jianglibo.vaadin.dashboard.sshrunner;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.jianglibo.vaadin.dashboard.config.ApplicationConfig;
import com.jianglibo.vaadin.dashboard.domain.BoxHistory;
import com.jianglibo.vaadin.dashboard.ssh.JschSession;
import com.jianglibo.vaadin.dashboard.taskrunner.OneThreadTaskDesc;

/**
 * Beside code this runner will create 4 files total. One is code file, other
 * three are clusterInfo, selfInfo, customInfo file. As a convention, code file
 * name is a uuid, others are uuid_clusterInfo, uuid_selfInfo, uuid_customInfo
 * 
 * Uuid as a parameter to code. For example, bash(tcl|perl|python)
 * 123e4567-e89b-12d3-a456-426655440000 -self
 * /root/easyinstaller/123e4567-e89b-12d3-a456-426655440000 So you can get other
 * three files
 * /root/easyinstaller/123e4567-e89b-12d3-a456-426655440000_clusterInfo etc.
 * 
 * What about uploaded files? You had known the file names. They are put in
 * configuration item "remoteFolder".
 * 
 * @author jianglibo@gmail.com
 *
 */
@Component
public class SshExecRunner implements BaseRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(SshExecRunner.class);

	@Autowired
	private ApplicationConfig applicationConfig;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private ObjectMapper ymlObjectMapper;
	
	@Autowired
	private ObjectMapper xmlObjectMapper;

	@Override
	public void run(JschSession jsession, OneThreadTaskDesc taskDesc) {
		copyCodeToServerAndRun(jsession, taskDesc);
	}

	private void copyCodeToServerAndRun(JschSession jsession, OneThreadTaskDesc taskDesc) {
		String uuid = UUID.randomUUID().toString();
		uplocadEnv(jsession, taskDesc, uuid);
		uploadCode(jsession, taskDesc, uuid);
	}

	private void uplocadEnv(JschSession jsession, OneThreadTaskDesc taskDesc, String uuid) {
		EvnForCodeExec env = new EvnForCodeExec(taskDesc, applicationConfig.getRemoteFolder());
		String envstr = null;
		try {
			switch (taskDesc.getSoftware().getPreferredFormat()) {
			case "XML":
				envstr = xmlObjectMapper.writeValueAsString(env);
				break;
			case "JSON":
				envstr = objectMapper.writeValueAsString(env);
				break;
			case "YAML":
				envstr = ymlObjectMapper.writeValueAsString(env);
				break;
			default:
				LOGGER.error("unsupported format: {}", taskDesc.getSoftware().getPreferredFormat());
				taskDesc.getBoxHistory().appendLog("unsupported format: " + taskDesc.getSoftware().getPreferredFormat()) ;
				return;
			}
			String targetFile = applicationConfig.getRemoteFolder() + uuid + "_env";
			putStream(taskDesc.getBoxHistory(), jsession, targetFile, envstr);
		} catch (Exception e) {
			taskDesc.getBoxHistory().appendLog(e.getMessage());
		}
	}

	private void putStream(BoxHistory bh, JschSession jsession, String targetFile, String content) {
		ChannelSftp sftp = null;
		try {
			sftp = jsession.getSftpCh();
			try {
				OutputStream os = sftp.put(targetFile, ChannelSftp.OVERWRITE);
				os.write(content.getBytes(Charsets.UTF_8));
				os.flush();
				os.close();
			} catch (SftpException | IOException e) {
				bh.appendLog(e.getMessage());
			}
		} catch (JSchException e) {
			bh.appendLog(e.getMessage());
		} finally {
			if (sftp != null) {
				sftp.disconnect();
			}
		}
	}

	private void uploadCode(JschSession jsession, OneThreadTaskDesc taskDesc, String uuid) {
		String targetFile = applicationConfig.getRemoteFolder() + uuid;
		putStream(taskDesc.getBoxHistory(), jsession, targetFile, taskDesc.getSoftware().getCodeToExecute());
	}

}
