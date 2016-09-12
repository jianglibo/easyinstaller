package com.jianglibo.vaadin.dashboard.view.kvv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Kkv;
import com.jianglibo.vaadin.dashboard.repositories.KkvRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase.HandMakeFieldsListener;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;
import com.jianglibo.vaadin.dashboard.view.BaseEditView;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Field;


@SpringView(name = KkvEditView.VIEW_NAME)
public class KkvEditView  extends BaseEditView<Kkv, FormBase<Kkv>, JpaRepository<Kkv,Long>>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(KkvEditView.class);

	public static final String VIEW_NAME = KkvListView.VIEW_NAME + "/edit";

	@Autowired
	public KkvEditView(KkvRepository repository,MessageSource messageSource,Domains domains,FieldFactories fieldFactories,
			ApplicationContext applicationContext) {
		super(messageSource,Kkv.class, domains, fieldFactories, repository);
	}


	@Override
	public Field<?> createField(VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw) {
		return null;
	}

	@Override
	protected FormBase<Kkv> createForm(MessageSource messageSource, Domains domains, FieldFactories fieldFactories,
			JpaRepository<Kkv, Long> repository,HandMakeFieldsListener handMakeFieldsListener) {
		return new KkvForm(getMessageSource(), getDomains(), fieldFactories, (KkvRepository) repository, handMakeFieldsListener);
	}

	@Override
	protected Kkv createNewBean() {
		return new Kkv();
	}

	@Override
	protected String getListViewName() {
		return VIEW_NAME;
	}
}
