package com.jianglibo.vaadin.dashboard.uicomponent.twingrid2;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.TwinGridFieldDescription;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.event.ui.TwinGridFieldItemClickEvent;
import com.jianglibo.vaadin.dashboard.event.ui.TwinGridFieldItemClickListener;
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
 * @author jianglibo@gmail.com
 *
 * @param <LC>
 * @param <L>
 * @param <R>
 */
public class BaseTwinGridField<LC extends Collection<L>, L extends BaseEntity, R extends BaseEntity>
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

	private FreeContainer<L> leftContainer;

	private FreeContainer<R> rightContainer;

	private String[] leftColumnNames;
	
	private String[] rightColumnNames;
	
	private Component fieldContentToReturn;
	
	private VaadinFormFieldWrapper vffw;
	
	public BaseTwinGridField(Class<L> leftClazz, String[] leftColumnNames, Class<R> rightClazz, String[] rightColumnNames, Domains domains, MessageSource messageSource) {
		this.leftClazz = leftClazz;
		this.rightClazz = rightClazz;
		this.leftColumnNames = leftColumnNames;
		this.rightColumnNames = rightColumnNames;
		this.domains = domains;
		this.messageSource = messageSource;
	}

	/**
	 * I don't know when initContent will called.
	 */
	private TwinGridFieldItemClickListener itemClickListener;

	public void addItemClickListener(TwinGridFieldItemClickListener itemClickListener) {
		// this.itemClickListener = itemClickListener;
		// if (twinGridLayout != null) {
		// twinGridLayout.addItemClickListener(itemClickListener);
		// this.itemClickListener = null;
		// }
	}

//	public BaseTwinGridField<LC, L, R> afterInjection(VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw) {
//		this.vtw = vtw;
//		this.vffw = vffw;
//		return this;
//	}

	@Override
	protected Component initContent() {
		return fieldContentToReturn;
	}
	
	protected FreeContainer<L> createLeftContainer(VaadinFormFieldWrapper vffw) {
		return new FreeContainer<>(domains, leftClazz, vffw.getExtraAnotation(TwinGridFieldDescription.class).leftPageLength());
	}
	
	protected FreeContainer<R> createRightContainer(VaadinFormFieldWrapper vffw) {
		return new FreeContainer<>(domains, rightClazz, vffw.getExtraAnotation(TwinGridFieldDescription.class).rightPageLength());
	}

	protected void buildTwinGridContent(VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw) {
		setVffw(vffw);
		leftContainer = createLeftContainer(vffw);
		rightContainer = createRightContainer(vffw);
		HorizontalLayout hl = new HorizontalLayout();
		setWidth(100.0f, Unit.PERCENTAGE);
		hl.setWidth(100.0f, Unit.PERCENTAGE);
		
		setCaption(MsgUtil.getFieldMsg(messageSource, vtw.getVt().messagePrefix(), vffw));
		
		Grid left = getOneGrid(leftClazz, LEFT_ICON_COL, leftContainer, leftColumnNames, FontAwesome.MINUS_SQUARE_O.getHtml());
		Grid right = getOneGrid(rightClazz, RIGHT_ICON_COL, rightContainer, rightColumnNames, FontAwesome.PLUS_SQUARE_O.getHtml());
		left.setWidth(100.0f, Unit.PERCENTAGE);
		right.setWidth(100.0f, Unit.PERCENTAGE);

		hl.addComponent(left);
		hl.addComponent(right);

		hl.setExpandRatio(left, 1);
		hl.setExpandRatio(right, 1);
		fieldContentToReturn = hl;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<LC> getType() {
		return (Class<LC>) Collection.class;
	}

	@SuppressWarnings("serial")
	private Grid getOneGrid(Class<?> clazz, String extraColName, FreeContainer<?> freeComtainer,String[] colnames, String iconHtml) {
		
		String[] allcolumns = new String[colnames.length + 1];
		allcolumns[0] = extraColName;
		for(int i =1;i < allcolumns.length ; i++) {
			allcolumns[i] = colnames[i - 1];
		}
		
		setWidth(100.0f, Unit.PERCENTAGE);
		GeneratedPropertyContainer gpcontainer = new GeneratedPropertyContainer(freeComtainer);

		gpcontainer.addGeneratedProperty(extraColName, new PropertyValueGenerator<String>() {
			@Override
			public String getValue(Item item, Object itemId, Object propertyId) {
				return iconHtml;
			}

			@Override
			public Class<String> getType() {
				return String.class;
			}
		});

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
				itemClickListener.itemClicked(new TwinGridFieldItemClickEvent(event.getItemId(), false));
				BaseTwinGridField.this.refreshValue();
			}
		});
		return grid;
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

	public FreeContainer<L> getLeftContainer() {
		return leftContainer;
	}

	public void setLeftContainer(FreeContainer<L> leftContainer) {
		this.leftContainer = leftContainer;
	}

	public FreeContainer<R> getRightContainer() {
		return rightContainer;
	}

	public void setRightContainer(FreeContainer<R> rightContainer) {
		this.rightContainer = rightContainer;
	}
	
	
}
