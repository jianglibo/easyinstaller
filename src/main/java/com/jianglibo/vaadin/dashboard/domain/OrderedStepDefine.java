package com.jianglibo.vaadin.dashboard.domain;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumn;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@Entity
@VaadinTable(multiSelect = true, messagePrefix = "domain.orderedstepdefine.", footerVisible = true, styleNames = {
		ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES,
		ValoTheme.TABLE_COMPACT }, selectable = true, fullSize = true)
@Table(name = "orderedstepdefine")
public class OrderedStepDefine extends BaseEntity {
	
	@OneToOne
	@VaadinTableColumn(order = 20)
	private StepDefine stepDefine;
	
	@VaadinTableColumn(order = 10)
	private int position;
	
	
	public OrderedStepDefine() {
	}
	
	public OrderedStepDefine(StepDefine stepDefine, int position) {
		this.stepDefine = stepDefine;
		this.position = position;
	}


	@Override
	public String getDisplayName() {
		return String.format("[%s,%s]", getStepDefine().getName(), getStepDefine().getOstype());
	}

	public StepDefine getStepDefine() {
		return stepDefine;
	}

	public void setStepDefine(StepDefine stepDefine) {
		this.stepDefine = stepDefine;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}
