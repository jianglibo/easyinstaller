package com.jianglibo.vaadin.dashboard.uicomponent.viewheader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;

import com.google.common.eventbus.EventBus;
import com.jianglibo.vaadin.dashboard.event.view.HistoryBackEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class HeaderLayout extends HorizontalLayout {
		
		private Label title;
		
		@Autowired
		private MessageSource messageSource;
		
		private Button backBtn;
		
		private EventBus eventBus;
		
		public HeaderLayout afterInjection(String labelTxt) {
			
			addStyleName("viewheader");
			setSpacing(true);
			Responsive.makeResponsive(this);

			title = new Label(labelTxt);
			title.setSizeUndefined();
			title.addStyleName(ValoTheme.LABEL_H1);
			title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
			addComponent(title);
			return this;
		}
		
		public HeaderLayout afterInjectionWithBackBtn(EventBus eventBus, String labelTxt) {
			afterInjection(labelTxt);
			this.eventBus = eventBus;
			addBtn();
			return this;
		}
		
		private void addBtn() {
			backBtn = new Button(FontAwesome.MAIL_REPLY);
			backBtn.setDescription(messageSource.getMessage("shared.btn.return", null, UI.getCurrent().getLocale()));
			backBtn.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					eventBus.post(new HistoryBackEvent());
				}
			});
			HorizontalLayout tools = new HorizontalLayout(backBtn);
			tools.addStyleName("toolbar");
			addComponent(tools);
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
