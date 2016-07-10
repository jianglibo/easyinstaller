package com.jianglibo.vaadin.dashboard.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "box")
public class Box extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String ip;
	
	private String keyFile;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getKeyFile() {
		return keyFile;
	}

	public void setKeyFile(String keyFile) {
		this.keyFile = keyFile;
	}
	
	

}
