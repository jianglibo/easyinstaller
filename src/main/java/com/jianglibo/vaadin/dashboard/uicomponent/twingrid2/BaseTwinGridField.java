package com.jianglibo.vaadin.dashboard.uicomponent.twingrid2;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import com.google.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.TwinGridFieldDescription;
import com.jianglibo.vaadin.dashboard.data.container.AllowEmptySortListContainer;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

/**
 * Every field need to know @VaadinFormFieldWrapper and @VaadinTableWrapper
 * 
 * Field's datasource has no relation to leftGrid's datasource.  
 * 
 * The left grid is a listcontainer, right grid is a general FreeContainer.
 * 
 * @author jianglibo@gmail.com
 *
 * @param <LC>
 * @param <L>
 * @param <R>
 */
public abstract class BaseTwinGridField<LC extends Collection<L>, L extends BaseEntity, R extends BaseEntity>
		extends CustomField<LC> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseTwinGridField.class);

	private final Domains domains;

	private final MessageSource messageSource;

	private final Class<L> leftClazz;

	private final Class<R> rightClazz;

	private Component fieldContentToReturn;
	
	private VaadinFormFieldWrapper vffw;
	
	private VaadinTableWrapper vtw;
	
	private Grid leftGrid;
	
	private Grid rightGrid;
	
	private MyValueChangeListener vc;
	
	public BaseTwinGridField(Class<L> leftClazz, Class<R> rightClazz, Domains domains, MessageSource messageSource,VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw) {
		this.leftClazz = leftClazz;
		this.rightClazz = rightClazz;
		this.domains = domains;
		this.messageSource = messageSource;
		vc = new MyValueChangeListener();
		addValueChangeListener(vc);
		setVffw(vffw);
		HorizontalLayout hl = new HorizontalLayout();
		setWidth(100.0f, Unit.PERCENTAGE);
		hl.setWidth(100.0f, Unit.PERCENTAGE);
		
		setCaption(MsgUtil.getFieldMsg(messageSource, vtw.getVt().messagePrefix(), vffw));
		
		leftGrid = createLeftGrid(vtw, vffw, null, true);
		rightGrid = createRightGrid(vtw, vffw);
		
		leftGrid.setWidth(100.0f, Unit.PERCENTAGE);
		rightGrid.setWidth(100.0f, Unit.PERCENTAGE);

		hl.addComponent(leftGrid);
		hl.addComponent(rightGrid);

		hl.setExpandRatio(leftGrid, 1);
		hl.setExpandRatio(rightGrid, 1);
		fieldContentToReturn = hl;
	}

	private boolean foundColumn(String[] columns, String column) {
		for(String col : columns) {
			if (col.equals(column)) {
				return true;
			}
		}
		return false;
	}
	
	
	@Override
	protected Component initContent() {
		return fieldContentToReturn;
	}
	
	public abstract void addGeneratedPropertyForLeft(GeneratedPropertyContainer gpcontainer, String extraName);
	public abstract void addGeneratedPropertyForRight(GeneratedPropertyContainer gpcontainer, String extraName);
	
	/**
	 * customize column, convert, render etc.
	 * @param col
	 */
	public abstract void setupLeftColumn(Column col, String name);

	/**
	 * customize column, convert, render etc.
	 * @param col
	 */
	public abstract void setupRightColumn(Column col, String name);
	
	public abstract void whenLeftItemClicked(ItemClickEvent event);
	public abstract void whenRightItemClicked(ItemClickEvent event);
	
	public Grid createLeftGrid(VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw, LC lc, boolean createGrid) {
		AllowEmptySortListContainer<L> lcc = new AllowEmptySortListContainer<L>(leftClazz, lc);
		GeneratedPropertyContainer gpcontainer = new GeneratedPropertyContainer(lcc);
		
		TwinGridFieldDescription tgfd = vffw.getExtraAnotation(TwinGridFieldDescription.class);
		
		String[] columns = tgfd.leftColumns();
		String[] sortableColumns = tgfd.leftSortableColumns();
		
		for(String name: columns) {
			if (name.startsWith("!")) {
				addGeneratedPropertyForLeft(gpcontainer, name);
			}
		}
		
		if (!createGrid) {
			leftGrid.setContainerDataSource(gpcontainer);
			return null;
		}
		
		setWidth(100.0f, Unit.PERCENTAGE);
		Grid grid = new Grid();
		grid.setWidth(100.0f, Unit.PERCENTAGE);
		grid.setColumns(columns);
		grid.setSelectionMode(SelectionMode.NONE);
		grid.setContainerDataSource(gpcontainer);
		
		String messagePrefix = domains.getTables().get(leftClazz.getSimpleName()).getVt().messagePrefix();
		
		for(String cn : columns){
			Grid.Column col = grid.getColumn(cn);
			col.setSortable(foundColumn(sortableColumns, cn));
			col.setHeaderCaption(MsgUtil.getFieldMsg(messageSource, messagePrefix, (String)cn));
			setupLeftColumn(col, cn);
		}
		
		if (tgfd.addItemClickListenerForLeft()) {
			grid.addItemClickListener(event -> {
				whenLeftItemClicked(event);
			});
		}

		TextField filterField = new TextField();
		filterField.setWidth(100.0f, Unit.PERCENTAGE);
		filterField.setEnabled(false);
		filterField.setReadOnly(true);

		HeaderRow groupingHeader = grid.appendHeaderRow();

		HeaderCell namesCell = groupingHeader.join(columns);
		namesCell.setComponent(filterField);
		setupLeftGrid(grid);
		return grid;
	}
	
	public abstract void setupLeftGrid(Grid grid);

	public Grid createRightGrid(VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw) {
		FreeContainer<R> lcc = new FreeContainer<>(domains, rightClazz, vffw.getExtraAnotation(TwinGridFieldDescription.class).rightPageLength(), vtw.getSortableContainerPropertyIds());
		GeneratedPropertyContainer gpcontainer = new GeneratedPropertyContainer(lcc);
		
		TwinGridFieldDescription tgfd = vffw.getExtraAnotation(TwinGridFieldDescription.class);
		
		String[] columns = tgfd.rightColumns();
		String[] sortableColumns = tgfd.rightSortableColumns();
		
		for(String name: columns) {
			if (name.startsWith("!")) {
				addGeneratedPropertyForRight(gpcontainer, name);
			}
		}
		
		setWidth(100.0f, Unit.PERCENTAGE);
		Grid grid = new Grid();
		grid.setWidth(100.0f, Unit.PERCENTAGE);
		grid.setColumns(columns);
		grid.setSelectionMode(SelectionMode.NONE);
		grid.setContainerDataSource(gpcontainer);
		
		String messagePrefix = domains.getTables().get(rightClazz.getSimpleName()).getVt().messagePrefix();
		
		for(String cn : columns){
			Grid.Column col = grid.getColumn(cn);
			col.setSortable(foundColumn(sortableColumns, cn));
			col.setHeaderCaption(MsgUtil.getFieldMsg(messageSource, messagePrefix, (String)cn));
			setupRightColumn(col, cn);
		}
		
		if (tgfd.addItemClickListenerForRight()) {
			grid.addItemClickListener(event -> {
				whenRightItemClicked(event);
			});			
		}

		TextField filterField = new TextField();
		filterField.setWidth(100.0f, Unit.PERCENTAGE);

		HeaderRow groupingHeader = grid.appendHeaderRow();

		HeaderCell namesCell = groupingHeader.join(columns);
		namesCell.setComponent(filterField);
		
		filterField.addTextChangeListener(change -> {
			// Can't modify filters so need to replace
			gpcontainer.removeAllContainerFilters();
			// (Re)create the filter if necessary
			gpcontainer.addContainerFilter(new SimpleStringFilter("", change.getText(), true, false));

		});
		setupRightGrid(grid);
		return grid;
	}
	

	public abstract void setupRightGrid(Grid grid);

	@SuppressWarnings("unchecked")
	@Override
	public Class<LC> getType() {
		return (Class<LC>) Collection.class;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void refreshValue() {
	}

	public VaadinFormFieldWrapper getVffw() {
		return vffw;
	}

	public void setVffw(VaadinFormFieldWrapper vffw) {
		this.vffw = vffw;
	}

	public VaadinTableWrapper getVtw() {
		return vtw;
	}

	public void setVtw(VaadinTableWrapper vtw) {
		this.vtw = vtw;
	}

	public Domains getDomains() {
		return domains;
	}
	
	@SuppressWarnings("serial")
	protected class MyValueChangeListener implements ValueChangeListener {
		@Override
		public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
			BaseTwinGridField.this.createLeftGrid(getVtw(), getVffw(), (LC) event.getProperty().getValue(), false);
		}
	}
}
