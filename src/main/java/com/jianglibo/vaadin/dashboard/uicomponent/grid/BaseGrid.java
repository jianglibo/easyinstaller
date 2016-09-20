package com.jianglibo.vaadin.dashboard.uicomponent.grid;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumnWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridWrapper;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.jianglibo.vaadin.dashboard.domain.Domains;
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
public abstract class BaseGrid<T extends BaseEntity> extends Grid {

	private final VaadinGridWrapper vgw;

	public BaseGrid(MessageSource messageSource, Domains domains, Class<T> clazz) {
		this.vgw = domains.getGrids().get(clazz.getSimpleName());
		FreeContainer<T> lcc = new FreeContainer<>(domains, clazz, vgw.getVg().defaultPerPage(),
				vgw.getSortableColumnNames());

		StyleUtil.setDisableCellFocus(this);

		GeneratedPropertyContainer gpcontainer = new GeneratedPropertyContainer(lcc);

		List<VaadinGridColumnWrapper> columnWrappers = vgw.getColumns();

		String[] columns = columnWrappers.stream().map(cw -> cw.getName()).collect(Collectors.toList())
				.toArray(new String[] {});

		for (String name : columns) {
			if (name.startsWith("!")) {
				addGeneratedProperty(gpcontainer, name);
			}
		}
		setSizeFull();

		setColumns(columns);
		setSelectionMode(vgw.getVg().selectMode());
		setContainerDataSource(gpcontainer);

		String messagePrefix = domains.getGrids().get(clazz.getSimpleName()).getVg().messagePrefix();

		columnWrappers.stream().forEach(vgcw -> {
			Grid.Column col = getColumn(vgcw.getName());
			col.setSortable(vgcw.getVgc().sortable());
			col.setHeaderCaption(MsgUtil.getFieldMsg(messageSource, messagePrefix, vgcw.getName()));
			col.setHidable(vgcw.getVgc().hidable());
			col.setHidden(vgcw.getVgc().initHidden());
			setupColumn(col, vgcw);
		});

		setColumnFiltering(columnWrappers);

		// Add a summary footer row to the Grid
		FooterRow footer = addFooterRowAt(0);

		setSummaryFooterCells(footer);

		// Allow column reordering
		setColumnReorderingAllowed(true);
	}

	protected abstract void setSummaryFooterCells(FooterRow footer);

	private void setColumnFiltering(Collection<VaadinGridColumnWrapper> columns) {
		if (columns.stream().filter(vgcw -> vgcw.getVgc().filterable()).count() > 0) {
			HeaderRow hr = appendHeaderRow();
			columns.stream().filter(vgcw -> vgcw.getVgc().filterable()).forEach(vgcw -> {
				TextField filter = getColumnFilter(vgcw.getName());
				HeaderCell hc = hr.getCell(vgcw.getName());
				hc.setComponent(filter);
				hc.setStyleName("filter-header");
			});
		}
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

	protected abstract void setupColumn(Column col, VaadinGridColumnWrapper vgcw);

	protected abstract void addGeneratedProperty(GeneratedPropertyContainer gpcontainer, String name);
}
