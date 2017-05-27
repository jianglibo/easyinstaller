package com.jianglibo.vaadin.dashboard.ssh;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.jcraft.jsch.JSchException;
import com.jianglibo.vaadin.dashboard.ssh.JschSession;
import com.jianglibo.vaadin.dashboard.vo.JschExecuteResult;

public class TestSessionBuilder extends SshBaset {
	
	@Test
	public void placeholder() {
		assertTrue(true);
	}

	@Test
	public void t() throws JSchException, IOException {
		JschSession jsb =  getJschSession();

		JschExecuteResult result = jsb.exec("ls -l");
		printme(result.toString());
		assertThat(result.getExitValue(), equalTo(0));
		assertThat(result.getErr(), equalTo(""));
		
		result = jsb.exec("lxxs");
		assertThat(result.getExitValue(), equalTo(1));
		assertTrue(result.getErr().contains("command not found"));
	}
	
	@Test
	public void twhich() throws JSchException {
		JschSession jsb =  getJschSession();

		JschExecuteResult result = jsb.exec("which java");
		printme(result.toString());
		assertThat(result.getExitValue(), equalTo(0));
		
		result = jsb.exec("which alternatives");
		printme(result.toString());
		assertThat(result.getErr(), equalTo(0));
		
		result = jsb.exec("which java1");
		printme(result.toString());
		
		result = jsb.exec("alternatives --list");
		printme(result.toString());
		
	}

}
