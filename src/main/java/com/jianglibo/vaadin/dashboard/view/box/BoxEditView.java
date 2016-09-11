package com.jianglibo.vaadin.dashboard.view.box;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.repositories.BoxRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase.HandMakeFieldsListener;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;
import com.jianglibo.vaadin.dashboard.view.BaseEditView;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Field;


@SpringView(name = BoxEditView.VIEW_NAME)
public class BoxEditView  extends BaseEditView<Box, FormBase<Box>, BoxRepository>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(BoxEditView.class);
	
	public static final String VIEW_NAME = BoxListView.VIEW_NAME + "/edit";

	public static final FontAwesome ICON_VALUE = FontAwesome.FILE_ARCHIVE_O;
    
	@Autowired
	public BoxEditView(BoxRepository repository, MessageSource messageSource,Domains domains,FieldFactories fieldFactories,
			ApplicationContext applicationContext) {
		super(messageSource, domains, fieldFactories, repository);
	}

	public Field<?> createField(VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw) {
		return null;
	}

	@Override
	protected BoxForm createForm(MessageSource messageSource, Domains domains, FieldFactories fieldFactories,
			JpaRepository<Box, Long> repository, HandMakeFieldsListener handMakeFieldsListener) {
		return new BoxForm(messageSource, domains, fieldFactories, (BoxRepository) repository, handMakeFieldsListener);
	}

	@Override
	protected Box createNewBean() {
		return new Box();
	}

	@Override
	protected String getListViewName() {
		return BoxListView.VIEW_NAME;
	}
}
