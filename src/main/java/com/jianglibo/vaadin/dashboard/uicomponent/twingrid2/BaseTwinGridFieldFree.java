package com.jianglibo.vaadin.dashboard.uicomponent.twingrid2;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import static com.google.common.base.Preconditions.*;
import com.google.common.reflect.TypeToken;
import com.jianglibo.vaadin.dashboard.data.container.AllowEmptySortListContainer;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.util.ColumnUtil;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

/**
 * 
 * @author jianglibo@gmail.com
 *
 * @param <LC>
 * @param <L>
 * @param <R>
 */
public abstract class BaseTwinGridFieldFree<LC extends Collection<L>, L extends BaseEntity, R extends BaseEntity, RC extends FreeContainer<R>>
		extends CustomField<LC> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseTwinGridFieldFree.class);

	private final MessageSource messageSource;

	private final Class<L> leftClazz;

	private Component fieldContentToReturn;
	
	private Grid leftGrid;
	
	private Grid rightGrid;
	
	private MyValueChangeListener vc;
	
	private TypeToken<LC> ttlc;
	
	private final int leftRowNumber;
	
	private final int rightRowNumber;
	
	private final RC dContainer;
	
	private String leftMessagePrefix;
	
	private String rightMessagePrefix;
	
	public BaseTwinGridFieldFree(RC dContainer, TypeToken<LC> ttlc, Class<L> leftClazz, MessageSource messageSource,int leftRowNumber, int rightRowNumber, String leftMessagePrefix, String rightMessagePrefix) {
		this.leftClazz = leftClazz;
		this.ttlc = ttlc;
		this.dContainer = checkNotNull(dContainer, "dContainer should not be null");
		this.leftRowNumber = leftRowNumber;
		this.rightRowNumber = rightRowNumber;
		this.messageSource = messageSource;
		this.leftMessagePrefix = leftMessagePrefix;
		this.rightMessagePrefix = rightMessagePrefix;
		
		
		setSizeFull();
		
		vc = new MyValueChangeListener();
		
		addValueChangeListener(vc);
		GridLayout gl = new GridLayout(2, 1);
		gl.setSizeFull();
		
		leftGrid = createLeftGrid(null, true);
		rightGrid = createRightGrid();

		gl.addComponent(leftGrid);
		gl.addComponent(rightGrid);

		leftGrid.setSizeFull();
		rightGrid.setSizeFull();
		
		fieldContentToReturn = gl;
	}

	private boolean foundColumn(String[] columns, String column) {
		for(String col : columns) {
			if (col.equals(column)) {
				return true;
			}
		}
		return false;
	}
	
	public RC getdContainer() {
		return dContainer;
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
	
	public Grid createLeftGrid(LC lc, boolean createGrid) {
		AllowEmptySortListContainer<L> lcc = new AllowEmptySortListContainer<L>(leftClazz, lc);
		GeneratedPropertyContainer gpcontainer = new GeneratedPropertyContainer(lcc);
		
		String[] columns = getLeftColumns();
		String[] sortableColumns = getLeftSortableColumns();
		
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
//		grid.setWidth(100.0f, Unit.PERCENTAGE);
		grid.setSizeFull();
		if (leftRowNumber > 0) {
			grid.setHeightMode(HeightMode.ROW);
			grid.setHeightByRows(leftRowNumber);
		}
		grid.setColumns(ColumnUtil.toObjectArray(columns));
		grid.setSelectionMode(SelectionMode.NONE);
		grid.setContainerDataSource(gpcontainer);
		
		
		for(String cn : columns){
			Grid.Column col = grid.getColumn(cn);
			col.setSortable(foundColumn(sortableColumns, cn));
			col.setHeaderCaption(MsgUtil.getFieldMsg(messageSource, leftMessagePrefix, (String)cn));
			setupLeftColumn(col, cn);
		}

		TextField filterField = new TextField();
		filterField.setWidth(100.0f, Unit.PERCENTAGE);
		filterField.setEnabled(false);
		filterField.setReadOnly(true);

		HeaderRow groupingHeader = grid.appendHeaderRow();

		HeaderCell namesCell = groupingHeader.join(ColumnUtil.toObjectArray(columns));
		namesCell.setComponent(filterField);
		setupLeftGrid(grid);
		return grid;
	}
	
	protected abstract String[] getLeftSortableColumns();

	protected abstract String[] getLeftColumns();

	public abstract void setupLeftGrid(Grid grid);
	
	public Grid createRightGrid() {
		GeneratedPropertyContainer gpcontainer = new GeneratedPropertyContainer(dContainer);
		String[] columns = getRightColumns();
		String[] sortableColumns = getRightSortableColumns().toArray(new String[]{});
		
		for(String name: columns) {
			if (name.startsWith("!")) {
				addGeneratedPropertyForRight(gpcontainer, name);
			}
		}
		
		setWidth(100.0f, Unit.PERCENTAGE);
		Grid grid = new Grid();
		grid.setWidth(100.0f, Unit.PERCENTAGE);
		grid.setColumns(ColumnUtil.toObjectArray(columns));
		grid.setSelectionMode(SelectionMode.NONE);
		grid.setContainerDataSource(gpcontainer);
		
		if (rightRowNumber > 0) {
			grid.setHeightMode(HeightMode.ROW);
			grid.setHeightByRows(rightRowNumber);
		}
		
		for(String cn : columns){
			Grid.Column col = grid.getColumn(cn);
			col.setSortable(foundColumn(sortableColumns, cn));
			col.setHeaderCaption(MsgUtil.getFieldMsg(messageSource, rightMessagePrefix, (String)cn));
			setupRightColumn(col, cn);
		}

		TextField filterField = new TextField();
		filterField.setWidth(100.0f, Unit.PERCENTAGE);

		HeaderRow groupingHeader = grid.appendHeaderRow();

		HeaderCell namesCell = groupingHeader.join(ColumnUtil.toObjectArray(columns));
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
	

	protected abstract String[] getRightColumns();

	protected abstract List<String> getRightSortableColumns();

	public abstract void setupRightGrid(Grid grid);

	@SuppressWarnings("unchecked")
	@Override
	public Class<LC> getType() {
		return (Class<LC>) ttlc.getRawType();
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void refreshValue() {
	}

	@SuppressWarnings("serial")
	protected class MyValueChangeListener implements ValueChangeListener {
		@Override
		public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
			BaseTwinGridFieldFree.this.createLeftGrid((LC) event.getProperty().getValue(), false);
		}
	}
}
