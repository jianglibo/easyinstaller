package com.jianglibo.vaadin.dashboard.uicomponent.pager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class Pager extends HorizontalLayout implements Button.ClickListener {
	
	private static Logger LOGGER = LoggerFactory.getLogger(Pager.class);
	
	private MessageSource messageSource;
	
	private Pageable pageable;
	
	private Label label;
	
	
	public Pager(MessageSource messageSource, Pageable pageable, PagerListener listener) {
		MarginInfo mf = new MarginInfo(true, true, true, false);
		setMargin(mf);
		addStyleName("pager");
		this.pageable = pageable;
		Button left = new Button("");
		left.addStyleName(ValoTheme.BUTTON_LINK);

		left.setIcon(FontAwesome.ARROW_LEFT);
		Button right = new Button("");
		
		right.setIcon(FontAwesome.ARROW_RIGHT);
		right.addStyleName(ValoTheme.BUTTON_LINK);
		
		right.addClickListener(this);
		left.addClickListener(this);
		pageable.getPageNumber();
		label = new Label();
		addComponents(left, label, right);
	}
	
	private void setLabelText() {
		label.setCaption(messageSource.getMessage("pager.pagenumber", new Object[]{pageable.getPageNumber(), pageable.getOffset()/pageable.getPageSize()}, UI.getCurrent().getLocale()));
	}


	@Override
	public void buttonClick(ClickEvent event) {
		LOGGER.info("is left arrow clicked: {}", String.valueOf(event.getButton().getIcon().equals(FontAwesome.ARROW_LEFT)));
		
	}
}
