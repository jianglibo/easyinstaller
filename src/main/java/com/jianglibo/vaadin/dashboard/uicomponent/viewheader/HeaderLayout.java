package com.jianglibo.vaadin.dashboard.uicomponent.viewheader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;

import com.google.common.eventbus.EventBus;
import com.jianglibo.vaadin.dashboard.event.view.HistoryBackEvent;
import com.jianglibo.vaadin.dashboard.uicomponent.filterform.FilterForm;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class HeaderLayout extends HorizontalLayout {
		
		private Label title;
		
		
		private final MessageSource messageSource;
		
		private EventBus eventBus;
		
		private ApplicationContext applicationContext;
		
		private HorizontalLayout tools;
		
		@Autowired
		public HeaderLayout(ApplicationContext applicationContext, MessageSource messageSource) {
			this.applicationContext = applicationContext;
			this.messageSource = messageSource;
		}
		
		public HeaderLayout afterInjection(EventBus eventBus,boolean showFilterForm, boolean showBackBtn, String labelTxt) {
			this.eventBus = eventBus;
			addStyleName("viewheader");
			setSpacing(true);
			Responsive.makeResponsive(this);
			title = new Label(labelTxt);
			title.setSizeUndefined();
			title.addStyleName(ValoTheme.LABEL_H1);
			title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
			addComponent(title);
			createToolbar();
			makeToolbar(showFilterForm, showBackBtn);
			return this;
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
		
		public void addToToolbar(Component c, int position, String...styles) {
			for(String style: styles) {
				c.addStyleName(style);
			}
			
			tools.addComponent(c, position);
		}
		
		private void makeToolbar(boolean showFilterForm, boolean showBackBtn) {
			if (showFilterForm) {
				FilterForm filterForm = applicationContext.getBean(FilterForm.class).afterInjection(eventBus, "");
				addToToolbar(filterForm);
			}
			
			if (showBackBtn) {
				Button backBtn = new Button(FontAwesome.MAIL_REPLY);
				backBtn.setDescription(messageSource.getMessage("shared.btn.return", null, UI.getCurrent().getLocale()));
				backBtn.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						eventBus.post(new HistoryBackEvent());
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
