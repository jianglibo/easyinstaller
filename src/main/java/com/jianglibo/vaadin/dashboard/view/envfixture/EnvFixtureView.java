package com.jianglibo.vaadin.dashboard.view.envfixture;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.google.common.base.Strings;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.jianglibo.vaadin.dashboard.util.NotificationUtil;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@SpringView(name = EnvFixtureView.VIEW_NAME)
public class EnvFixtureView extends VerticalLayout implements View {

private static final Logger LOGGER = LoggerFactory.getLogger(EnvFixtureView.class);

	private final MessageSource messageSource;

	public final static String VIEW_NAME = "envfixture";

	public static final FontAwesome ICON_VALUE = FontAwesome.WRENCH;

	private ListViewFragmentBuilder lvfb;
	
	private final EnvFixtureCreator envFixtureCreator;

	private Button backBtn;

	private Label title;

	@Autowired
	public EnvFixtureView(MessageSource messageSource,EnvFixtureCreator envFixtureCreator) {
		this.messageSource = messageSource;
		this.envFixtureCreator = envFixtureCreator;
		setSizeFull();
		addStyleName("transactions");
		addComponent(createTop());

		VerticalLayout vl = new VerticalLayout();
		vl.setSpacing(true);
		vl.setSizeFull();
		Component tb = toolbars();
		vl.addComponent(tb);

		addComponent(vl);
		setExpandRatio(vl, 1);
	}

	private Component toolbars() {
		CssLayout vl = new CssLayout();
		vl.setSizeFull();
		Responsive.makeResponsive(vl);
		StyleUtil.setMarginTwenty(vl);

		TextField filePathField = new TextField();
		filePathField.setWidth("80%");
		
		final Button filePathBtn = new Button(MsgUtil.getMsgFallbackToSelf(messageSource, "view.envfixture.", "filePathBtn"));
		
		filePathBtn.addClickListener(event -> {
					if (!Strings.isNullOrEmpty(filePathField.getValue())) {
						Path scriptPath = Paths.get(filePathField.getValue());
						filePathField.setValue("");
						try {
							envFixtureCreator.create(scriptPath);
							NotificationUtil.tray(messageSource, "taskdone", filePathField.getValue());
						} catch (Exception e) {
							NotificationUtil.warn(messageSource, "illegalScriptFolder", filePathField.getValue());
						}
					}
				});

		StyleUtil.setMarginLeftTen(filePathBtn);
		
		Label descriptionLabel = new Label();
		descriptionLabel.setContentMode(ContentMode.HTML);
		descriptionLabel.setValue(MsgUtil.getMsgFallbackToSelf(messageSource, "view.envfixture.", "descriptionLabel"));
		vl.addComponents(filePathField, filePathBtn, descriptionLabel);
		return vl;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		setLvfb(new ListViewFragmentBuilder(event));
		if (getLvfb().getPreviousView().isPresent()) {
			StyleUtil.show(backBtn);
		}
	}

	private Component createTop() {
		HorizontalLayout hl = new HorizontalLayout();

		hl.addStyleName("viewheader");
		hl.setSpacing(true);
		Responsive.makeResponsive(hl);

		title = new Label(MsgUtil.getMsgFallbackToSelf(messageSource, "view.envfixture.", "title"));
		title.setSizeUndefined();
		title.addStyleName(ValoTheme.LABEL_H1);
		title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		hl.addComponent(title);

		HorizontalLayout tools = new HorizontalLayout();
		tools.addStyleName("toolbar");
		hl.addComponent(tools);

		backBtn = new Button(FontAwesome.MAIL_REPLY);
		StyleUtil.hide(backBtn);

		backBtn.setDescription(MsgUtil.getMsgWithSubsReturnKeyOnAbsent(messageSource, "shared.btn.return"));

		backBtn.addClickListener(event -> {
			this.backward();
		});
		tools.addComponent(backBtn);
		return hl;
	}

	public void backward() {
		UI.getCurrent().getNavigator().navigateTo(getLvfb().getPreviousView().get());
	}

	public ListViewFragmentBuilder getLvfb() {
		return lvfb;
	}

	public void setLvfb(ListViewFragmentBuilder lvfb) {
		this.lvfb = lvfb;
	}
}
