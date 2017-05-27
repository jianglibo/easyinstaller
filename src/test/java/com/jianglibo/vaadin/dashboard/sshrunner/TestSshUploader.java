package com.jianglibo.vaadin.dashboard.sshrunner;


import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.jianglibo.vaadin.dashboard.ssh.SshBaset;
import com.jianglibo.vaadin.dashboard.vo.FileToUploadVo;
import com.jianglibo.vaadin.dashboard.sshrunner.SshUploadRunner;

public class TestSshUploader extends SshBaset {
	
	
	@Autowired
	private SshUploadRunner uploadRunner;
	
	private String phoenixUrl = "http://mirrors.hust.edu.cn/apache/phoenix/apache-phoenix-4.10.0-HBase-1.2/bin/apache-phoenix-4.10.0-HBase-1.2-bin.tar.gz";
	
	@Test
	public void t() throws JSchException, IOException, SftpException {
		FileToUploadVo fvo = new FileToUploadVo(phoenixUrl);
		boolean notUpload = uploadRunner.putOneFile(getJschSession(), getJschSession().getSftpCh(), fvo);
		assertFalse(notUpload);
	}

}
