package com.jianglibo.vaadin.dashboard.uicomponent.gridfield;

import java.util.Collection;
import java.util.List;

import org.springframework.context.MessageSource;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.GridFieldDescription;
import com.jianglibo.vaadin.dashboard.data.container.AllowEmptySortListContainer;
import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.SelectionMode;

@SuppressWarnings("serial")
public abstract class BaseGridField<C extends Collection<B>, B extends BaseEntity> extends CustomField<C> {

	private final Domains domains;

	private final MessageSource messageSource;

	private Grid grid;

	private VaadinTableWrapper vtw;

	private VaadinFormFieldWrapper vffw;

	private Class<B> clazz;

	private MyValueChangeListener vc;

	public BaseGridField(Domains domains, Class<B> clazz, MessageSource messageSource, VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw) {
		this.domains = domains;
		this.messageSource = messageSource;
		this.clazz = clazz;
		vc = new MyValueChangeListener();
		this.vtw = vtw;
		this.vffw = vffw;
		grid = createGrid(vtw, vffw, Lists.newArrayList(), true);
		setCaption(MsgUtil.getFieldMsg(messageSource, vtw.getVt().messagePrefix(), vffw));
		addValueChangeListener(vc);
	}

	@Override
	protected Component initContent() {
		return grid;
	}

	private boolean foundColumn(String[] columns, String column) {
		for (String col : columns) {
			if (col.equals(column)) {
				return true;
			}
		}
		return false;
	}

	public abstract void addGeneratedProperty(GeneratedPropertyContainer gpcontainer, String extraName);

	private Grid createGrid(VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw, List<B> lb, boolean createGrid) {

		GridFieldDescription dfd = vffw.getExtraAnotation(GridFieldDescription.class);
		GeneratedPropertyContainer gpcontainer = new GeneratedPropertyContainer(
				new AllowEmptySortListContainer<>(clazz, lb));

		String[] columns = dfd.columns();
		String[] sortableColumns = dfd.sortableColumns();

		for (String name : columns) {
			if (name.startsWith("!")) {
				addGeneratedProperty(gpcontainer, name);
			}
		}

		if (!createGrid) {
			grid.setContainerDataSource(gpcontainer);
			return null;
		}

		setWidth(100.0f, Unit.PERCENTAGE);
		grid = new Grid();
		
		grid.setWidth(100.0f, Unit.PERCENTAGE);
		grid.setColumns(columns);
		grid.setSelectionMode(SelectionMode.NONE);
		grid.setContainerDataSource(gpcontainer);

		for (String cn : columns) {
			Grid.Column col = grid.getColumn(cn);
			col.setSortable(foundColumn(sortableColumns, cn));
			col.setHeaderCaption(MsgUtil.getFieldMsg(messageSource, vtw.getVt().messagePrefix(), (String) cn));
			setupColumn(col, cn);
		}
		
		setupGrid(grid);
		
		if (dfd.addItemClickListener()) {
			grid.addItemClickListener(event -> {
				whenItemClicked(event);
			});
		}
		return grid;
	}

	protected abstract void setupGrid(Grid grid);

	public abstract void whenItemClicked(ItemClickEvent event);

	/**
	 * customize column, convert, render etc.
	 * 
	 * @param col
	 */
	public abstract void setupColumn(Column col, String name);

	@SuppressWarnings("unchecked")
	@Override
	public Class<C> getType() {
		return (Class<C>) Collection.class;
	}

	public Domains getDomains() {
		return domains;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public VaadinTableWrapper getVtw() {
		return vtw;
	}

	public VaadinFormFieldWrapper getVffw() {
		return vffw;
	}

	public Class<B> getClazz() {
		return clazz;
	}

	protected class MyValueChangeListener implements ValueChangeListener {
		@SuppressWarnings("unchecked")
		@Override
		public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
			BaseGridField.this.createGrid(getVtw(), getVffw(), (List<B>) event.getProperty().getValue(), false);
		}
	}
}
