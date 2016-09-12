package com.jianglibo.vaadin.dashboard.uicomponent.dynmenu;

import java.util.Map;

import org.springframework.context.MessageSource;

import com.google.gwt.thirdparty.guava.common.collect.Maps;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class DynMenu extends HorizontalLayout {
	
	
	private Map<String, MenuItemDescription> menuItemDescriptionMap = Maps.newHashMap();
	
	private Map<String, MenuItem> itemMap = Maps.newHashMap();
	
	public DynMenu(MessageSource messageSource,  MenuItemDescription...menuItemDescriptions) {
		MarginInfo mf = new MarginInfo(true, true, true, true);
		setMargin(mf);
		addStyleName("dyn-menu");
        MenuBar mb = new MenuBar();
        
        mb.addStyleName(ValoTheme.MENUBAR_SMALL);
        for(MenuItemDescription mid: menuItemDescriptions) {
        	menuItemDescriptionMap.put(mid.getItemId(), mid);
        	String msg = MsgUtil.getDynaMenuMsg(messageSource, mid.getItemId());
        	MenuItem mi;
        	if (mid.getIcon() == null) {
        		mi = mb.addItem(msg, null, new ItemCommand(mid.getItemId()));
        	} else {
        		mi = mb.addItem("", mid.getIcon(), new ItemCommand(mid.getItemId()));
        		mi.setDescription(msg);
        	}
        	itemMap.put(mid.getItemId(), mi);
        	
        }
        addComponent(mb);
	}
	
	private class ItemCommand implements Command {
		private String itemId;
		
		public ItemCommand(String itemId) {
			this.itemId = itemId;
		}

		@Override
		public void menuSelected(MenuItem selectedItem) {
		}
	}

}
