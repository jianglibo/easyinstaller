package com.jianglibo.vaadin.dashboard.view.boxgroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.BoxGroup;
import com.jianglibo.vaadin.dashboard.repositories.BoxGroupRepository;
import com.jianglibo.vaadin.dashboard.repositories.BoxRepository;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase.HandMakeFieldsListener;
import com.jianglibo.vaadin.dashboard.uicomponent.twingrid2.BoxTwinGridField;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;
import com.jianglibo.vaadin.dashboard.view.BaseEditView;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Field;


@SpringView(name = BoxGroupEditView.VIEW_NAME)
public class BoxGroupEditView  extends BaseEditView<BoxGroup, FormBase<BoxGroup>, JpaRepository<BoxGroup,Long>>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(BoxGroupEditView.class);

	public static final String VIEW_NAME = BoxGroupListView.VIEW_NAME + "/edit";
	
	private final BoxRepository boxRepository;
	
	private final PersonRepository personRepository;

	@Autowired
	public BoxGroupEditView(BoxRepository boxRepository,PersonRepository personRepository, BoxGroupRepository repository,MessageSource messageSource,Domains domains,FieldFactories fieldFactories,
			ApplicationContext applicationContext) {
		super(messageSource,BoxGroup.class, domains, fieldFactories, repository);
		this.boxRepository = boxRepository;
		this.personRepository = personRepository;
		delayCreateContent();
	}


	@Override
	public Field<?> createField(VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw) {
		switch (vffw.getName()) {
		case "boxes":
			return new BoxTwinGridField(getDomains(),getMessageSource(), boxRepository, vtw, vffw);
		default:
			break;
		}
		return null;
	}

	@Override
	protected FormBase<BoxGroup> createForm(MessageSource messageSource, Domains domains, FieldFactories fieldFactories,
			JpaRepository<BoxGroup, Long> repository,HandMakeFieldsListener handMakeFieldsListener) {
		return new BoxGroupForm(personRepository, getMessageSource(), getDomains(), fieldFactories, (BoxGroupRepository) repository, boxRepository, handMakeFieldsListener);
	}

	@Override
	protected BoxGroup createNewBean() {
		return new BoxGroup();
	}

	@Override
	protected String getListViewName() {
		return VIEW_NAME;
	}
}
