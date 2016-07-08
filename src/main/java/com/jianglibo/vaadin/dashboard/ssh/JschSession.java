package com.jianglibo.vaadin.dashboard.ssh;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import com.google.common.base.Strings;
import com.google.common.io.ByteSink;
import com.google.common.io.ByteStreams;
import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jianglibo.vaadin.dashboard.ssh.JschExecuteResult.ResultType;

public class JschSession {
	
	private Session session;
	
	private JschSession(Session session) {
		this.setSession(session);
	}
	
	public JschExecuteResult exec(String cmd) {
		return execs(cmd).get(0);
	}
	
	public ChannelSftp getSftpCh() throws JSchException {
		return (ChannelSftp) getSession().openChannel("sftp");
	}
	
	public List<JschExecuteResult> execs(String...cmds) {
		List<JschExecuteResult> results = Lists.newArrayList();
		
		for(String cmd: cmds) {
			try {
				ChannelExec chExec = (ChannelExec) getSession().openChannel("exec");
				chExec.setCommand(cmd);
				chExec.setInputStream(null);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				chExec.setErrStream(baos);
				chExec.connect();
				String out = new String(ByteStreams.toByteArray(chExec.getInputStream()));
				String err = baos.toString();
				int ext = chExec.getExitStatus();
				if (ext == 0) {
					results.add(new BaseJschExecuteResult(Strings.isNullOrEmpty(out) ? err : out, 0));
				} else {
					results.add(new BaseJschExecuteResult(Strings.isNullOrEmpty(err) ? out : err, ext));
				}
				chExec.disconnect();
			} catch (JSchException | IOException e) {
				results.add(new BaseJschExecuteResult(e.getMessage(), Integer.MAX_VALUE, ResultType.EXP));
			}
		}
		return results;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public static class JschSessionBuilder {
		private String user;
		private String host;
		private int port = 22;
		
		private String keyFile;
		
		private String knownHosts;
		
		public JschSessionBuilder setUser(String user) {
			this.user = user;
			return this;
		}

		public JschSessionBuilder  setHost(String host) {
			this.host = host;
			return this;
		}

		public JschSessionBuilder  setPort(int port) {
			this.port = port;
			return this;
		}
		
		public JschSessionBuilder  setKeyFile(String keyFile) {
			this.keyFile = keyFile;
			return this;
		}
		
		public JschSessionBuilder  setKnownHosts(String knownHosts) {
			this.knownHosts = knownHosts;
			return this;
		}

		public JschSession build() throws JSchException {
			JSch jsch = new JSch();
			jsch.addIdentity(keyFile);
			jsch.setKnownHosts(knownHosts);
			Session session = jsch.getSession(user, host, port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			return new JschSession(session);
		}
	}
}
