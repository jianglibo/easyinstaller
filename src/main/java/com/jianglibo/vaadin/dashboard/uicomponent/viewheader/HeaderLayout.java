package com.jianglibo.vaadin.dashboard.uicomponent.viewheader;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.uicomponent.filterform.FilterForm;
import com.jianglibo.vaadin.dashboard.view.ListView;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class HeaderLayout extends HorizontalLayout {

	private Label title;

	private final MessageSource messageSource;

	private HorizontalLayout tools;
	
	private FilterForm filterForm;
	
	private ListView listview;

	public HeaderLayout(MessageSource messageSource, FilterForm filterForm, boolean showBackBtn,
			String labelTxt, ListView listview) {
		this.messageSource = messageSource;
		this.listview = listview;
		addStyleName("viewheader");
		setSpacing(true);
		Responsive.makeResponsive(this);
		title = new Label(labelTxt);
		title.setSizeUndefined();
		title.addStyleName(ValoTheme.LABEL_H1);
		title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		addComponent(title);
		createToolbar();
		makeToolbar(showBackBtn);
	}


	private void createToolbar() {
		tools = new HorizontalLayout();
		tools.addStyleName("toolbar");
		addComponent(tools);
	}

	public void addToToolbar(Component c) {
		tools.addComponent(c);
	}

	public void addToToolbar(Component c, int position) {
		tools.addComponent(c, position);
	}

	public void addToToolbar(Component c, int position, String... styles) {
		for (String style : styles) {
			c.addStyleName(style);
		}

		tools.addComponent(c, position);
	}

	private void makeToolbar(boolean showBackBtn) {
		if (filterForm != null) {
			addToToolbar(filterForm);
		}
		if (showBackBtn) {
			Button backBtn = new Button(FontAwesome.MAIL_REPLY);
			backBtn.setDescription(messageSource.getMessage("shared.btn.return", null, UI.getCurrent().getLocale()));
			backBtn.addClickListener(new ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					listview.backward();
				}
			});
			addToToolbar(backBtn);
		}
	}

	public void setLabelTxt(String labelTxt) {
		title.setValue(labelTxt);
	}

	public Label getTitle() {
		return title;
	}

	public void setTitle(Label title) {
		this.title = title;
	}
}
