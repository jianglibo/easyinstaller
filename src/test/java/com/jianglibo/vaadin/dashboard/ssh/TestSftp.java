package com.jianglibo.vaadin.dashboard.ssh;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;

import org.junit.Test;

import com.google.common.io.ByteSource;
import com.google.common.io.ByteStreams;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.jianglibo.vaadin.dashborad.Tutil;

public class TestSftp extends Tutil {

	@Test
	public void t() throws JSchException, SftpException, IOException {
		JschSession jsb = getJschSession();
		ChannelSftp sftpCh = jsb.getSftpCh();
		sftpCh.connect();
		InputStream is = ByteSource.wrap("abc".getBytes()).openStream();
		sftpCh.put(is, "/root/ttt", ChannelSftp.OVERWRITE);
		sftpCh.put("fixtures/filetoput.txt", "\\root\\filetoput.txt");
		sftpCh.quit();

		sftpCh = jsb.getSftpCh();
		sftpCh.connect();
		String s = new String(ByteStreams.toByteArray(sftpCh.get("/root/ttt")));
		assertThat(s, equalTo("abc"));
		s = new String(ByteStreams.toByteArray(sftpCh.get("/root/filetoput.txt")));
		assertThat(s, equalTo("hello"));
		sftpCh.quit();
	}

	@Test
	public void tdir() throws JSchException, SftpException {
		JschSession jsb = getJschSession();
		ChannelSftp sftpCh = jsb.getSftpCh();
		SftpUtil.putDir(Paths.get("fixtures"), "/root/fixtures", sftpCh);
		
		sftpCh = jsb.getSftpCh();
		sftpCh.connect();
		Vector<LsEntry> files = sftpCh.ls("/root/fixtures");
		files.forEach(f -> {
			printme(f.toString());
		});
	}

	@Test
	public void tmkdir() throws JSchException, SftpException {
		JschSession jsb = getJschSession();
		ChannelSftp sftpCh = jsb.getSftpCh();
		String dir = "/root/akak";
		sftpCh.connect();
		sftpCh.rmdir(dir);
		sftpCh.mkdir(dir);
		try {
			sftpCh.mkdir(dir);
		} catch (Exception e) {
		}
		sftpCh.quit();
	}

	@Test
	public void tpath() {
		Path nixroot = Paths.get("/");
		assertThat(nixroot.toString(), equalTo("\\"));

		nixroot = Paths.get("/root");
		assertThat(nixroot.toString(), equalTo("\\root"));

		nixroot = nixroot.resolve("abc");
		assertThat(nixroot.toString(), equalTo("\\root\\abc"));


	}
}
