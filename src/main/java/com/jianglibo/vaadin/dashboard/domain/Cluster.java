package com.jianglibo.vaadin.dashboard.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.gwt.thirdparty.guava.common.collect.Lists;

@Entity
@Table(name = "cluster")
public class Cluster extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@OneToMany
	private List<BoxWithRole> boxes = Lists.newArrayList();
	
	private String name;
	
	@Enumerated(EnumType.STRING)
	private ClusterType type;

	
	public List<BoxWithRole> getBoxes() {
		return boxes;
	}

	public void setBoxes(List<BoxWithRole> boxes) {
		this.boxes = boxes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ClusterType getType() {
		return type;
	}



	public void setType(ClusterType type) {
		this.type = type;
	}



	public static enum ClusterType {
		HADOOP, ZOOKPER, HBASE
	}

}
