package com.jianglibo.vaadin.dashboard.uicomponent.dynmenu;

import java.util.Map;

import org.springframework.context.MessageSource;

import com.google.gwt.thirdparty.guava.common.collect.Maps;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;

@SuppressWarnings("serial")
public class DynButtonComponent extends HorizontalLayout implements ClickListener {
	
	private Map<String, ButtonDescription> btnDescriptionMap = Maps.newHashMap();
	
	private Map<String, Button> itemMap = Maps.newHashMap();
	
	private DynaMenuItemClickListener dynaMenuItemClickListener;
	
	public DynButtonComponent(MessageSource messageSource, ButtonGroup...groups) {
		MarginInfo mf = new MarginInfo(false, false, false, true);
		setMargin(mf);
		addStyleName("dyn-menu");
        HorizontalLayout btgHl = new HorizontalLayout();
        btgHl.setSpacing(true);
        for(ButtonGroup btg: groups) {
        	HorizontalLayout hl = new HorizontalLayout();
        	for(ButtonDescription btnDesc: btg.getButtons()) {
            	btnDescriptionMap.put(btnDesc.getItemId(), btnDesc);
            	String msg = MsgUtil.getDynaMenuMsg(messageSource, btnDesc.getItemId());
            	Button bt;
            	if (btnDesc.getIcon() == null) {
            		bt = new Button(msg);
            	} else {
            		bt = new Button(btnDesc.getIcon());
            		bt.setDescription(msg);
            	}
            	bt.addClickListener(this);
            	bt.setData(btnDesc);
            	itemMap.put(btnDesc.getItemId(), bt);
            	hl.addComponent(bt);
        	}
        	btgHl.addComponent(hl);
        }
        addComponent(btgHl);
        onSelectionChange(0);
	}
	
	public void AddDynaMenuItemClickListener(DynaMenuItemClickListener dynaMenuItemClickListener) {
		this.dynaMenuItemClickListener = dynaMenuItemClickListener;
	}
	
	public static interface DynaMenuItemClickListener {
		void dynaMenuItemClicked(ButtonDescription btdsc);
	}
	
	public void onSelectionChange(int num) {
		itemMap.forEach((k, v) -> {
			boolean enabled = btnDescriptionMap.get(k).isEnabled(num);
			if (enabled) {
				StyleUtil.show(v);
			} else {
				StyleUtil.hide(v);
			}
		});
	}

	@Override
	public void buttonClick(ClickEvent event) {
		ButtonDescription btDesc = (ButtonDescription) event.getButton().getData();
		getDynaMenuItemClickListener().dynaMenuItemClicked(btDesc);
	}

	public DynaMenuItemClickListener getDynaMenuItemClickListener() {
		return dynaMenuItemClickListener;
	}
}
