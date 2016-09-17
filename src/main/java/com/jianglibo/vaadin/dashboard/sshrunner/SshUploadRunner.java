package com.jianglibo.vaadin.dashboard.sshrunner;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.jcraft.jsch.ChannelSftp;
import com.jianglibo.vaadin.dashboard.annotation.Runner;
import com.jianglibo.vaadin.dashboard.config.ApplicationConfig;
import com.jianglibo.vaadin.dashboard.domain.JschExecuteResult;
import com.jianglibo.vaadin.dashboard.domain.StepRun;
import com.jianglibo.vaadin.dashboard.repositories.JschExecuteResultRepository;
import com.jianglibo.vaadin.dashboard.repositories.StepRunRepository;
import com.jianglibo.vaadin.dashboard.ssh.JschSession;
import com.jianglibo.vaadin.dashboard.ssh.StepConfig;

/**
 * ApplicationConfig has a configurable remoteFolder property. Files upload to
 * that folder.
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
		UploadDescription udesc = StepConfig.getUploadDescription(stepRun);

		JschExecuteResult jschExecuteResult = null;
		if (udesc.getFrom().isEmpty()) {
			String msg = "step {}, SshUploadRunner need 'from' configuration.";
			LOGGER.error(msg, stepRun.toString());
			jschExecuteResult = new JschExecuteResult(msg, msg, 1);
		} else {
			String toFolder;
			if (udesc.getTo().isEmpty()) {
				toFolder = applicationConfig.getRemoteFolder();
			} else {
				toFolder = udesc.getTo();
			}
			List<FromToPair> fromToPairs = udesc.getFrom().stream().map(f -> new FromToPair(f, toFolder))
					.collect(Collectors.toList());

			ChannelSftp sftp = null;
			try {
				sftp = jsession.getSftpCh();
				try {
					for (FromToPair pair : fromToPairs) {
						sftp.put(pair.getFrom(), pair.getTo(), ChannelSftp.OVERWRITE);
					}
				} catch (Exception e) {
					e.printStackTrace();
					jschExecuteResult = new JschExecuteResult(e.getMessage(), e.getMessage(), 1);
				}
			} catch (Exception e) {
				e.printStackTrace();
				jschExecuteResult = new JschExecuteResult(e.getMessage(), e.getMessage(), 1);
			} finally {
				if (sftp != null) {
					sftp.disconnect();
				}
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

	public static class UploadDescription {
		private List<String> from;
		private String to;

		public List<String> getFrom() {
			if (from == null) {
				return Lists.newArrayList();
			} else {
				return from;
			}
		}

		public void setFrom(List<String> from) {
			this.from = from;
		}

		public String getTo() {
			if (to == null) {
				return "";
			} else {
				return to;
			}
		}

		public void setTo(String to) {
			this.to = to;
		}
	}

	private class FromToPair {
		private String from;
		private String to;

		public FromToPair(String fromItem, String toFolder) {
			Path fp = applicationConfig.getLocalFolderPath().resolve(fromItem); 
			setFrom(fp.toFile().getAbsolutePath());
			String t = fromItem.replaceAll("\\\\", "/");
			if (!t.endsWith("/")) {
				t = t + "/";
			}
			setTo(t + fp.getFileName().toString());
		}

		public String getFrom() {
			return from;
		}

		public void setFrom(String from) {
			this.from = from;
		}

		public String getTo() {
			return to;
		}

		public void setTo(String to) {
			this.to = to;
		}
	}

}
