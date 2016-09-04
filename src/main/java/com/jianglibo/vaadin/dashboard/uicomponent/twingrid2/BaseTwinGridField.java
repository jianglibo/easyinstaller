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
import com.jianglibo.vaadin.dashboard.event.ui.TwinGridFieldItemClickEvent;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.renderers.HtmlRenderer;

/**
 * Every field need to know @VaadinFormFieldWrapper and @VaadinTableWrapper
 * 
 * Field's datasource has no relation to leftGrid's datasource.  
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
	private static final String LEFT_ICON_COL = "removeFromLeft";
	private static final String RIGHT_ICON_COL = "addToLeft";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseTwinGridField.class);

	private final Domains domains;

	private final MessageSource messageSource;

	private final Class<L> leftClazz;

	private final Class<R> rightClazz;

	private String[] leftColumnNames;
	
	private String[] rightColumnNames;
	
	private Component fieldContentToReturn;
	
	private VaadinFormFieldWrapper vffw;
	
	private Grid leftGrid;
	
	private Grid rightGrid;
	
	private MyValueChangeListener vc;
	
	public BaseTwinGridField(Class<L> leftClazz, String[] leftColumnNames, Class<R> rightClazz, String[] rightColumnNames, Domains domains, MessageSource messageSource) {
		this.leftClazz = leftClazz;
		this.rightClazz = rightClazz;
		this.leftColumnNames = leftColumnNames;
		this.rightColumnNames = rightColumnNames;
		this.domains = domains;
		this.messageSource = messageSource;
		vc = new MyValueChangeListener();
		addValueChangeListener(vc);
	}
	
	@Override
	protected Component initContent() {
		return fieldContentToReturn;
	}
	
	protected GeneratedPropertyContainer createLeftContainer(VaadinFormFieldWrapper vffw, List<L> lc) {
		AllowEmptySortListContainer<L> lcc = new AllowEmptySortListContainer<L>(leftClazz, lc);
		GeneratedPropertyContainer gpcontainer = new GeneratedPropertyContainer(lcc);

		gpcontainer.addGeneratedProperty(LEFT_ICON_COL, new PropertyValueGenerator<String>() {
			@Override
			public String getValue(Item item, Object itemId, Object propertyId) {
				return FontAwesome.MINUS_SQUARE_O.getHtml();
			}

			@Override
			public Class<String> getType() {
				return String.class;
			}
		});
		return gpcontainer;
	}
	
	protected GeneratedPropertyContainer createRightContainer(VaadinFormFieldWrapper vffw) {
		GeneratedPropertyContainer gpcontainer = new GeneratedPropertyContainer(new FreeContainer<>(domains, rightClazz, vffw.getExtraAnotation(TwinGridFieldDescription.class).rightPageLength()));

		gpcontainer.addGeneratedProperty(RIGHT_ICON_COL, new PropertyValueGenerator<String>() {
			@Override
			public String getValue(Item item, Object itemId, Object propertyId) {
				return FontAwesome.PLUS_SQUARE_O.getHtml();
			}

			@Override
			public Class<String> getType() {
				return String.class;
			}
		});
		return gpcontainer;
	}

	protected void buildTwinGridContent(VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw) {
		setVffw(vffw);
		HorizontalLayout hl = new HorizontalLayout();
		setWidth(100.0f, Unit.PERCENTAGE);
		hl.setWidth(100.0f, Unit.PERCENTAGE);
		
		setCaption(MsgUtil.getFieldMsg(messageSource, vtw.getVt().messagePrefix(), vffw));
		
		leftGrid = getOneGrid(leftClazz, LEFT_ICON_COL, createLeftContainer(vffw, Lists.newArrayList()), leftColumnNames, true);
		rightGrid = getOneGrid(rightClazz, RIGHT_ICON_COL, createRightContainer(vffw), rightColumnNames, false);
		leftGrid.setWidth(100.0f, Unit.PERCENTAGE);
		rightGrid.setWidth(100.0f, Unit.PERCENTAGE);

		hl.addComponent(leftGrid);
		hl.addComponent(rightGrid);

		hl.setExpandRatio(leftGrid, 1);
		hl.setExpandRatio(rightGrid, 1);
		fieldContentToReturn = hl;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<LC> getType() {
		return (Class<LC>) Collection.class;
	}

	private Grid getOneGrid(Class<?> clazz, String extraColName, GeneratedPropertyContainer gpcontainer,String[] colnames, boolean isLeft) {
		
		String[] allcolumns = new String[colnames.length + 1];
		allcolumns[0] = extraColName;
		for(int i =1;i < allcolumns.length ; i++) {
			allcolumns[i] = colnames[i - 1];
		}
		
		setWidth(100.0f, Unit.PERCENTAGE);

		TextField filterField = new TextField();
		filterField.setWidth(100.0f, Unit.PERCENTAGE);

		Grid grid = new Grid();
		grid.setColumns(allcolumns);
		grid.setSelectionMode(SelectionMode.NONE);
		for (String cn : allcolumns) {
			Grid.Column col = grid.getColumn(cn);
			col.setHeaderCaption(MsgUtil.getFieldMsg(messageSource,
					domains.getTables().get(clazz.getSimpleName()).getVt().messagePrefix(), cn));
		}

		HeaderRow groupingHeader = grid.appendHeaderRow();

		for (String s : allcolumns) {
			HeaderCell hc = groupingHeader.getCell(s);
			LOGGER.info("" + hc);

		}

		HeaderCell namesCell = groupingHeader.join(allcolumns);
		namesCell.setComponent(filterField);

		grid.setContainerDataSource(gpcontainer);

		Grid.Column addToLeftColumn = grid.getColumn(extraColName);

		addToLeftColumn.setRenderer(new HtmlRenderer());
		addToLeftColumn.setHeaderCaption("");

		filterField.addTextChangeListener(change -> {
			// Can't modify filters so need to replace
			gpcontainer.removeAllContainerFilters();
			// (Re)create the filter if necessary
			gpcontainer.addContainerFilter(new SimpleStringFilter("", change.getText(), true, false));

		});
		grid.addItemClickListener(event -> {
			if (event.getPropertyId().equals(extraColName)) {
				BaseTwinGridField.this.itemClicked(new TwinGridFieldItemClickEvent(event.getItemId(), isLeft));
				BaseTwinGridField.this.refreshValue();
			}
		});
		return grid;
	}
	
	public abstract void itemClicked(TwinGridFieldItemClickEvent twinGridFieldItemClickEvent);

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



	public Domains getDomains() {
		return domains;
	}
	
	@SuppressWarnings("serial")
	public class MyValueChangeListener implements ValueChangeListener {
		@Override
		public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
			BaseTwinGridField.this.leftGrid.setContainerDataSource(createLeftContainer(vffw, (List<L>) event.getProperty().getValue()));
		}
	}
}
