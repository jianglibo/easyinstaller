package com.jianglibo.vaadin.dashboard.uicomponent.pager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.event.view.PageMetaEvent;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
/**
 * This object's state is from outside, not from self event. perpage and currentPage is from url, totalPage is from datasource.
 * 
 * @author jianglibo@gmail.com
 *
 */
@SuppressWarnings("serial")
public class Pager extends HorizontalLayout implements Button.ClickListener {
	
	private static Logger LOGGER = LoggerFactory.getLogger(Pager.class);
	
	private MessageSource messageSource;
	
	private Label label;
	
	private int currentPage;
	
	private int totalPage;
	
	private Button right;
	
	private Button left;
	
	private List<PageChangeListener> pageChangeListeners = Lists.newArrayList();
	
	public Pager(MessageSource messageSource) {
		this.messageSource = messageSource;
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
	}
	
	public void addPageChangeListener(PageChangeListener pcl) {
		this.pageChangeListeners.add(pcl);
	}
	
	private void updateDisplay() {
		label.setValue(messageSource.getMessage("pager.pagenumber", new Object[]{getTotalPage(), getCurrentPage()}, UI.getCurrent().getLocale()));
	}
	
	public void setTotalPage(PageMetaEvent tre) {
		setTotalPage(tre.getTotalPage());
		updateDisplay();
	}

	public void setCurrentPageAndPerPage(ListViewFragmentBuilder vfb) {
		setCurrentPage(vfb.getCurrentPage());
		updateDisplay();
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == left) {
			if (currentPage > 1) {
				pageChangeListeners.forEach(pcl -> pcl.pageChanged(currentPage - 1));
			}
		} else {
			if (currentPage < totalPage) {
				pageChangeListeners.forEach(pcl -> pcl.pageChanged(currentPage + 1));
			}
		}
	}
	
	public static interface PageChangeListener {
		void pageChanged(int page);
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
