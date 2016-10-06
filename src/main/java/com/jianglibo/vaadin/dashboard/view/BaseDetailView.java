package com.jianglibo.vaadin.dashboard.view;

import java.util.List;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.util.ItemViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public abstract class BaseDetailView<E extends BaseEntity, J extends JpaRepository<E, Long>>
		extends VerticalLayout implements View {

	private final Logger LOGGER = LoggerFactory.getLogger(BaseDetailView.class);;

	private final MessageSource messageSource;

	private final J repository;

	private E bean;

	private final Class<E> clazz;

	private final Domains domains;

	private ItemViewFragmentBuilder ifb;

	private Label headTitle;
	
	private Panel contentPanel;

	public BaseDetailView(MessageSource messageSource, Class<E> clazz, Domains domains, J repository) {
		this.messageSource = messageSource;
		this.repository = repository;
		this.domains = domains;
		this.clazz = clazz;
	}

	protected void delayCreateContent() {
		setSizeFull();
		addStyleName("transactions");
		setMargin(new MarginInfo(true, true, false, true));
		addComponent(createHeaderLayout());
		contentPanel = new Panel();
		contentPanel.setSizeFull();
		addComponent(contentPanel);
		setExpandRatio(contentPanel, 1);
	}

	private Component createTwoColumnGrid() {
		List<DisplayFieldPair> detailItems = getDetailItems();
		GridLayout gl = new GridLayout(2, detailItems.size());
		IntStream.range(0, detailItems.size()).forEach(i -> {
			DisplayFieldPair df = detailItems.get(i);
			Label lb = new Label();
			lb.setValue(MsgUtil.getFieldMsg(messageSource, getMessagePrefix(), df.getFn()));
			StyleUtil.setMarginRightTwenty(lb);
			gl.addComponent(lb, 0, i);
			gl.addComponent(detailItems.get(i).getDisplayField(), 1, i);
		});
		return gl;
	}
	
	protected abstract String getMessagePrefix();
	
	protected static class DisplayFieldPair {
		private String fn;
		private Component displayField;
		
		public DisplayFieldPair(String fn, Component displayField) {
			super();
			this.fn = fn;
			this.displayField = displayField;
		}
		public String getFn() {
			return fn;
		}
		public void setFn(String fn) {
			this.fn = fn;
		}
		public Component getDisplayField() {
			return displayField;
		}
		public void setDisplayField(Component displayField) {
			this.displayField = displayField;
		}
	}
	
	protected abstract List<DisplayFieldPair> getDetailItems();


	@Override
	public void detach() {
		super.detach();
		// A new instance of TransactionsView is created every time it's
		// navigated to so we'll need to clean up references to it on detach.
		// DashboardEventBus.unregister(this);
	}

	protected HorizontalLayout createHeaderLayout() {
		HorizontalLayout hl = new HorizontalLayout();
		hl.addStyleName("viewheader");
		hl.setSpacing(true);
		Responsive.makeResponsive(hl);
		headTitle = new Label("");
		headTitle.setSizeUndefined();
		headTitle.addStyleName(ValoTheme.LABEL_H1);
		headTitle.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		hl.addComponent(headTitle);
		HorizontalLayout tools = new HorizontalLayout();
		tools.addStyleName("toolbar");
		hl.addComponent(tools);

		Button backBtn = new Button(FontAwesome.MAIL_REPLY);
		backBtn.setDescription(messageSource.getMessage("shared.btn.return", null, UI.getCurrent().getLocale()));
		backBtn.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				BaseDetailView.this.backward();
			}
		});
		tools.addComponent(backBtn);
		return hl;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		LOGGER.info("parameter string is: {}", event.getParameters());
		ifb = new ItemViewFragmentBuilder(event);
		long bid = ifb.getBeanId();
		if (bid > 0) {
			bean = repository.findOne(bid);
			headTitle.setValue(getBeanDisplayName());
		}
		contentPanel.setContent(createTwoColumnGrid());
	}

	protected String getBeanDisplayName() {
		return bean.getDisplayName();
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public J getRepository() {
		return repository;
	}

	public E getBean() {
		return bean;
	}

	public Domains getDomains() {
		return domains;
	}

	public ItemViewFragmentBuilder getIfb() {
		return ifb;
	}

	public void backward() {
		UI.getCurrent().getNavigator().navigateTo(getIfb().getPreviousView().orElse(getListViewName()));
	}

	protected abstract String getListViewName();
}
