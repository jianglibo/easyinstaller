package com.jianglibo.vaadin.dashboard.window.localeselector;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.vaadin.maddon.FilterableListContainer;

import com.jianglibo.vaadin.dashboard.DashboardUI;
import com.jianglibo.vaadin.dashboard.component.MovieDetailsWindow;
import com.jianglibo.vaadin.dashboard.domain.Transaction;
import com.jianglibo.vaadin.dashboard.event.DashboardEventBus;
import com.jianglibo.vaadin.dashboard.wrapper.Wrapper;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.Action;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ClassResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.TableDragMode;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@Scope("prototype")
public class LocaleSelector implements Wrapper<Label> {

	@Autowired
	private MessageSource messageSource;

	private static final DateFormat DATEFORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
	private static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#.##");

	private Label label = new Label();
	
	private Table table;

	@Override
	public Label unwrap() {
		Locale lo = VaadinSession.getCurrent().getLocale();
		final ClassResource flagResource = new ClassResource("/com/jianglibo/vaadin/dashboard/icons/flags/China.png");
		label.setSizeUndefined();
		label.setIcon(flagResource);

		final Window window = new Window("Select Language");
		window.setWidth(300.0f, Unit.PIXELS);
		
		window.setContent(buildWindowContent());

		return label;
	}
	
	private Component buildWindowContent() {
		VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.addStyleName("transactions");
        DashboardEventBus.register(this);
        content.addComponent(buildToolbar());

        Table table = buildTable();
        content.addComponent(table);
        content.setExpandRatio(table, 1);
		return content;
	}
	
    private Component buildToolbar() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);
        Responsive.makeResponsive(header);

        Label title = new Label("Select Language");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(title);
        HorizontalLayout tools = new HorizontalLayout(buildFilter());
        tools.setSpacing(true);
        tools.addStyleName("toolbar");
        header.addComponent(tools);

        return header;
    }
    
    private boolean filterByProperty(final String prop, final Item item,
            final String text) {
        if (item == null || item.getItemProperty(prop) == null
                || item.getItemProperty(prop).getValue() == null) {
            return false;
        }
        String val = item.getItemProperty(prop).getValue().toString().trim()
                .toLowerCase();
        if (val.contains(text.toLowerCase().trim())) {
            return true;
        }
        return false;
    }
    
    private Component buildFilter() {
        final TextField filter = new TextField();
        filter.addTextChangeListener(new TextChangeListener() {
            @Override
            public void textChange(final TextChangeEvent event) {
                Filterable data = (Filterable) table.getContainerDataSource();
                data.removeAllContainerFilters();
                data.addContainerFilter(new Filter() {
                    @Override
                    public boolean passesFilter(final Object itemId,
                            final Item item) {

                        if (event.getText() == null
                                || event.getText().equals("")) {
                            return true;
                        }

                        return filterByProperty("country", item,
                                event.getText())
                                || filterByProperty("city", item,
                                        event.getText())
                                || filterByProperty("title", item,
                                        event.getText());

                    }

                    @Override
                    public boolean appliesToProperty(final Object propertyId) {
                        if (propertyId.equals("country")
                                || propertyId.equals("city")
                                || propertyId.equals("title")) {
                            return true;
                        }
                        return false;
                    }
                });
            }
        });

        filter.setInputPrompt("Filter");
        filter.setIcon(FontAwesome.SEARCH);
        filter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        filter.addShortcutListener(new ShortcutListener("Clear",
                KeyCode.ESCAPE, null) {
            @Override
            public void handleAction(final Object sender, final Object target) {
                filter.setValue("");
                ((Filterable) table.getContainerDataSource())
                        .removeAllContainerFilters();
            }
        });
        return filter;
    }
    

	private Table buildTable() {
		final Table table = new Table() {
			@Override
			protected String formatPropertyValue(final Object rowId, final Object colId, final Property<?> property) {
				String result = super.formatPropertyValue(rowId, colId, property);
				if (colId.equals("time")) {
					result = DATEFORMAT.format(((Date) property.getValue()));
				} else if (colId.equals("price")) {
					if (property != null && property.getValue() != null) {
						return "$" + DECIMALFORMAT.format(property.getValue());
					} else {
						return "";
					}
				}
				return result;
			}
		};
		table.setSizeFull();
		table.addStyleName(ValoTheme.TABLE_BORDERLESS);
		table.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
		table.addStyleName(ValoTheme.TABLE_COMPACT);
		table.setSelectable(true);

		table.setColumnCollapsingAllowed(true);
		table.setColumnCollapsible("time", false);
		table.setColumnCollapsible("price", false);

		table.setColumnReorderingAllowed(true);
		table.setContainerDataSource(
				new CountryContainer(DashboardUI.getDataProvider().getRecentTransactions(200)));
		table.setSortContainerPropertyId("time");
		table.setSortAscending(false);

		table.setColumnAlignment("seats", Align.RIGHT);
		table.setColumnAlignment("price", Align.RIGHT);

		table.setVisibleColumns("time", "country", "city", "theater", "room", "title", "seats", "price");
		table.setColumnHeaders("Time", "Country", "City", "Theater", "Room", "Title", "Seats", "Price");

		table.setFooterVisible(true);
		table.setColumnFooter("time", "Total");

		table.setColumnFooter("price", "$" + DECIMALFORMAT.format(DashboardUI.getDataProvider().getTotalSum()));

		// Allow dragging items to the reports menu
		table.setDragMode(TableDragMode.MULTIROW);
		table.setMultiSelect(true);

		table.addActionHandler(new TransactionsActionHandler());

		// table.addValueChangeListener(new ValueChangeListener() {
		// @Override
		// public void valueChange(final ValueChangeEvent event) {
		// if (table.getValue() instanceof Set) {
		// Set<Object> val = (Set<Object>) table.getValue();
		// createReport.setEnabled(val.size() > 0);
		// }
		// }
		// });
		table.setImmediate(true);

		return table;
	}

	@SuppressWarnings("serial")
	private class CountryContainer extends FilterableListContainer<Transaction> {

		public CountryContainer(final Collection<Transaction> collection) {
			super(collection);
		}

		// This is only temporarily overridden until issues with
		// BeanComparator get resolved.
		@Override
		public void sort(final Object[] propertyId, final boolean[] ascending) {
			final boolean sortAscending = ascending[0];
			final Object sortContainerPropertyId = propertyId[0];
			Collections.sort(getBackingList(), new Comparator<Transaction>() {
				@Override
				public int compare(final Transaction o1, final Transaction o2) {
					int result = 0;
					if ("time".equals(sortContainerPropertyId)) {
						result = o1.getTime().compareTo(o2.getTime());
					} else if ("country".equals(sortContainerPropertyId)) {
						result = o1.getCountry().compareTo(o2.getCountry());
					} else if ("city".equals(sortContainerPropertyId)) {
						result = o1.getCity().compareTo(o2.getCity());
					} else if ("theater".equals(sortContainerPropertyId)) {
						result = o1.getTheater().compareTo(o2.getTheater());
					} else if ("room".equals(sortContainerPropertyId)) {
						result = o1.getRoom().compareTo(o2.getRoom());
					} else if ("title".equals(sortContainerPropertyId)) {
						result = o1.getTitle().compareTo(o2.getTitle());
					} else if ("seats".equals(sortContainerPropertyId)) {
						result = new Integer(o1.getSeats()).compareTo(o2.getSeats());
					} else if ("price".equals(sortContainerPropertyId)) {
						result = new Double(o1.getPrice()).compareTo(o2.getPrice());
					}

					if (!sortAscending) {
						result *= -1;
					}
					return result;
				}
			});
		}
	}

	@SuppressWarnings("serial")
	private class TransactionsActionHandler implements Handler {
		private final Action report = new Action("Create Report");

		private final Action discard = new Action("Discard");

		private final Action details = new Action("Movie details");

		@Override
		public void handleAction(final Action action, final Object sender, final Object target) {
			if (action == report) {
//				createNewReportFromSelection();
			} else if (action == discard) {
				Notification.show("Not implemented in this demo");
			} else if (action == details) {
				Item item = ((Table) sender).getItem(target);
				if (item != null) {
					Long movieId = (Long) item.getItemProperty("movieId").getValue();
					MovieDetailsWindow.open(DashboardUI.getDataProvider().getMovie(movieId), null, null);
				}
			}
		}

		@Override
		public Action[] getActions(final Object target, final Object sender) {
			return new Action[] { details, report, discard };
		}
	}
}
