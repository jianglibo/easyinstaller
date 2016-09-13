package com.jianglibo.vaadin.dashboard.view.dashboard;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jianglibo.vaadin.dashboard.util.HttpPageGetter.NewNew;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public final class DevelopeNewsTable extends Table {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DevelopeNewsTable.class);

	public DevelopeNewsTable(List<NewNew> news) {
		setCaption("Develope News");

		addStyleName(ValoTheme.TABLE_BORDERLESS);
		addStyleName(ValoTheme.TABLE_NO_STRIPES);
		addStyleName(ValoTheme.TABLE_NO_VERTICAL_LINES);
		addStyleName(ValoTheme.TABLE_SMALL);
		setSortEnabled(false);

		

		setRowHeaderMode(RowHeaderMode.INDEX);
		setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
		setSizeFull();

		addContainerProperty("title", Link.class, null);
		addContainerProperty("datetime", String.class, null);
		setColumnAlignment("datetime", Align.RIGHT);
		int i = 0;
		for(NewNew n: news) {
			LOGGER.info(n.toString());
			Link link = new Link(n.getTitle(), new ExternalResource(n.getUrl()));
			link.setTargetName("_blank");
			addItem(new Object[] { link, n.getDatetime()}, i++);
		}

		// for (int i=0; i<100; i++) {
		// // Create the fields for the current table row
		// Label sumField = new Label(String.format(
		// "Sum is <b>$%04.2f</b><br/><i>(VAT incl.)</i>",
		// new Object[] {new Double(Math.random()*1000)}),
		// ContentMode.HTML);
		// CheckBox transferredField = new CheckBox("is transferred");
		//
		// // Multiline text field. This required modifying the
		// // height of the table row.
		// TextArea commentsField = new TextArea();
		// commentsField.setRows(3);
		//
		// // The Table item identifier for the row.
		// Integer itemId = new Integer(i);
		//
		// // Create a button and handle its click. A Button does not
		// // know the item it is contained in, so we have to store the
		// // item ID as user-defined data.
		// Button detailsField = new Button("show details");
		// detailsField.setData(itemId);
		// detailsField.addClickListener(new Button.ClickListener() {
		// public void buttonClick(ClickEvent event) {
		// // Get the item identifier from the user-defined data.
		// Integer iid = (Integer)event.getButton().getData();
		// Notification.show("Link " +
		// iid.intValue() + " clicked.");
		// }
		// });
		// detailsField.addStyleName("link");
		//
		// // Create the table row.
		// addItem(new Object[] {sumField, transferredField,
		// commentsField, detailsField},
		// itemId);
		// }

		// List<MovieRevenue> movieRevenues = new ArrayList<MovieRevenue>(
		// DashboardUI.getDataProvider().getTotalMovieRevenues());
		// Collections.sort(movieRevenues, new Comparator<MovieRevenue>() {
		// @Override
		// public int compare(final MovieRevenue o1, final MovieRevenue o2) {
		// return o2.getRevenue().compareTo(o1.getRevenue());
		// }
		// });
		//
		// setContainerDataSource(new BeanItemContainer<MovieRevenue>(
		// MovieRevenue.class, movieRevenues.subList(0, 10)));
		//
//		setVisibleColumns("title", "datetime");
		// setColumnHeaders("Title", "Revenue");
		// setColumnExpandRatio("title", 2);
		// setColumnExpandRatio("revenue", 1);
		//
		// setSortContainerPropertyId("revenue");
		// setSortAscending(false);
	}

}
