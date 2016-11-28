package com.jianglibo.vaadin.dashboard.uicomponent.grid;

import java.util.List;
import java.util.Optional;

import org.springframework.context.MessageSource;

import static com.google.common.base.Preconditions.*;

import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumnWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridWrapper;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.jianglibo.vaadin.dashboard.util.ColumnUtil;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public abstract class BaseGrid<T extends BaseEntity, C extends FreeContainer<T>> extends Grid {
	
	/**
	 * allow null
	 */
	private final VaadinGridWrapper vgw;

	private final MessageSource messageSource;
	
	private final C dContainer;
	
	private final List<String> sortableContainerPropertyIds;
	
	private final List<String> columnNames; 
	
	private final String messagePrefix;

	public BaseGrid(VaadinGridWrapper vgw, C dContainer, MessageSource messageSource, List<String> sortableContainerPropertyIds, List<String> columnNames, String messagePrefix) {
		this.messageSource = messageSource;
		this.dContainer = checkNotNull(dContainer, "dataContainer should not be null");
		this.messagePrefix = messagePrefix;
		this.sortableContainerPropertyIds = sortableContainerPropertyIds;
		this.columnNames = columnNames;
		this.vgw = vgw;
	}

	public void delayCreateContent() {
		StyleUtil.setDisableCellFocus(this);
		
		GeneratedPropertyContainer gpcontainer = new GeneratedPropertyContainer(dContainer);
		
		for (String name : getColumnNames()) {
			if (name.startsWith("!")) {
				addGeneratedProperty(gpcontainer, name);
			}
		}
		setSizeFull();

		setColumns(ColumnUtil.toObjectArray(getColumnNames()));
		setSelectionMode(vgw.getVg().selectMode());
		setContainerDataSource(gpcontainer);
		
		getColumnNames().forEach(cn -> {
			Grid.Column col = getColumn(cn);
			setupColumn(col, cn);
		});

		setColumnFiltering(getColumnNames());
		setupGrid();
//		setSortOrder(Lists.newArrayList(getdContainer().getVaadinSort()));
		sort(getdContainer().getVaadinSort());
		// Allow column reordering
		setColumnReorderingAllowed(true);
	}
	
	// Add a summary footer row to the Grid
	//	FooterRow footer = addFooterRowAt(0);
	//	setSummaryFooterCells(footer);
	
	protected abstract void setupGrid();

	protected void setupColumn(Column col, String cn) {
		checkNotNull(getVgw(), "if vgw is null, you must override setupColumn(Column col, String cn).");
		Optional<VaadinGridColumnWrapper> vgcw = getVgw().getColumns().stream().filter(one -> cn.equals(one.getName())).findAny();
		if (vgcw.isPresent()) {
			setupColumn(vgcw.get(), col);
		}
		switch (cn) {
		case "archived":
			ColumnUtil.setTrueFalseRender(col, false);
			break;
		case "success":
			ColumnUtil.setTrueFalseRender(col, true);
			break;
		default:
			break;
		}
	}

	private void setupColumn(VaadinGridColumnWrapper vgcw, Grid.Column col) {
		if (getSortableContainerPropertyIds().contains(vgcw.getName())) {
			col.setSortable(vgcw.getVgc().sortable());
		}
		col.setHeaderCaption(MsgUtil.getFieldMsg(messageSource, messagePrefix, vgcw.getName()));
		col.setHidable(vgcw.getVgc().hidable());
		col.setHidden(vgcw.getVgc().initHidden());
	}

	/**
	 * can setup whole grid here. Because this is extend from grid.
	 * @param footer
	 */
//	protected abstract void setSummaryFooterCells(FooterRow footer);

	
	private void setColumnFiltering(List<String> columnNames) {
		if (showFilterRow()) {
			HeaderRow hr = appendHeaderRow();
			
			columnNames.forEach(cn -> {
				if (showFilterRow(cn)) {
					TextField filter = getColumnFilter(cn);
					HeaderCell hc = hr.getCell(cn);
					hc.setComponent(filter);
					hc.setStyleName("filter-header");
				}
			});
		}
	}

	protected boolean showFilterRow(String cn) {
		checkNotNull(getVgw(), "if vgw is null, you must override showFilterRow(String cn).");
		Optional<VaadinGridColumnWrapper> vgcw = getVgw().getColumns().stream().filter(one -> cn.equals(one.getName())).findAny();
		if (vgcw.isPresent()) {
			return vgcw.get().getVgc().filterable();
		} else {
			return false;
		}

	}

	protected boolean showFilterRow() {
		checkNotNull(getVgw(), "if vgw is null, you must override showFilterRow().");
		return getVgw().getColumns().stream().filter(vgcw -> vgcw.getVgc().filterable()).findAny().isPresent();
	}

	private TextField getColumnFilter(final Object columnId) {
		TextField filter = new TextField();
		filter.setWidth("100%");
		filter.addStyleName(ValoTheme.TEXTFIELD_TINY);
		filter.setInputPrompt("Filter");
		filter.addTextChangeListener(new TextChangeListener() {
			SimpleStringFilter filter = null;

			@Override
			public void textChange(TextChangeEvent event) {
				Filterable f = (Filterable) getContainerDataSource();

				// Remove old filter
				if (filter != null) {
					f.removeContainerFilter(filter);
				}

				// Set new filter for the "Name" column
				filter = new SimpleStringFilter(columnId, event.getText(), true, true);
				f.addContainerFilter(filter);
				cancelEditor();
			}
		});
		return filter;
	}

	// private void setSummaryFooterCells(FooterRow footer) {
	// for (int i = 0; i < numberOfYearHalves; i++) {
	// String propertyId = ExampleUtil.getBudgetPeriodPropertyId(i);
	//
	// // Loop items in the data source to calculate sums
	// double sum = 0;
	// for (Object itemId : sample.getContainerDataSource().getItemIds()) {
	// BigDecimal value = (BigDecimal) sample.getContainerDataSource()
	// .getItem(itemId).getItemProperty(propertyId).getValue();
	// sum += value.doubleValue();
	// }
	//
	// // Use the same converter as the column values use
	// footer.getCell(propertyId).setText(
	// ((DollarConverter) sample.getColumn(propertyId)
	// .getConverter()).convertToPresentation(
	// new BigDecimal(sum), String.class,
	// sample.getLocale()));
	//
	// // Align the footer text
	// footer.getCell(propertyId).setStyleName("align-right");
	// }
	// }

	public VaadinGridWrapper getVgw() {
		return vgw;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public C getdContainer() {
		return dContainer;
	}

	protected abstract void addGeneratedProperty(GeneratedPropertyContainer gpcontainer, String name);

	public List<?> getSortableContainerPropertyIds() {
		return sortableContainerPropertyIds;
	}

	public List<String> getColumnNames() {
		return columnNames;
	}
}
