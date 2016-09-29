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
import com.jianglibo.vaadin.dashboard.vo.JschExecuteResult;

/**
 * Beside code this runner will create 2 files total. One is code file, other
 * is envfile.
 * 
 * Uuid as a parameter to code. For example, bash(tcl|perl|python)
 * 123e4567-e89b-12d3-a456-426655440000 --envfile /root/easyinstaller/123e4567-e89b-12d3-a456-426655440000_env install. 
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
		String envFile, codeFile, tpl;
		
		envFile = uplocadEnv(jsession, taskDesc, uuid);
		if (taskDesc.getBoxHistory().isSuccess()) {
			codeFile = uploadCode(jsession, taskDesc, uuid);
			if (taskDesc.getBoxHistory().isSuccess()) {
				tpl = "%s %s --envfile %s %s";
				JschExecuteResult jer = jsession.exec(String.format(tpl, taskDesc.getSoftware().getRunner(), codeFile, envFile, taskDesc.getAction()));
				
				if (jer.getExitValue() != 0) { //success
					taskDesc.getBoxHistory().appendLogAndSetFailure(jer.getOut());
					taskDesc.getBoxHistory().appendLogAndSetFailure(jer.getErr());
				}
			}
		}
	}

	private String uplocadEnv(JschSession jsession, OneThreadTaskDesc taskDesc, String uuid) {
		EnvForCodeExec env = new EnvForCodeExec(taskDesc, applicationConfig.getRemoteFolder());
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
				taskDesc.getBoxHistory().appendLogAndSetFailure("unsupported format: " + taskDesc.getSoftware().getPreferredFormat()) ;
				return null;
			}
			String targetFile = applicationConfig.getRemoteFolder() + uuid + "_env";
			return putStream(taskDesc.getBoxHistory(), jsession, targetFile, envstr);
		} catch (Exception e) {
			taskDesc.getBoxHistory().appendLogAndSetFailure(e.getMessage());
		}
		return null;
	}

	private String putStream(BoxHistory bh, JschSession jsession, String targetFile, String content) {
		ChannelSftp sftp = null;
		try {
			sftp = jsession.getSftpCh();
			try {
				OutputStream os = sftp.put(targetFile, ChannelSftp.OVERWRITE);
				os.write(content.getBytes(Charsets.UTF_8));
				os.flush();
				os.close();
			} catch (SftpException | IOException e) {
				bh.appendLogAndSetFailure(e.getMessage());
			}
		} catch (JSchException e) {
			bh.appendLogAndSetFailure(e.getMessage());
		} finally {
			if (sftp != null) {
				sftp.disconnect();
			}
		}
		return targetFile;
	}

	private String uploadCode(JschSession jsession, OneThreadTaskDesc taskDesc, String uuid) {
		String targetFile = applicationConfig.getRemoteFolder() + uuid;
		return putStream(taskDesc.getBoxHistory(), jsession, targetFile, taskDesc.getSoftware().getCodeToExecute());
	}

}
