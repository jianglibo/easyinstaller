package com.jianglibo.vaadin.dashboard.view.clustersoftware;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.vaadin.maddon.ListContainer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.yaml.snakeyaml.scanner.ScannerException;
import com.google.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.service.AppObjectMappers;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase.PropertyIdAndField;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBaseFree;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextArea;

@SuppressWarnings("serial")
public class InstallNewSoftwareForm extends FormBaseFree<InstallNewSoftwareVo>{
	
	private ComboBox softwareCb;
	
	private ComboBox actionCb;
	
	private TextArea othersField;
	
	private TextArea actionParameterTpl;
	
	private Software software;
	
	private AppObjectMappers appObjectMappers;
	
	private ActionParameters actionParameters;
	
	private List<Software> softwares;

	public InstallNewSoftwareForm(PersonRepository personRepository,
			MessageSource messageSource, Domains domains, FieldFactories fieldFactories,AppObjectMappers appObjectMappers,List<Software> softwares) {
		super(InstallNewSoftwareVo.class, personRepository, messageSource, domains, fieldFactories);
		this.appObjectMappers = appObjectMappers;
		this.softwares = softwares;
		delayCreateContent();
	}

	@Override
	protected List<PropertyIdAndField> buildFields() {
		List<PropertyIdAndField> fields = Lists.newArrayList();
		fields.add(createSoftwareField());
		fields.add(createActionField());
		fields.add(createOthersField());
		fields.add(createDescriptionField());
		return fields;
	}
	
	
	private PropertyIdAndField createDescriptionField() {
		actionParameterTpl = new TextArea(MsgUtil.getMsgWithSubsReturnKeyOnAbsent(messageSource, "view.clustersoftware.form.actionpatpl"));
		actionParameterTpl.setRows(4);
		return new PropertyIdAndField("actionParameterTpl", actionParameterTpl);
	}

	private PropertyIdAndField createOthersField() {
		othersField = new TextArea(MsgUtil.getMsgWithSubsReturnKeyOnAbsent(messageSource, "view.clustersoftware.form.others"));
		othersField.setRows(4);
		String desc = MsgUtil.getMsgWithSubsReturnKeyOnAbsent(messageSource, "view.clustersoftware.form.desc.others");
		if (!desc.equals("view.clustersoftware.form.desc.others")) {
			othersField.setDescription(desc);
		}
		return new PropertyIdAndField("others", othersField);
	}

	private PropertyIdAndField createActionField() {
		actionCb = new ComboBox(MsgUtil.getMsgWithSubsReturnKeyOnAbsent(messageSource, "view.clustersoftware.form.action"));
		actionCb.setNewItemsAllowed(false);
		
		if (softwareCb != null) {
			software = (Software)softwareCb.getValue();
			if (software != null) {
				actionParameters = new ActionParameters(appObjectMappers, software.getActionDescriptions(), software.getCodeLineSeperator());
			}
			setActionCbItems();
		}
		
		actionCb.addValueChangeListener(event -> {
			String action = (String) event.getProperty().getValue();
			if (software != null) {
				String actionDesc = actionParameters.getParameterYmlStr(action);
				if (actionDesc != null) {
					actionParameterTpl.setValue(actionDesc);
				} else {
					actionParameterTpl.setValue("");
				}
			}
		});
		
		return new PropertyIdAndField("action", actionCb);
	}
	
	private void setActionCbItems() {
		if (software != null) {
			actionCb.removeAllItems();
			String[] acs = software.getActions().split(",");
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
//		AllSoftwareContainer fc = new AllSoftwareContainer(domains, 10, Lists.newArrayList());
		softwareCb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		softwareCb.setItemCaptionPropertyId("displayName");
		ListContainer<Software> lc = new ListContainer<>(softwares);
		softwareCb.setContainerDataSource(lc);
		softwareCb.setPageLength(10);
		
		softwareCb.addValueChangeListener(event -> {
			software = (Software) event.getProperty().getValue();
			actionParameters = new ActionParameters(appObjectMappers, software.getActionDescriptions(), software.getCodeLineSeperator());
			setActionCbItems();
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
	
	public String getOthers() throws JsonParseException, JsonMappingException, JsonProcessingException,ScannerException, IOException {
		String s= (String) othersField.getValue();
		if (actionParameters != null) {
			return actionParameters.convertToServerNeeds(s);
		}
		return s.trim();
	}

	@Override
	public boolean saveToRepo() {
		return false;
	}

	public TextArea getOthersField() {
		return othersField;
	}

	public void setOthersField(TextArea othersField) {
		this.othersField = othersField;
	}
	
	public TextArea getActionParameterTpl() {
		return actionParameterTpl;
	}

	public void setActionParameterTpl(TextArea actionParameterTpl) {
		this.actionParameterTpl = actionParameterTpl;
	}

}
