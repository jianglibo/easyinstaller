package com.jianglibo.vaadin.dashboard.uifactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.GlobalComboOptions;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.ComboBoxBackByContainer;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.ComboBoxBackByJpql;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.ComboBoxBackByStringOptions;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.ComboBoxBackByYaml;
import com.jianglibo.vaadin.dashboard.config.ApplicationConfig;
import com.jianglibo.vaadin.dashboard.config.ComboItem;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.repositories.RepositoryCommonCustom;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.ComboBox;


@Component
public class ComboBoxFieldFactory {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ComboBoxFieldFactory.class);

	private final MessageSource messageSource;
	
	private final ApplicationConfig appConfig;
	
	@Autowired
	private Domains domains;
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private GlobalComboOptions comboOptions;
	
	
	@Autowired
	public ComboBoxFieldFactory(MessageSource messageSource, ApplicationConfig appConfig) {
		this.messageSource = messageSource;
		this.appConfig = appConfig;
	}
	
	public ComboBox create(VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw) {
		
		String caption = null;
		try {
			caption = MsgUtil.getFieldMsg(messageSource, vtw.getVt().messagePrefix(), vffw);
		} catch (NoSuchMessageException e) {
		}

		ComboBox cb = new ComboBox(caption);
		
		cb.setNewItemsAllowed(vffw.getVff().newItemAllowed());
		cb.setItemCaptionMode(
			    ItemCaptionMode.EXPLICIT_DEFAULTS_ID);
		
		Field field = vffw.getReflectField();
		
		if (field.isAnnotationPresent(ComboBoxBackByContainer.class)) {
			ComboBoxBackByContainer cbbbc = field.getAnnotation(ComboBoxBackByContainer.class);
			return buildContainerCombox(cbbbc, vtw, vffw, cb);
		} else if (field.isAnnotationPresent(ComboBoxBackByYaml.class)) {
			ComboBoxBackByYaml cbbby = field.getAnnotation(ComboBoxBackByYaml.class);
			return buildYmlCombox(cbbby, vtw, vffw, cb);
		} else if (field.isAnnotationPresent(ComboBoxBackByJpql.class)) {
			ComboBoxBackByJpql cbbbj = field.getAnnotation(ComboBoxBackByJpql.class);
			return buildJpqlCombox(cbbbj, vtw, vffw, cb);
		} else if (field.isAnnotationPresent(ComboBoxBackByStringOptions.class)){
			ComboBoxBackByStringOptions cbbbo = field.getAnnotation(ComboBoxBackByStringOptions.class);
			return buildStringOptionCombox(cbbbo, vtw, vffw, cb);
		} else {
			return null;
		}
		
	}
	
	private ComboBox buildStringOptionCombox(ComboBoxBackByStringOptions cbbbo, VaadinTableWrapper vtw,
			VaadinFormFieldWrapper vffw, ComboBox cb) {
		Set<String> options = comboOptions.getStringOptions(cbbbo.key());
		cb.addItems(options);
		return cb;
	}

	private ComboBox buildJpqlCombox(ComboBoxBackByJpql cbbbj, VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw,
			ComboBox cb) {
		String jpql = cbbbj.jpql();
		List<? extends BaseEntity> results = entityManager.createQuery(jpql, BaseEntity.class).getResultList();
		
		for(BaseEntity be : results) {
			cb.addItem(be);
			cb.setItemCaption(be, be.getDisplayName());
		}
		return cb;
	}

	private ComboBox buildYmlCombox(ComboBoxBackByYaml cbbby, VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw, ComboBox cb) {
		List<ComboItem> comboItems = appConfig.getComboDatas().get(cbbby.ymlKey());
		
		if (comboItems == null) {
			LOGGER.warn("comboitem key '{}' does not exists in application.yml under common-datas.", cbbby.ymlKey());
			comboItems = Lists.newArrayList();
		}
		
		for(ComboItem ci : comboItems) {
			Object v = convertItemValue(ci);
			cb.addItem(v);
			cb.setItemCaption(v, MsgUtil.getComboItemMsg(messageSource, cbbby.ymlKey(), ci));
		}
		return cb;
	}
	
	private ComboBox buildContainerCombox(ComboBoxBackByContainer cbbbc, VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw, ComboBox cb) {
		RepositoryCommonCustom rcc = domains.getRepositoryCommonCustom(cbbbc.entityClass().getSimpleName());
		Sort defaultSort = domains.getDefaultSort(cbbbc.entityClass());
		FreeContainer<? extends BaseEntity> fc = new FreeContainer<>(rcc, defaultSort, cbbbc.entityClass(),cbbbc.pageLength(), vtw.getSortableContainerPropertyIds());
		cb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cb.setItemCaptionPropertyId(cbbbc.itemCaptionPropertyId());
		cb.setContainerDataSource(fc);
		cb.setPageLength(cbbbc.pageLength());
		return cb;
	}

	public Object convertItemValue(ComboItem ci) {
		switch (ci.getValueType()) {
		case "Integer":
			return Integer.valueOf(ci.getValue());
		case "Long":
			return Long.valueOf(ci.getValue());
		default:
			return ci.getValue();
		}
	}
}
