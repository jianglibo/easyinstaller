package com.jianglibo.vaadin.dashboard.uicomponent.gridfield;

import java.util.Collection;

import org.springframework.context.MessageSource;

import com.google.common.reflect.TypeToken;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.ScalarGridFieldDescription;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.util.ColumnUtil;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.SelectionMode;

@SuppressWarnings("serial")
public abstract class ScalarGridField<C extends Collection<B>, B extends Object> extends CustomField<C> {

	private final Domains domains;

	private final MessageSource messageSource;

	private Grid grid;

	private VaadinTableWrapper vtw;

	private VaadinFormFieldWrapper vffw;

	private Class<B> clazz;
	
	private TypeToken<C> ttc;

	private MyValueChangeListener vc;

	public ScalarGridField(Domains domains,TypeToken<C> ttc, Class<B> clazz, MessageSource messageSource, VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw) {
		this.domains = domains;
		this.messageSource = messageSource;
		this.clazz = clazz;
		this.ttc = ttc;
		vc = new MyValueChangeListener();
		this.vtw = vtw;
		this.vffw = vffw;
		createGrid(vtw, vffw, null, true);
		setCaption(MsgUtil.getFieldMsg(messageSource, vtw.getVt().messagePrefix(), vffw));
		addValueChangeListener(vc);
	}

	@Override
	protected Component initContent() {
		return getGrid();
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

	private void createGrid(VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw, C lb, boolean createGrid) {

		ScalarGridFieldDescription dfd = vffw.getExtraAnotation(ScalarGridFieldDescription.class);
		
		StyleUtil.setDisableCellFocus(this);
		GeneratedPropertyContainer gpcontainer = new GeneratedPropertyContainer(
				new ScalarListContainer(clazz, lb));

		String[] columns = dfd.columns();
		String[] sortableColumns = dfd.sortableColumns();

		for (String name : columns) {
			if (name.startsWith("!")) {
				addGeneratedProperty(gpcontainer, name);
			}
		}

		if (!createGrid) {
			getGrid().setContainerDataSource(gpcontainer);
		} else {
			setWidth(100.0f, Unit.PERCENTAGE);
			Grid localGrid = new Grid();
			
			if (dfd.rowNumber() > 0) {
				localGrid.setHeightByRows(dfd.rowNumber());
				localGrid.setHeightMode(HeightMode.ROW);
			}
			
			localGrid.setWidth(100.0f, Unit.PERCENTAGE);
			localGrid.setColumns(ColumnUtil.toObjectArray(columns));
			localGrid.setSelectionMode(SelectionMode.NONE);
			localGrid.setContainerDataSource(gpcontainer);

			for (String cn : columns) {
				Grid.Column col = localGrid.getColumn(cn);
				col.setSortable(foundColumn(sortableColumns, cn));
				col.setHeaderCaption(MsgUtil.getFieldMsg(messageSource, vtw.getVt().messagePrefix(), (String) cn));
				setupColumn(col, cn);
			}
			setupGrid(localGrid);
			setGrid(localGrid);
		}
	}

	protected abstract void setupGrid(Grid grid);

	/**
	 * customize column, convert, render etc.
	 * 
	 * @param col
	 */
	public abstract void setupColumn(Column col, String name);

	@SuppressWarnings("unchecked")
	@Override
	public Class<C> getType() {
		return (Class<C>) ttc.getClass();
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

	public Grid getGrid() {
		return grid;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	protected class MyValueChangeListener implements ValueChangeListener {
		@SuppressWarnings("unchecked")
		@Override
		public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
			ScalarGridField.this.createGrid(getVtw(), getVffw(), (C) event.getProperty().getValue(), false);
		}
	}
}
