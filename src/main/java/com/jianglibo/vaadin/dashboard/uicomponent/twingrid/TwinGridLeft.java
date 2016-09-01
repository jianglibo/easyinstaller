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
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TwinGridLeft<T extends Collection<? extends BaseEntity>> extends VerticalLayout {
	
	@Autowired
	private Domains domains;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private ApplicationContext applicationContext;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TwinGridLeft<T> afterInjection(VaadinFormFieldWrapper vffw, TwinGridFieldDescription tgfd) {
		FreeContainer fc = applicationContext.getBean(FreeContainer.class).afterInjection(tgfd.leftClazz(), tgfd.leftPageLength());
		
		VaadinTableWrapper vtw = domains.getTables().get(tgfd.leftClazz().getSimpleName());
		
		String[] allcolnames = tgfd.leftColumns();
		
		GridMeta gridMeta = VaadinGridUtil.setupColumns(applicationContext, allcolnames, messageSource, vtw);
		Grid grid = gridMeta.getGrid();
		grid.setContainerDataSource(fc);
		addComponent(grid);
		return this;
	}
}
