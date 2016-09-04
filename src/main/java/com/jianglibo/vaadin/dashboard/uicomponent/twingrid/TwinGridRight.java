package com.jianglibo.vaadin.dashboard.uicomponent.twingrid;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.TwinGridFieldDescription;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.data.vaadinconverter.VaadinGridUtil;
import com.jianglibo.vaadin.dashboard.data.vaadinconverter.VaadinGridUtil.GridMeta;
import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.event.ui.TwinGridFieldItemClickEvent;
import com.vaadin.data.Item;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;

@SuppressWarnings("serial")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TwinGridRight<T extends Collection<? extends BaseEntity>> extends VerticalLayout {

	private static Logger LOGGER = LoggerFactory.getLogger(TwinGridRight.class);

	private static final String ADD_TO_LEFT_COL_NAME = "addToLeft";

	private TextField filterField;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private Domains domains;

	@Autowired
	private MessageSource messageSource;
	
//	private TwinGridFieldItemClickListener itemClickListener;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public TwinGridRight<T> afterInjection(VaadinFormFieldWrapper vffw, TwinGridFieldDescription tgfd, TwinGridLayout<T> tgl) {
		setWidth(100.0f, Unit.PERCENTAGE);

		FreeContainer fc1 = new FreeContainer(domains, tgfd.rightClazz(),tgfd.rightPageLength());

		GeneratedPropertyContainer gpcontainer = new GeneratedPropertyContainer(fc1);

		gpcontainer.addGeneratedProperty(ADD_TO_LEFT_COL_NAME, new PropertyValueGenerator<String>() {
			@Override
			public String getValue(Item item, Object itemId, Object propertyId) {
				return FontAwesome.PLUS_SQUARE_O.getHtml();
			}

			@Override
			public Class<String> getType() {
				return String.class;
			}
		});

		filterField = new TextField();
		filterField.setWidth(100.0f, Unit.PERCENTAGE);
		filterField.setInputPrompt("filter");

		VaadinTableWrapper vtw = domains.getTables().get(tgfd.rightClazz().getSimpleName());

		String[] allcolnames = tgfd.rightColumns();

		GridMeta gridMeta = VaadinGridUtil.setupGrid(applicationContext, allcolnames, messageSource, vtw, tgfd.rightClazz(),
				ADD_TO_LEFT_COL_NAME);

		Grid grid = gridMeta.getGrid();

		HeaderRow groupingHeader = grid.appendHeaderRow();

		for (String s : gridMeta.getColnames()) {
			HeaderCell hc = groupingHeader.getCell(s);
			LOGGER.info("" + hc);

		}

		HeaderCell namesCell = groupingHeader.join(gridMeta.getColnames());
		namesCell.setComponent(filterField);

		grid.setContainerDataSource(gpcontainer);
		addComponent(grid);

		Grid.Column addToLeftColumn = grid.getColumn(ADD_TO_LEFT_COL_NAME);

		addToLeftColumn.setRenderer(new HtmlRenderer());
		addToLeftColumn.setHeaderCaption("");
		// addToLeftColumn.setWidth(15.0d);

		filterField.addTextChangeListener(change -> {
			// Can't modify filters so need to replace
			gpcontainer.removeAllContainerFilters();
			// (Re)create the filter if necessary

			gpcontainer.addContainerFilter(new SimpleStringFilter("", change.getText(), true, false));

		});
		grid.addItemClickListener(event -> {
			if (event.getPropertyId().equals(ADD_TO_LEFT_COL_NAME)) {
//				itemClickListener.itemClicked(new TwinGridFieldItemClickEvent(event.getItemId(), false));
				tgl.refreshValue();
			}
		});
		return this;
	}

//	public void addItemClickListener(TwinGridFieldItemClickListener itemClickListener) {
//		this.itemClickListener = itemClickListener;
//	}
}
