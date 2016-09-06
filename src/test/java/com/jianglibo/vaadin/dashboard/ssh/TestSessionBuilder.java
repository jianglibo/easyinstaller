package com.jianglibo.vaadin.dashboard.ssh;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.jcraft.jsch.JSchException;
import com.jianglibo.vaadin.dashboard.domain.JschExecuteResult;
import com.jianglibo.vaadin.dashboard.ssh.JschSession;

public class TestSessionBuilder extends SshBaset {

//	@Test
//	public void t() throws JSchException, IOException {
//		JschSession jsb =  getJschSession();
//
//		JschExecuteResult result = jsb.exec("ls");
//		assertThat(result.getExitStatus(), equalTo(0));
//		assertThat(result.getState(), equalTo(ResultType.ZERO));
//		
//		result = jsb.exec("lxxs");
//		assertThat(result.getState(), equalTo(ResultType.NONE_ZERO));
//		assertTrue(result.getCmdOut().contains("command not found"));
//	}
//	
//	@Test
//	public void twhich() throws JSchException {
//		JschSession jsb =  getJschSession();
//
//		JschExecuteResult result = jsb.exec("which java");
//		assertThat(result.getState(), equalTo(ResultType.ZERO));
//		
//		result = jsb.exec("which alternatives");
//		assertThat(result.getState(), equalTo(ResultType.ZERO));
//		
//		result = jsb.exec("which java1");
//		printme(result.toString());
//		
//		result = jsb.exec("alternatives --list");
//		printme(result.toString());
//		
//	}

}
