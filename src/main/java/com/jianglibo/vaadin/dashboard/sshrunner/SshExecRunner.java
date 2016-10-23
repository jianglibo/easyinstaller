package com.jianglibo.vaadin.dashboard.sshrunner;

import java.io.IOException;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Charsets;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.jianglibo.vaadin.dashboard.config.ApplicationConfig;
import com.jianglibo.vaadin.dashboard.domain.BoxHistory;
import com.jianglibo.vaadin.dashboard.service.AppObjectMappers;
import com.jianglibo.vaadin.dashboard.ssh.JschSession;
import com.jianglibo.vaadin.dashboard.taskrunner.OneThreadTaskDesc;
import com.jianglibo.vaadin.dashboard.util.SoftwareUtil;
import com.jianglibo.vaadin.dashboard.vo.JschExecuteResult;

/**
 * Beside code this runner will create 2 files total. One is code file, other is envfile.
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
	private AppObjectMappers appObjectmappers;
	
	@Autowired
	private SoftwareUtil softwareUtil;

	@Override
	public void run(JschSession jsession, OneThreadTaskDesc taskDesc) {
		copyCodeToServerAndRun(jsession, taskDesc);
	}

	private void copyCodeToServerAndRun(JschSession jsession, OneThreadTaskDesc taskDesc) {
		String codeToExec = softwareUtil.getParsedCodeToExecute(taskDesc.getSoftware());
		String codeFileName = taskDesc.getSoftware().getCodeFileName(codeToExec);
		String envFile, codeFile, tpl, cmd;
		
		envFile = uplocadEnv(jsession, taskDesc, codeFileName);
		if (taskDesc.getBoxHistory().isSuccess()) {
			codeFile = uploadCode(jsession, taskDesc,codeToExec, codeFileName);
			if (taskDesc.getBoxHistory().isSuccess()) {
				String runner = taskDesc.getSoftware().getRunner();
				if (runner.contains("{code}") || runner.contains("{envfile}") || runner.contains("{action}")) {
					cmd = runner.replace("{code}", codeFile).replace("{envfile}", envFile).replace("{action}", taskDesc.getAction());
				} else {
					tpl = "%s %s -envfile %s -action %s";
					cmd = String.format(tpl, runner , codeFile, envFile, taskDesc.getAction());
				}
				JschExecuteResult jer = jsession.exec(cmd);
				
				taskDesc.getBoxHistory().appendLog(jer.getOut());
				taskDesc.getBoxHistory().appendLog(jer.getErr());
				// must return explicit @@success@@ to indicate success. 
				if (jer.getOut() != null && jer.getOut().contains("@@success@@")) {
					taskDesc.getBoxHistory().setSuccess(true);
				} else {
					taskDesc.getBoxHistory().setSuccess(false);
				}
			}
		}
	}

	private String uplocadEnv(JschSession jsession, OneThreadTaskDesc taskDesc, String uuid) {
		EnvForCodeExec env = new EnvForCodeExec.EnvForCodeExecBuilder(appObjectmappers, taskDesc, applicationConfig.getRemoteFolder()).build();
		String envstr = null;
		try {
			switch (taskDesc.getSoftware().getPreferredFormat()) {
			case "XML":
				envstr = appObjectmappers.getXmlObjectMapper().writeValueAsString(env);
				break;
			case "JSON":
				envstr = appObjectmappers.getObjectMapperNoIdent().writeValueAsString(env);
				break;
			case "YAML":
				envstr = appObjectmappers.getYmlObjectMapper().writeValueAsString(env);
				break;
			default:
				LOGGER.error("unsupported format: {}", taskDesc.getSoftware().getPreferredFormat());
				taskDesc.getBoxHistory().appendLogAndSetFailure("unsupported format: " + taskDesc.getSoftware().getPreferredFormat()) ;
				return null;
			}
			String targetFile = applicationConfig.getRemoteFolder() + uuid + ".env";
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
			sftp.connect();
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

	private String uploadCode(JschSession jsession, OneThreadTaskDesc taskDesc,String codeToExec, String codeFileName) {
		String targetFile = applicationConfig.getRemoteFolder() + codeFileName;
		return putStream(taskDesc.getBoxHistory(), jsession, targetFile, codeToExec);
	}

}
