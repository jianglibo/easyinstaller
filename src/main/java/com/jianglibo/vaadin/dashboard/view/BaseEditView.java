package com.jianglibo.vaadin.dashboard.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.Authentication;

import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase;
import com.jianglibo.vaadin.dashboard.uicomponent.form.FormBase.HandMakeFieldsListener;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;
import com.jianglibo.vaadin.dashboard.util.ItemViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public abstract class BaseEditView<E extends BaseEntity, F extends FormBase<E>, J extends JpaRepository<E, Long>>
		extends VerticalLayout implements View {

	private final Logger LOGGER = LoggerFactory.getLogger(BaseEditView.class);;

	private final MessageSource messageSource;

	private final J repository;

	private F form;

	private E bean;

	private final Class<E> clazz;

	private final Domains domains;

	private ItemViewFragmentBuilder ifb;

	private final FieldFactories fieldFactories;

	private Label headTitle;

	public BaseEditView(MessageSource messageSource, Class<E> clazz, Domains domains, FieldFactories fieldFactories,
			J repository) {
		this.messageSource = messageSource;
		this.repository = repository;
		this.domains = domains;
		this.clazz = clazz;
		this.fieldFactories = fieldFactories;

	}

	protected void delayCreateContent() {
		setSizeFull();
		addStyleName("transactions");

		setMargin(new MarginInfo(true, true, false, true));

		addComponent(createHeaderLayout());
		form = createForm(messageSource, domains, fieldFactories, repository, (vtw, vffw) -> {
			return createField(vtw, vffw);
		});

		StyleUtil.setOverflowAuto(form, true);

		addComponent(form);

		form.setSizeFull();

		Component ft = buildFooter();

		form.addComponent(ft);
		setExpandRatio(form, 1);
	}

	protected abstract Field<?> createField(VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw);

	@Override
	public void detach() {
		super.detach();
		// A new instance of TransactionsView is created every time it's
		// navigated to so we'll need to clean up references to it on detach.
		// DashboardEventBus.unregister(this);
	}

	protected abstract F createForm(MessageSource messageSource, Domains domains, FieldFactories fieldFactories,
			JpaRepository<E, Long> repository, HandMakeFieldsListener handMakeFieldsListener);

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
				BaseEditView.this.backward();
			}
		});
		tools.addComponent(backBtn);
		return hl;
	}

	private Component buildFooter() {
		HorizontalLayout footer = new HorizontalLayout();
		footer.setMargin(true);
		footer.setWidth(100.0f, Unit.PERCENTAGE);

		Button ok = new Button(messageSource.getMessage("shared.btn.save", null, UI.getCurrent().getLocale()));
		ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
		ok.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Authentication ac = VaadinSession.getCurrent().getAttribute(Authentication.class);
				form.save();
			}
		});
		ok.focus();
		footer.addComponent(ok);
		footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);
		return footer;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		LOGGER.info("parameter string is: {}", event.getParameters());
		ifb = new ItemViewFragmentBuilder(event);
		long bid = ifb.getBeanId();
		if (bid == 0) {
			bean = createNewBean();
			headTitle.setValue(MsgUtil.getViewMsg(messageSource, clazz.getSimpleName() + ".newtitle"));
		} else {
			bean = repository.findOne(bid);
			headTitle.setValue(getBeanDisplayName());
		}
		form.setItemDataSource(bean);
	}

	protected abstract E createNewBean();

	protected String getBeanDisplayName() {
		return bean.getDisplayName();
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public J getRepository() {
		return repository;
	}

	public F getForm() {
		return form;
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

	public FieldFactories getFieldFactories() {
		return fieldFactories;
	}

	public void backward() {
		UI.getCurrent().getNavigator().navigateTo(getIfb().getPreviousView().orElse(getListViewName()));
	}

	protected abstract String getListViewName();
}
