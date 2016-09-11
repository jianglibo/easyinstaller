package com.jianglibo.vaadin.dashboard.sshrunner;

import java.io.File;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.jianglibo.vaadin.dashboard.annotation.Runner;
import com.jianglibo.vaadin.dashboard.config.ApplicationConfig;
import com.jianglibo.vaadin.dashboard.domain.JschExecuteResult;
import com.jianglibo.vaadin.dashboard.domain.StepRun;
import com.jianglibo.vaadin.dashboard.repositories.JschExecuteResultRepository;
import com.jianglibo.vaadin.dashboard.repositories.StepRunRepository;
import com.jianglibo.vaadin.dashboard.ssh.JschSession;
import com.jianglibo.vaadin.dashboard.ssh.StepConfig;

/**
 * 
 * 
 * @author jianglibo@gmail.com
 *
 */
@Runner(SshUploadRunner.UNIQUE_NAME)
@Component(SshUploadRunner.UNIQUE_NAME)
public class SshUploadRunner implements BaseRunner {
	
	public static final String UNIQUE_NAME = "SshUploadRunner";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SshUploadRunner.class);
	
	@Autowired
	private StepRunRepository stepRunRepository;
	
	@Autowired
	private ApplicationConfig applicationConfig;
	
	private JschExecuteResultRepository jschExecuteResultRepository;

	@Override
	public JschExecuteResult run(JschSession jsession, StepRun stepRun) {
		Map<String, Object> context = StepConfig.createStepConfig(stepRun).getMap();
		String from = (String) context.getOrDefault("from", "");
		String to = (String) context.getOrDefault("to", "");
		JschExecuteResult jschExecuteResult = null;
		if (from.isEmpty()) {
			String msg = "step {}, SshUploadRunner need 'from' configuration.";
			LOGGER.error(msg, stepRun.toString());
			jschExecuteResult = new JschExecuteResult(msg, msg, 1);
		} else {
			File fromFile = new File(from);
			if (to.isEmpty()) {
				if (fromFile.isAbsolute()) {
					to = fromFile.getName();
				} else {
					to = from.replaceAll("\\\\", "/");
					fromFile = new File(applicationConfig.getRemoteFolder(), from);
				}
			}
			
			if (!fromFile.exists() || fromFile.isDirectory()) {
				to = applicationConfig.getRemoteFolder() + "pkg/" + to;
				
				ChannelSftp sftp = null;
				try {
					sftp = jsession.getSftpCh();
					try {
						sftp.put(fromFile.getAbsolutePath(), to, ChannelSftp.RESUME);
					} catch (SftpException e) {
						jschExecuteResult = new JschExecuteResult(e.getMessage(), e.getMessage(), 1);
					}
				} catch (JSchException e) {
					jschExecuteResult = new JschExecuteResult(e.getMessage(), e.getMessage(), 1);
				} finally {
					if (sftp != null) {
						sftp.disconnect();
					}
				}
			} else {
				jschExecuteResult = new JschExecuteResult("from is not a file.", "from is not a file.", 1);
			}
		}
		
		if (jschExecuteResult == null) {
			jschExecuteResult = new JschExecuteResult(null, null, 0);
		}

		JschExecuteResult oldJer = stepRun.getResult();
		stepRun.setResult(jschExecuteResult);
		if (oldJer != null) {
			jschExecuteResultRepository.delete(oldJer);
		}
		stepRunRepository.save(stepRun);
		return jschExecuteResult;
	}

	@Override
	public String uniqueName() {
		return UNIQUE_NAME;
	}

}
