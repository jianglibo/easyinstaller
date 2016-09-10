package com.jianglibo.vaadin.dashboard.uicomponent.dynmenu;

import java.util.Map;

import org.springframework.context.MessageSource;

import com.google.common.eventbus.EventBus;
import com.google.gwt.thirdparty.guava.common.collect.Maps;
import com.jianglibo.vaadin.dashboard.event.view.DynMenuClickEvent;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.jianglibo.vaadin.dashboard.view.ListView;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;

@SuppressWarnings("serial")
public class DynButtonComponent extends HorizontalLayout implements ClickListener {
	
	private MessageSource messageSource;
	
	private Map<String, ButtonDescription> btnDescriptionMap = Maps.newHashMap();
	
	private Map<String, Button> itemMap = Maps.newHashMap();
	
	private ListView listview;
	
	public DynButtonComponent(MessageSource messageSource,ListView listview, ButtonGroup...groups) {
		this.listview = listview;
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
	
	
//	public DynButtonComponent afterInjection(ListView listview) {
//		MarginInfo mf = new MarginInfo(false, false, false, true);
//		setMargin(mf);
//		addStyleName("dyn-menu");
//        HorizontalLayout btgHl = new HorizontalLayout();
//        btgHl.setSpacing(true);
//        for(ButtonGroup btg: listview.getButtonGroup()) {
//        	HorizontalLayout hl = new HorizontalLayout();
//        	for(ButtonDescription btnDesc: btg.getButtons()) {
//            	btnDescriptionMap.put(btnDesc.getItemId(), btnDesc);
//            	String msg = MsgUtil.getDynaMenuMsg(messageSource, btnDesc.getItemId());
//            	Button bt;
//            	if (btnDesc.getIcon() == null) {
//            		bt = new Button(msg);
//            	} else {
//            		bt = new Button(btnDesc.getIcon());
//            		bt.setDescription(msg);
//            	}
//            	bt.addClickListener(this);
//            	bt.setData(btnDesc);
//            	itemMap.put(btnDesc.getItemId(), bt);
//            	hl.addComponent(bt);
//        	}
//        	btgHl.addComponent(hl);
//        }
//        addComponent(btgHl);
//        onSelectionChange(Lists.newArrayList());
//        return this;
//	}
	
//	public DynButton afterInjection(EventBus eventBus, ButtonGroup...btnGroups) {
//		this.eventBus = eventBus;
//		eventBus.register(this);
//		MarginInfo mf = new MarginInfo(false, false, false, true);
//		setMargin(mf);
//		addStyleName("dyn-menu");
//        HorizontalLayout btgHl = new HorizontalLayout();
//        btgHl.setSpacing(true);
//        for(ButtonGroup btg: btnGroups) {
//        	HorizontalLayout hl = new HorizontalLayout();
//        	for(ButtonDescription btnDesc: btg.getButtons()) {
//            	btnDescriptionMap.put(btnDesc.getItemId(), btnDesc);
//            	String msg = MsgUtil.getDynaMenuMsg(messageSource, btnDesc.getItemId());
//            	Button bt;
//            	if (btnDesc.getIcon() == null) {
//            		bt = new Button(msg);
//            	} else {
//            		bt = new Button(btnDesc.getIcon());
//            		bt.setDescription(msg);
//            	}
//            	bt.addClickListener(this);
//            	bt.setData(btnDesc);
//            	itemMap.put(btnDesc.getItemId(), bt);
//            	hl.addComponent(bt);
//        	}
//        	btgHl.addComponent(hl);
//        }
//        addComponent(btgHl);
//        onSelectionChange(Lists.newArrayList());
//        return this;
//	}

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
		listview.onDynButtonClicked(btDesc);
	}
}
