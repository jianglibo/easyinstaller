package com.jianglibo.vaadin.dashboard;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;
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
	
	public static List<String> randomStrings(int size) {
		List<String> strs = Lists.newArrayList();
		
		for(int i =0; i< size; i++) {
			strs.add(UUID.randomUUID().toString().replaceAll("-", ""));
		}
		return strs;
	}
}
