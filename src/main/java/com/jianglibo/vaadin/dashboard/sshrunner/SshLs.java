package com.jianglibo.vaadin.dashboard.sshrunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jianglibo.vaadin.dashboard.ssh.JschSession;
import com.jianglibo.vaadin.dashboard.vo.JschExecuteResult;

/**
 * 
 * @author jianglibo@gmail.com
 *
 */
@Component
public class SshLs {

	private static final Logger LOGGER = LoggerFactory.getLogger(SshLs.class);
	
	// -rw-r--r--.  1 root root   20 2016-10-11 01:16:19.195012000 +0000 abc.ps1
	
	public List<LsResult> ls(JschSession jsession, String remotePath) {
		List<LsResult> rs = new ArrayList<>();
		JschExecuteResult result = jsession.exec("ls -l --full-time " + remotePath);
		if (result.getExitValue() == 0) {
			return Arrays.asList(result.getOutNullToEmpty().split("\\n")).stream()
				.map(LsResult::new)
				.filter(LsResult::isValid)
				.collect(Collectors.toList());
		}
		return rs;
	}

	public static class LsResult {
		private String perm;
		private int unknownField;
		private String user;
		private String group;
		private long length;
		private String dt;
		private String tm;
		private String zn;
		private String filename;
		
		private boolean valid;
		
		private String originLine; 
		
		public LsResult(String line) {
			originLine = line.trim();
			String[] segs = originLine.split("\\s+");
			if (segs.length == 9) {
				try {
					setPerm(segs[0]);
					setUnknownField(Integer.valueOf(segs[1]));
					setUser(segs[2]);
					setGroup(segs[3]);
					setLength(Long.valueOf(segs[4]));
					setDt(segs[5]);
					setTm(segs[6]);
					setZn(segs[7]);
					String[] ss = segs[8].split("/"); 
					setFilename(ss[ss.length - 1]);
					setValid(true);
				} catch (NumberFormatException e) {
				}
			}
		}
		
		
		public String getPerm() {
			return perm;
		}
		public void setPerm(String perm) {
			this.perm = perm;
		}
		public int getUnknownField() {
			return unknownField;
		}
		public void setUnknownField(int unknownField) {
			this.unknownField = unknownField;
		}
		public String getUser() {
			return user;
		}
		public void setUser(String user) {
			this.user = user;
		}
		public String getGroup() {
			return group;
		}
		public void setGroup(String group) {
			this.group = group;
		}
		public long getLength() {
			return length;
		}
		public void setLength(long length) {
			this.length = length;
		}
		public String getDt() {
			return dt;
		}
		public void setDt(String dt) {
			this.dt = dt;
		}
		public String getTm() {
			return tm;
		}
		public void setTm(String tm) {
			this.tm = tm;
		}
		public String getZn() {
			return zn;
		}
		public void setZn(String zn) {
			this.zn = zn;
		}
		public String getFilename() {
			return filename;
		}
		public void setFilename(String filename) {
			this.filename = filename;
		}

		public boolean isValid() {
			return valid;
		}
		public void setValid(boolean valid) {
			this.valid = valid;
		}
		
		@Override
		public String toString() {
			return originLine;
		}


		public String getOriginLine() {
			return originLine;
		}


		public void setOriginLine(String originLine) {
			this.originLine = originLine;
		}
	}

}
