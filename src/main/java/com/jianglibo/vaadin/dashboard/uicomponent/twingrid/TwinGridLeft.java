package com.jianglibo.vaadin.dashboard.uicomponent.twingrid;

import java.util.Collection;

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
import com.jianglibo.vaadin.dashboard.event.ui.TwinGridFieldItemClickListener;
import com.vaadin.data.Item;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;

@SuppressWarnings("serial")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TwinGridLeft<T extends Collection<? extends BaseEntity>> extends VerticalLayout {
	
	private static final String REMOVE_FROM_LEFT = "removeFromLeft";
	
	@Autowired
	private Domains domains;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	private TwinGridFieldItemClickListener itemClickListener;
	
	private FreeContainer freeContainer;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TwinGridLeft<T> afterInjection(VaadinFormFieldWrapper vffw, TwinGridFieldDescription tgfd, TwinGridLayout<T> tgl) {
		
		freeContainer = applicationContext.getBean(FreeContainer.class).afterInjection(tgfd.leftClazz(), tgfd.leftPageLength());
		
		GeneratedPropertyContainer gpcontainer = new GeneratedPropertyContainer(freeContainer);

		gpcontainer.addGeneratedProperty(REMOVE_FROM_LEFT, new PropertyValueGenerator<String>() {
			@Override
			public String getValue(Item item, Object itemId, Object propertyId) {
				return FontAwesome.MINUS_SQUARE_O.getHtml();
			}

			@Override
			public Class<String> getType() {
				return String.class;
			}
		});
		
		VaadinTableWrapper vtw = domains.getTables().get(tgfd.leftClazz().getSimpleName());
		
		String[] allcolnames = tgfd.leftColumns();
		
		GridMeta gridMeta = VaadinGridUtil.setupGrid(applicationContext, allcolnames, messageSource, vtw, tgfd.leftClazz(), REMOVE_FROM_LEFT);
		Grid grid = gridMeta.getGrid();
		grid.setContainerDataSource(gpcontainer);
		addComponent(grid);
		
		Grid.Column removeFromLeftColumn = grid.getColumn(REMOVE_FROM_LEFT);

		removeFromLeftColumn.setRenderer(new HtmlRenderer());
		removeFromLeftColumn.setHeaderCaption("");
		grid.addItemClickListener(event -> {
			if (event.getPropertyId().equals(REMOVE_FROM_LEFT)) {
				itemClickListener.itemClicked(new TwinGridFieldItemClickEvent(event.getItemId(), true));
				tgl.refreshValue();
			}
		});
		return this;
	}

	public void addItemClickListener(TwinGridFieldItemClickListener itemClickListener) {
		this.itemClickListener = itemClickListener;
	}

	public FreeContainer getFreeContainer() {
		return freeContainer;
	}
}
