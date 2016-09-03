package com.jianglibo.vaadin.dashboard.domain;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.GlobalComboOptions;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField.Ft;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumn;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.ComboBoxBackByStringOptions;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.TwinGridFieldDescription;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.themes.ValoTheme;

/**
 * A installation can not exists alone, It must install to a machine.
 * 
 * @author jianglibo@gmail.com
 *
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "software", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "ostype" }) })
@VaadinTable(multiSelect = true, footerVisible = true, messagePrefix = "domain.software.", styleNames = {
		ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES,
		ValoTheme.TABLE_COMPACT }, selectable = true, fullSize = true)
public class Software extends BaseEntity {

	@ComboBoxBackByStringOptions(key = GlobalComboOptions.SOFTWARE_NAMES)
	@VaadinFormField(fieldType = Ft.COMBO_BOX)
	@VaadinTableColumn(alignment = Align.LEFT)
	@NotNull
	private String name;

	@ComboBoxBackByStringOptions(key = GlobalComboOptions.OS_TYPES)
	@VaadinFormField(fieldType = Ft.COMBO_BOX)
	@VaadinTableColumn()
	@NotNull
	private String ostype;

	@OneToMany(fetch = FetchType.EAGER)
	@OrderBy("position ASC")
	@TwinGridFieldDescription(leftClazz = OrderedStepDefine.class, rightClazz = StepDefine.class, leftPageLength = 100, rightColumns = {
			"name", "ostype" }, leftColumns = { "position", "stepDefine" })
	@VaadinFormField(fieldType = Ft.HAND_MAKER, order = 30)
	
	private List<OrderedStepDefine> orderedStepDefines = Lists.newArrayList();

	/**
	 * when orderedStepDefines changes name, change static file bellow too.
	 */
	public static final String orderedStepDefinesFieldName = "orderedStepDefines";

	@Lob
	private String sortedIds;

	public Software() {

	}

	public Software(String name, String ostype) {
		setName(name);
		setOstype(ostype);
	}

	public Install createNewInstall() {
		Install in = new Install(this);
		in.setStepRuns(getOrderedStepDefines().stream().map(isd -> new StepRun(in, isd)).collect(Collectors.toList()));
		return in;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOstype() {
		return ostype;
	}

	public void setOstype(String ostype) {
		this.ostype = ostype;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("name", getName()).add("ostye", getOstype()).toString();
	}

	@Override
	public String getDisplayName() {
		return name;
	}

	public List<OrderedStepDefine> getOrderedStepDefines() {
		return orderedStepDefines;
	}

	public void setOrderedStepDefines(List<OrderedStepDefine> orderedStepDefines) {
		this.orderedStepDefines = orderedStepDefines;
	}

	public String getSortedIds() {
		return sortedIds;
	}

	public void setSortedIds(String sortedIds) {
		this.sortedIds = sortedIds;
	}
}
