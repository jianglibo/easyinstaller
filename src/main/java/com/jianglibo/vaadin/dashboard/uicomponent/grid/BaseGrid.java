package com.jianglibo.vaadin.dashboard.uicomponent.grid;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumnWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
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
	
	private final VaadinTableWrapper vtw;

	public BaseGrid(MessageSource messageSource, Domains domains, Class<T> clazz) {
		this.vtw = domains.getTables().get(clazz.getSimpleName()); 
		FreeContainer<T> lcc = new FreeContainer<>(domains, clazz, vtw.getVt().defaultPerPage());
		
		StyleUtil.setDisableCellFocus(this);
		
		GeneratedPropertyContainer gpcontainer = new GeneratedPropertyContainer(lcc);
		
		Collection<VaadinTableColumnWrapper> columnWrappers = domains.getTableColumns().get(clazz.getSimpleName()).getColumns();
		String[] columns = columnWrappers.stream().map(cw -> cw.getName()).collect(Collectors.toList()).toArray(new String[]{});
		
		String[] sortableColumns = columnWrappers.stream().filter(cw -> cw.getVtc().sortable()).map(cw -> cw.getName()).collect(Collectors.toList()).toArray(new String[]{});
		
		for(String name: columns) {
			if (name.startsWith("!")) {
				addGeneratedProperty(gpcontainer, name);
			}
		}
		
		setWidth(100.0f, Unit.PERCENTAGE);
		setWidth(100.0f, Unit.PERCENTAGE);
		
		setColumns(columns);
		setSelectionMode(SelectionMode.NONE);
		setContainerDataSource(gpcontainer);
		
		String messagePrefix = domains.getTables().get(clazz.getSimpleName()).getVt().messagePrefix();
		
		for(String cn : columns){
			Grid.Column col = getColumn(cn);
			col.setSortable(foundColumn(sortableColumns, cn));
			col.setHeaderCaption(MsgUtil.getFieldMsg(messageSource, messagePrefix, (String)cn));
			setupColumn(col, cn);
		}

		TextField filterField = new TextField();
		filterField.setWidth(100.0f, Unit.PERCENTAGE);

		HeaderRow groupingHeader = appendHeaderRow();
		
        // Add a summary footer row to the Grid
        FooterRow footer = addFooterRowAt(0);
        setSummaryFooterCells(footer);
 
        // Allow column reordering
        setColumnReorderingAllowed(true);
 
        // Allow column hiding
        for (Column c : getColumns()) {
        	c.setHidable(true);
        }

		HeaderCell namesCell = groupingHeader.join(columns);
		namesCell.setComponent(filterField);
		
		filterField.addTextChangeListener(change -> {
			// Can't modify filters so need to replace
			gpcontainer.removeAllContainerFilters();
			// (Re)create the filter if necessary
			gpcontainer.addContainerFilter(new SimpleStringFilter("", change.getText(), true, false));

		});
	}
	
    private void setColumnFiltering(boolean filtered) {
        if (filtered) {
        	HeaderRow hr  = appendHeaderRow();
        	
            TextField filter = getColumnFilter(columnId);
            filteringHeader.getCell(columnId).setComponent(filter);
            filteringHeader.getCell(columnId).setStyleName("filter-header");
        } else if (!filtered && filteringHeader != null) {
            sample.removeHeaderRow(filteringHeader);
            filteringHeader = null;
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
                filter = new SimpleStringFilter(columnId, event.getText(),
                        true, true);
                f.addContainerFilter(filter);
                cancelEditor();
            }
        });
        return filter;
    }
    
//    private void setSummaryFooterCells(FooterRow footer) {
//        for (int i = 0; i < numberOfYearHalves; i++) {
//            String propertyId = ExampleUtil.getBudgetPeriodPropertyId(i);
// 
//            // Loop items in the data source to calculate sums
//            double sum = 0;
//            for (Object itemId : sample.getContainerDataSource().getItemIds()) {
//                BigDecimal value = (BigDecimal) sample.getContainerDataSource()
//                        .getItem(itemId).getItemProperty(propertyId).getValue();
//                sum += value.doubleValue();
//            }
// 
//            // Use the same converter as the column values use
//            footer.getCell(propertyId).setText(
//                    ((DollarConverter) sample.getColumn(propertyId)
//                            .getConverter()).convertToPresentation(
//                            new BigDecimal(sum), String.class,
//                            sample.getLocale()));
// 
//            // Align the footer text
//            footer.getCell(propertyId).setStyleName("align-right");
//        }
//    }
 
	
	protected abstract void setupColumn(Column col, String cn);

	private boolean foundColumn(String[] columns, String column) {
		for(String col : columns) {
			if (col.equals(column)) {
				return true;
			}
		}
		return false;
	}

	protected abstract void addGeneratedProperty(GeneratedPropertyContainer gpcontainer, String name);
}
