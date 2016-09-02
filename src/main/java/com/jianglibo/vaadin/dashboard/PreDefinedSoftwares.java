package com.jianglibo.vaadin.dashboard;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.jianglibo.vaadin.dashboard.vo.PreDefinedSoftware;

/**
 * Software is just combination of stepruns.
 * 
 * @author jianglibo@gmail.com
 *
 */
@Component
@ConfigurationProperties(prefix="softwares")
public class PreDefinedSoftwares {
	
	private List<PreDefinedSoftware> predefined;

	public List<PreDefinedSoftware> getPredefined() {
		return predefined;
	}

	public void setPredefined(List<PreDefinedSoftware> predefined) {
		this.predefined = predefined;
	}
}
