package com.jianglibo.vaadin.dashboard.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumn;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.ComboBoxBackByContainer;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.GridFieldDescription;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField.Ft;
import com.vaadin.ui.themes.ValoTheme;

/**
 * An installation combined with stepruns. Not shared between boxes.
 * 
 * @author jianglibo@gmail.com
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "install")
@VaadinTable(multiSelect = true, footerVisible = true, messagePrefix = "domain.install.", styleNames = {
		ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES,
		ValoTheme.TABLE_COMPACT }, selectable = true, fullSize = true)
public class Install extends BaseEntity {

	@OneToOne
	@VaadinTableColumn
	@VaadinFormField(fieldType = Ft.COMBO_BOX, order = 10)
	@ComboBoxBackByContainer(entityClass = Software.class, pageLength = 10)
	private Software software;

	/**
	 * If use JPA CascadeType.PERSIST will not save relations. fix it by manually in form code.
	 */
	@OneToMany(mappedBy = "install", fetch=FetchType.EAGER, cascade={CascadeType.REMOVE})
	@OrderBy("position ASC")
	@VaadinFormField(fieldType = Ft.HAND_MAKER, order = 20)
	@GridFieldDescription(columns = { "position", "name", "ostype", "!edit" }, clazz = StepRun.class)
	private List<StepRun> stepRuns = Lists.newArrayList();

	@ManyToOne
	private Box box;

	@OneToOne
	private StepRun lastStep;

	public Install() {
	}

	public Install(Software software) {
		setSoftware(software);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("name", getSoftware().getName()).add("ostye", getSoftware().getOstype())
				.toString();
	}

	@Override
	public String getDisplayName() {
		return null;
	}

	public Box getBox() {
		return box;
	}

	public void setBox(Box box) {
		this.box = box;
	}

	public List<StepRun> getStepRuns() {
		return stepRuns;
	}

	public void setStepRuns(List<StepRun> stepRuns) {
		this.stepRuns = stepRuns;
	}

	public StepRun getLastStep() {
		return lastStep;
	}

	public void setLastStep(StepRun lastStep) {
		this.lastStep = lastStep;
	}

	public Software getSoftware() {
		return software;
	}

	public void setSoftware(Software software) {
		this.software = software;
	}
}
