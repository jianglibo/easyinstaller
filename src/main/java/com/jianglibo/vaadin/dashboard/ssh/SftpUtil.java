package com.jianglibo.vaadin.dashboard.ssh;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

public class SftpUtil {

	public static void putDir(Path start, String dst, ChannelSftp sftpCh) throws JSchException, SftpException {
		putDir(start, dst, sftpCh, false);
	}

	public static void putDir(Path start, String dst, ChannelSftp sftpCh, boolean includeSourceDir)
			throws JSchException, SftpException {
		if (!sftpCh.isConnected()) {
			sftpCh.connect();
		}
		Path srcName = start.getFileName();
		dst = dst.replace('\\', '/');
		if (!dst.endsWith("/")) {
			dst = dst + "/";
		}
		final String dstf = dst;

		int pos = 0;
		do {
			String seg = dstf.substring(0, pos);
			if (!seg.isEmpty()) {
				try {
					sftpCh.mkdir(seg);
				} catch (Exception e) {
				}
			}
		} while ((pos = dstf.indexOf('/', pos + 1)) != -1);

		try (Stream<Path> fpaths = Files.walk(start)) {
			fpaths.forEach(one -> {
				Path srcla = start.relativize(one);
				String oneDst = null;

				if (includeSourceDir) {
					oneDst = dstf + '/' + srcName + "/" + srcla.toString().replace('\\', '/');
				} else {
					oneDst = dstf + '/' + srcla.toString().replace('\\', '/');
				}
				oneDst = oneDst.replace("//", "/");

				if (Files.isDirectory(one)) {
					try {
						sftpCh.mkdir(oneDst);
					} catch (Exception e) {
					}
				} else {
					try {
						sftpCh.put(one.toString(), oneDst);
					} catch (Exception e) {
					}
				}
			});
			sftpCh.quit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
