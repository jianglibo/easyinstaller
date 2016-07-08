package com.jianglibo.vaadin.dashborad;

import com.jcraft.jsch.JSchException;
import com.jianglibo.vaadin.dashboard.ssh.JschSession;

public class Tutil {

	public static void printme(Object o) {
		System.out.println(o);
	}
	
	public JschSession getJschSession() throws JSchException{
		return  new JschSession.JschSessionBuilder()//
				.setHost("che.intranet.fh.gov.cn")//
				.setKeyFile("D:\\cygwin64\\home\\admin\\aaa")
				.setKnownHosts("D:\\cygwin64\\home\\admin\\.ssh\\known_hosts").setUser("root").build();
	}
}
