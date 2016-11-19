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
import com.jianglibo.vaadin.dashboard.util.ThrowableUtil;
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
		String codeFileNameAtRemote = applicationConfig.getRemoteFolder() + taskDesc.getSoftware().getCodeFileName(codeToExec);
		String envFileNameAtRemote = codeFileNameAtRemote + ".env";
		
		String tpl, cmd;
		
		uplocadEnv(jsession, taskDesc, envFileNameAtRemote);
		
		if (taskDesc.getBoxHistory().isSuccess()) {
			uploadCode(jsession, taskDesc,codeToExec, codeFileNameAtRemote);
			if (taskDesc.getBoxHistory().isSuccess()) {
				String runner = taskDesc.getSoftware().getRunner();
				if (runner.contains("{code}") || runner.contains("{envfile}") || runner.contains("{action}")) {
					cmd = runner.replace("{code}", codeFileNameAtRemote).replace("{envfile}", envFileNameAtRemote).replace("{action}", taskDesc.getAction());
				} else {
					tpl = "%s %s -envfile %s -action %s";
					cmd = String.format(tpl, runner , codeFileNameAtRemote, envFileNameAtRemote, taskDesc.getAction());
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

	private void uplocadEnv(JschSession jsession, OneThreadTaskDesc taskDesc, String envFileNameAtRemote) {
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
			}
			putStream(taskDesc.getBoxHistory(), jsession, envFileNameAtRemote, envstr);
		} catch (Exception e) {
			taskDesc.getBoxHistory().appendLogAndSetFailure(ThrowableUtil.printToString(e));
		}
	}
	
	private void createRemoteFolder(BoxHistory bh,JschSession jsession, String targetFile, String content) {
		ChannelSftp sftp = null;
		try {
			sftp = jsession.getSftpCh();
			sftp.connect();
			try {
				sftp.mkdir(applicationConfig.getRemoteFolder());
				putStream(bh, jsession, targetFile, content, true);
			} catch (SftpException e) {
			}
		} catch (JSchException e) {
		} finally {
			if (sftp != null) {
				sftp.disconnect();
			}
		}
	}
	
	private void putStream(BoxHistory bh, JschSession jsession, String targetFile, String content) {
		putStream(bh, jsession, targetFile, content,false);
	}
	private void putStream(BoxHistory bh, JschSession jsession, String targetFile, String content, boolean retry) {
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
				// /easyinstaller folder on target server doesn't exists.
				if (!retry) {
					if (e instanceof SftpException) {
						if (((SftpException)e).id == 2) {
							if (targetFile.startsWith(applicationConfig.getRemoteFolder())) {
								createRemoteFolder(bh,jsession,targetFile, content);
							}
						}
					}
				} else {
					bh.appendLogAndSetFailure(String.format("boxhistory: %s, targetFile: %s", bh.getDisplayName(), targetFile));
					bh.appendLogAndSetFailure(ThrowableUtil.printToString(e));
				}
			}
		} catch (JSchException e) {
			bh.appendLogAndSetFailure(ThrowableUtil.printToString(e));
		} finally {
			if (sftp != null) {
				sftp.disconnect();
			}
		}
	}

	private void uploadCode(JschSession jsession, OneThreadTaskDesc taskDesc,String codeToExec, String codeFileNameAtRemote) {
		putStream(taskDesc.getBoxHistory(), jsession, codeFileNameAtRemote, codeToExec);
	}

}
