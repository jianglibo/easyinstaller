package com.jianglibo.vaadin.dashboard.uicomponent.pager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jianglibo.vaadin.dashboard.config.InterestInUriFragemnt;
import com.jianglibo.vaadin.dashboard.event.view.CurrentPageEvent;
import com.jianglibo.vaadin.dashboard.event.view.PageMetaEvent;
import com.jianglibo.vaadin.dashboard.util.ViewFragmentBuilder;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class Pager extends HorizontalLayout implements Button.ClickListener, InterestInUriFragemnt {
	
	private static Logger LOGGER = LoggerFactory.getLogger(Pager.class);
	
	@Autowired
	private MessageSource messageSource;
	
	private Label label;
	
	private int currentPage;
	
	private int totalPage;
	
	private Button right;
	
	private Button left;

	private EventBus eventBus;
	
	public Pager afterInjection(EventBus eventBus) {
		this.eventBus = eventBus;
		eventBus.register(this);
		MarginInfo mf = new MarginInfo(true, true, true, false);
		setMargin(mf);
		addStyleName("pager");
		left = new Button("");
		left.addStyleName(ValoTheme.BUTTON_LINK);

		left.setIcon(FontAwesome.ARROW_LEFT);
		right = new Button("");
		
		right.setIcon(FontAwesome.ARROW_RIGHT);
		right.addStyleName(ValoTheme.BUTTON_LINK);
		
		right.addClickListener(this);
		left.addClickListener(this);
		label = new Label();
		addComponents(left, label, right);
		return this;
	}
	
	@Subscribe
	public void whenTotalPageChange(PageMetaEvent tre) {
		setTotalPage(tre.getTotalPage());
		label.setValue(messageSource.getMessage("pager.pagenumber", new Object[]{getTotalPage(), getCurrentPage()}, UI.getCurrent().getLocale()));
	}

	@Subscribe
	public void whenUriFragementChange(ViewFragmentBuilder vfb) {
		setCurrentPage(vfb.getCurrentPage());
		label.setValue(messageSource.getMessage("pager.pagenumber", new Object[]{getTotalPage(), getCurrentPage()}, UI.getCurrent().getLocale()));
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == left) {
			if (currentPage > 0) {
				currentPage--;
				eventBus.post(new CurrentPageEvent(currentPage));
			}
		} else {
			if (currentPage < totalPage) {
				currentPage++;
				eventBus.post(new CurrentPageEvent(currentPage));
			}
		}
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	
	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
}
