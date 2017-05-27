package com.jianglibo.vaadin.dashboard.ssh;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jcraft.jsch.JSchException;
import com.jianglibo.vaadin.dashboard.ssh.JschSession;
import com.jianglibo.vaadin.dashboard.sshrunner.SshLs;
import com.jianglibo.vaadin.dashboard.sshrunner.SshLs.LsResult;

public class TestSshLs extends SshBaset {
	
	
	@Autowired
	private SshLs sshls;

	@Test
	public void t() throws JSchException, IOException {
		JschSession jsb =  getJschSession();
		List<LsResult> lsrs = sshls.ls(jsb, "~");
		assertTrue(lsrs.size() > 0);
		lsrs.forEach(lr -> {
			printme(lr.toString());
		});
	}

}
