package com.jianglibo.vaadin.dashboard.uicomponent.dynmenu;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.gwt.thirdparty.guava.common.collect.Maps;
import com.jianglibo.vaadin.dashboard.domain.PkSource;
import com.jianglibo.vaadin.dashboard.event.view.DynMenuClickEvent;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DynButton extends HorizontalLayout implements ClickListener {
	
	@Autowired
	private MessageSource messageSource;
	
	private Map<String, ButtonDescription> btnDescriptionMap = Maps.newHashMap();
	
	private Map<String, Button> itemMap = Maps.newHashMap();
	
	private EventBus eventBus;
	
	public DynButton afterInjection(EventBus eventBus, ButtonGroup...btnGroups) {
		this.eventBus = eventBus;
		eventBus.register(this);
		MarginInfo mf = new MarginInfo(false, false, false, true);
		setMargin(mf);
		addStyleName("dyn-menu");
        HorizontalLayout btgHl = new HorizontalLayout();
        btgHl.setSpacing(true);
        for(ButtonGroup btg: btnGroups) {
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
        onSelectionChange(Lists.newArrayList());
        return this;
	}

	@Subscribe
	public void onSelectionChange(Collection<PkSource> files) {
		itemMap.forEach((k, v) -> {
			boolean enabled = btnDescriptionMap.get(k).isEnabled(files.size());
			if (enabled) {
				v.removeStyleName("display-none");
			} else {
				v.addStyleName("display-none");
			}
		});
	}

	@Override
	public void buttonClick(ClickEvent event) {
		ButtonDescription btDesc = (ButtonDescription) event.getButton().getData();
		eventBus.post(new DynMenuClickEvent(btDesc.getItemId()));
	}
}
