package com.jianglibo.vaadin.dashboard.view.boxsoftware;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.repositories.BoxRepository;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@SpringView(name = BoxSoftwareView.VIEW_NAME)
public class BoxSoftwareView extends VerticalLayout implements View {
	
	
	private final MessageSource messageSource;
	
	public final static String VIEW_NAME = "boxsoftware";
	
	private ListViewFragmentBuilder lvfb;
	
	private Button backBtn;
	
	private Label title;
	
	private final BoxRepository boxRepository;
	
	private final Domains domains;
	
	@Autowired
	public BoxSoftwareView(MessageSource messageSource,BoxRepository boxRepository, Domains domains) {
		this.messageSource = messageSource;
		this.boxRepository = boxRepository;
		this.domains = domains;
		setSizeFull();
		addStyleName("transactions");
		addComponent(createTop());
		
		VerticalLayout vl = new VerticalLayout();
		vl.setSpacing(true);
		vl.setSizeFull();

		vl.addComponent(createInstallForm());
		
		addComponent(vl);
		setExpandRatio(vl, 1);
	}
	

	private Component createInstallForm() {
		BoxSoftwareViewSoftwareGrid g = new BoxSoftwareViewSoftwareGrid(messageSource, domains);
		g.delayCreateContent();
		return g;
	}


	@Override
	public void enter(ViewChangeEvent event) {
		setLvfb(new ListViewFragmentBuilder(event));
		if (getLvfb().getPreviousView().isPresent()) {
			StyleUtil.show(backBtn);
		}
		Box box = boxRepository.findOne(getLvfb().getLong("boxid"));
		
		title.setValue(MsgUtil.getMsgWithSubs(messageSource, "view.boxsoftware.title", box.getDisplayName()));
	}
	
	private Component createTop() {
		HorizontalLayout hl = new HorizontalLayout();
		
		hl.addStyleName("viewheader");
		hl.setSpacing(true);
		Responsive.makeResponsive(hl);

		title = new Label("");
		title.setSizeUndefined();
		title.addStyleName(ValoTheme.LABEL_H1);
		title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		hl.addComponent(title);
		
		
		HorizontalLayout tools = new HorizontalLayout();
		tools.addStyleName("toolbar");
		hl.addComponent(tools);

		backBtn = new Button(FontAwesome.MAIL_REPLY);
		StyleUtil.hide(backBtn);

		backBtn.setDescription(messageSource.getMessage("shared.btn.return", null, UI.getCurrent().getLocale()));

		tools.addComponent(backBtn);
		return hl;
	}


	public ListViewFragmentBuilder getLvfb() {
		return lvfb;
	}


	public void setLvfb(ListViewFragmentBuilder lvfb) {
		this.lvfb = lvfb;
	}

}
