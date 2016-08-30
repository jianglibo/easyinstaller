package com.jianglibo.vaadin.dashboard.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumn;

/**
 * An installation combined with stepruns. Not shared between boxes.
 * @author jianglibo@gmail.com
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "installation", uniqueConstraints = { @UniqueConstraint(columnNames = {"name", "ostype"})})
public class Installation extends BaseEntity {
	
	@NotNull
	@VaadinTableColumn
	@VaadinFormField(order = 10)
	private String name;
	
	@OneToMany(mappedBy="installation")
	@OrderBy("order ASC")
	private List<StepRun> steps = Lists.newArrayList();
	
	@ManyToOne
	private Box box;
	
	@OneToOne
	private StepRun lastStep;
	
	@NotNull
	@VaadinTableColumn
	@VaadinFormField(order = 20)
	private String ostype;
	
	@Override
	public String getDisplayName() {
		return null;
	}

	public List<StepRun> getSteps() {
		return steps;
	}

	public void setSteps(List<StepRun> steps) {
		this.steps = steps;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Box getBox() {
		return box;
	}

	public void setBox(Box box) {
		this.box = box;
	}

	public String getOstype() {
		return ostype;
	}

	public void setOstype(String ostype) {
		this.ostype = ostype;
	}

	public StepRun getLastStep() {
		return lastStep;
	}

	public void setLastStep(StepRun lastStep) {
		this.lastStep = lastStep;
	}
	
	
}
