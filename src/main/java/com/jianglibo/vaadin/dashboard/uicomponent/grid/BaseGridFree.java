package com.jianglibo.vaadin.dashboard.uicomponent.grid;

import java.util.stream.Stream;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.jianglibo.vaadin.dashboard.domain.Domains;
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
public abstract class BaseGridFree<T extends BaseEntity, C extends FreeContainer<T>> extends Grid {

	private final Class<T> clazz;
	private final Domains domains;

	private final MessageSource messageSource;
	
	private C originDataSource;
	
	private String[] columns;

	public BaseGridFree(MessageSource messageSource, Domains domains, Class<T> clazz, String[] columns) {
		this.messageSource = messageSource;
		this.clazz = clazz;
		this.domains = domains;
		this.columns = columns;
	}

	public C getOriginDataSource() {
		return originDataSource;
	}

	public void setOriginDataSource(C originDataSource) {
		this.originDataSource = originDataSource;
	}

	public void delayCreateContent() {
		StyleUtil.setDisableCellFocus(this);
		originDataSource = createContainer();
		
		GeneratedPropertyContainer gpcontainer = new GeneratedPropertyContainer(originDataSource);

		for (String name : columns) {
			if (name.startsWith("!")) {
				addGeneratedProperty(gpcontainer, name);
			}
		}
		setSizeFull();

		setColumns(ColumnUtil.toObjectArray(columns));
		setContainerDataSource(gpcontainer);

		String messagePrefix = domains.getGrids().get(clazz.getSimpleName()).getVg().messagePrefix();

		Stream.of(columns).forEach(cn -> {
			Grid.Column col = getColumn(cn);
			col.setHeaderCaption(MsgUtil.getFieldMsg(messageSource, messagePrefix, cn));
//			col.setSortable(vgcw.getVgc().sortable());
//			col.setHidable(vgcw.getVgc().hidable());
//			col.setHidden(vgcw.getVgc().initHidden());
			setupColumn(col, cn);
			setColumnFiltering(col, cn);
		});

		
		setupGrid();

		// Add a summary footer row to the Grid
		FooterRow footer = addFooterRowAt(0);

		setSummaryFooterCells(footer);

		// Allow column reordering
		setColumnReorderingAllowed(true);
	}

	protected abstract void setupGrid();

	protected abstract void setupColumn(Column col, String cn);

	protected abstract void setColumnFiltering(Column col, String cn);

	protected abstract C createContainer();
//	{
//		return (C) new FreeContainer(domains, clazz, vgw.getVg().defaultPerPage(), vgw.getSortableColumnNames());
//	}

	protected abstract void setSummaryFooterCells(FooterRow footer);

	protected abstract void setColumnFiltering();
//	{
//		if (columns.stream().filter(vgcw -> vgcw.getVgc().filterable()).count() > 0) {
//			HeaderRow hr = appendHeaderRow();
//			columns.stream().filter(vgcw -> vgcw.getVgc().filterable()).forEach(vgcw -> {
//				TextField filter = getColumnFilter(vgcw.getName());
//				HeaderCell hc = hr.getCell(vgcw.getName());
//				hc.setComponent(filter);
//				hc.setStyleName("filter-header");
//			});
//		}
//	}

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


	public Class<T> getClazz() {
		return clazz;
	}

	public Domains getDomains() {
		return domains;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	protected abstract void addGeneratedProperty(GeneratedPropertyContainer gpcontainer, String name);
}
