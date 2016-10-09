package com.jianglibo.vaadin.dashboard.view.clustersoftware;

import java.util.List;
import java.util.Optional;

import org.springframework.context.MessageSource;

import com.google.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase.PropertyIdAndField;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBaseFree;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.ComboBox;

@SuppressWarnings("serial")
public class InstallNewSoftwareForm extends FormBaseFree<InstallNewSoftwareVo>{
	
	private ComboBox softwareCb;
	
	private ComboBox actionCb;

	public InstallNewSoftwareForm(PersonRepository personRepository,
			MessageSource messageSource, Domains domains, FieldFactories fieldFactories) {
		super(InstallNewSoftwareVo.class, personRepository, messageSource, domains, fieldFactories);
		delayCreateContent();
	}

	@Override
	protected List<PropertyIdAndField> buildFields() {
		List<PropertyIdAndField> fields = Lists.newArrayList();
		fields.add(createSoftwareField());
		fields.add(createActionField());
		return fields;
	}
	
	

	private PropertyIdAndField createActionField() {
		actionCb = new ComboBox(MsgUtil.getMsgWithSubsReturnKeyOnAbsent(messageSource, "view.clustersoftware.form.action"));
		actionCb.setNewItemsAllowed(false);
		
		if (softwareCb != null) {
			Software sf = (Software)softwareCb.getValue();
			setActionCbItems(sf);
		}
		return new PropertyIdAndField("action", actionCb);
	}
	
	private void setActionCbItems(Software sf) {
		if (sf != null) {
			actionCb.removeAllItems();
			String[] acs = sf.getActions().split(",");
			if (acs.length == 0) {
				acs = new String[]{"install"};
			}
			for(String ac : acs) {
				actionCb.addItem(ac);
				actionCb.setItemCaption(ac, MsgUtil.getMsgWithSubsReturnLastKeyPartOnAbsent(messageSource, "software.action." + ac));
			}
		}
	}

	private PropertyIdAndField createSoftwareField() {
		softwareCb = new ComboBox(MsgUtil.getMsgWithSubsReturnKeyOnAbsent(messageSource, "view.clustersoftware.form.software"));
		softwareCb.setNewItemsAllowed(false);
		AllSoftwareContainer fc = new AllSoftwareContainer(domains, 10, Lists.newArrayList());
		softwareCb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		softwareCb.setItemCaptionPropertyId("displayName");
		softwareCb.setContainerDataSource(fc);
		softwareCb.setPageLength(10);
		
		softwareCb.addValueChangeListener(event -> {
			Software sw = (Software) event.getProperty().getValue();
			setActionCbItems(sw);
		});
		return new PropertyIdAndField("software", softwareCb);
	}
	
	public Optional<Software> getSelectedSoftware() {
		Software sf = (Software)softwareCb.getValue();
		if (sf == null) {
			return Optional.empty();
		} else {
			return Optional.of(sf);
		}
	}
	
	public Optional<String> getSelectedAction() {
		String s= (String) actionCb.getValue();
		if (s == null || s.trim().isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(s);
		}
	}

	@Override
	public boolean saveToRepo() {
		return false;
	}

}
