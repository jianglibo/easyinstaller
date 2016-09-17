package com.jianglibo.vaadin.dashboard.sshrunner;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Charsets;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.jianglibo.vaadin.dashboard.annotation.Runner;
import com.jianglibo.vaadin.dashboard.config.ApplicationConfig;
import com.jianglibo.vaadin.dashboard.domain.JschExecuteResult;
import com.jianglibo.vaadin.dashboard.domain.StepRun;
import com.jianglibo.vaadin.dashboard.repositories.JschExecuteResultRepository;
import com.jianglibo.vaadin.dashboard.repositories.StepRunRepository;
import com.jianglibo.vaadin.dashboard.ssh.CodeSubstitudeUtil;
import com.jianglibo.vaadin.dashboard.ssh.JschSession;

/**
 * Beside code this runner will create 4 files total. One is code file, other three are clusterInfo, selfInfo, customInfo file.
 * As a convention, code file name is a uuid, others are uuid_clusterInfo, uuid_selfInfo, uuid_customInfo 
 * 
 * Uuid as a parameter to code. For example, bash(tcl|perl|python) 123e4567-e89b-12d3-a456-426655440000 -self /root/easyinstaller/123e4567-e89b-12d3-a456-426655440000
 * So you can get other three files /root/easyinstaller/123e4567-e89b-12d3-a456-426655440000_clusterInfo etc.
 * 
 * @author jianglibo@gmail.com
 *
 */
@Runner(SshExecRunner.UNIQUE_NAME)
@Component(SshExecRunner.UNIQUE_NAME)
public class SshExecRunner implements BaseRunner {
	
	public static final String UNIQUE_NAME = "SshExecRunner";
	
	@Autowired
	private StepRunRepository stepRunRepository;
	
	@Autowired
	private ApplicationConfig applicationConfig;
	
	private JschExecuteResultRepository jschExecuteResultRepository;

	@Override
	public JschExecuteResult run(JschSession jsession, StepRun stepRun) {
		return copyCodeToServerAndRun(jsession, stepRun);
	}
	
	private JschExecuteResult copyCodeToServerAndRun(JschSession jsession, StepRun stepRun) {
		String code = CodeSubstitudeUtil.process(stepRun);
		ChannelSftp sftp = null;
		String codeFileToRun = applicationConfig.getRemoteFolder() + stepRun.getUniqueFileName();
		try {
			sftp = jsession.getSftpCh();
			try {
				OutputStream os = sftp.put(codeFileToRun, ChannelSftp.OVERWRITE);
				os.write(code.getBytes(Charsets.UTF_8));
				os.flush();
				os.close();
			} catch (SftpException | IOException e) {
				e.printStackTrace();
			}
		} catch (JSchException e) {
			e.printStackTrace();
		} finally {
			if (sftp != null) {
				sftp.disconnect();
			}
		}
		String cmd = stepRun.getStepDefine().getRunner() + " " + codeFileToRun + "-self " + codeFileToRun;
		JschExecuteResult jer = jsession.exec(cmd);
		JschExecuteResult oldJer = stepRun.getResult();
		stepRun.setResult(jer);
		if (oldJer != null) {
			jschExecuteResultRepository.delete(oldJer);
		}
		stepRunRepository.save(stepRun);
		return jer;
	}

	@Override
	public String uniqueName() {
		return UNIQUE_NAME;
	}
}
