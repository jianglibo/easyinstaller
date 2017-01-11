package com.jianglibo.vaadin.dashboard.window.localeselector;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.vaadin.maddon.FilterableListContainer;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEventBus;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServletRequest;
import com.vaadin.server.VaadinServletResponse;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.TableDragMode;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class LocaleSelector {

	
	private final MessageSource messageSource;

	private static final Set<String> supportedLanguages = Sets.newHashSet("en", "zh");

//	private final LocaleResolver localeResolver;

	private static final Logger LOGGER = LoggerFactory.getLogger(LocaleSelector.class);

	public static Locale getLocaleSupported(Locale locale) {
		if (supportedLanguages.contains(locale.getLanguage())) {
			return locale;
		} else {
			return new Locale("en");
		}
	}

	private Button btn;

	private Table table;
	
	public LocaleSelector(MessageSource messageSource, LocaleResolver localeResolver){
		this.messageSource = messageSource;
//		this.localeResolver = localeResolver;
	}

	public Button getButton() {
		btn = new Button(messageSource.getMessage("lanselector.btn", null, UI.getCurrent().getLocale()));
		btn.setStyleName(Reindeer.BUTTON_LINK);
		btn.setIcon(FontAwesome.LANGUAGE);

		final Window window = new Window(
				messageSource.getMessage("lanselector.title", null, UI.getCurrent().getLocale()));
		window.setWidth(300.0f, Unit.PIXELS);

		window.setContent(buildWindowContent());
		window.setModal(true);
		window.setSizeFull();

		btn.addClickListener(ce -> {
			UI.getCurrent().addWindow(window);
		});

		return btn;
	}

	private Component buildWindowContent() {
		VerticalLayout content = new VerticalLayout();
		content.setSizeFull();
		content.addStyleName("transactions");
		DashboardEventBus.register(this);
		content.addComponent(buildToolbar());

		Table table = buildTable();
		content.addComponent(table);
		content.setExpandRatio(table, 1);
		return content;
	}

	private Component buildToolbar() {
		HorizontalLayout header = new HorizontalLayout();
		header.addStyleName("viewheader");
		header.setSpacing(true);
		Responsive.makeResponsive(header);

		Label title = new Label(messageSource.getMessage("lanselector.title", null, UI.getCurrent().getLocale()));
		title.setSizeUndefined();
		title.addStyleName(ValoTheme.LABEL_H1);
		title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		header.addComponent(title);
		HorizontalLayout tools = new HorizontalLayout(buildFilter());
		tools.setSpacing(true);
		tools.addStyleName("toolbar");
		header.addComponent(tools);

		return header;
	}

	private boolean filterByProperty(final String prop, final Item item, final String text) {
		if (item == null || item.getItemProperty(prop) == null || item.getItemProperty(prop).getValue() == null) {
			return false;
		}
		String val = item.getItemProperty(prop).getValue().toString().trim().toLowerCase();
		if (val.contains(text.toLowerCase().trim())) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("serial")
	private Component buildFilter() {
		final TextField filter = new TextField();
		filter.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(final TextChangeEvent event) {
				Filterable data = (Filterable) table.getContainerDataSource();
				data.removeAllContainerFilters();
				data.addContainerFilter(new Filter() {
					@Override
					public boolean passesFilter(final Object itemId, final Item item) {

						if (event.getText() == null || event.getText().equals("")) {
							return true;
						}

						return filterByProperty("country", item, event.getText())
								|| filterByProperty("city", item, event.getText())
								|| filterByProperty("title", item, event.getText());

					}

					@Override
					public boolean appliesToProperty(final Object propertyId) {
						if (propertyId.equals("country") || propertyId.equals("city") || propertyId.equals("title")) {
							return true;
						}
						return false;
					}
				});
			}
		});

		filter.setInputPrompt(messageSource.getMessage("lanselector.input", null, UI.getCurrent().getLocale()));
		filter.setIcon(FontAwesome.SEARCH);
		filter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		filter.addShortcutListener(new ShortcutListener("Clear", KeyCode.ESCAPE, null) {
			@Override
			public void handleAction(final Object sender, final Object target) {
				filter.setValue("");
				((Filterable) table.getContainerDataSource()).removeAllContainerFilters();
			}
		});
		return filter;
	}

	@SuppressWarnings("serial")
	private Table buildTable() {
		final Table table = new Table();

		table.setSizeFull();
		table.addStyleName(ValoTheme.TABLE_BORDERLESS);
		table.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
		table.addStyleName(ValoTheme.TABLE_COMPACT);
		table.setSelectable(true);

		table.setColumnCollapsingAllowed(true);
		table.setContainerDataSource(new LansContainer(getAvailableLans()));
		table.setVisibleColumns("name", "code");

		table.setColumnHeaders(messageSource.getMessage("lanselector.name", null, UI.getCurrent().getLocale()),
				messageSource.getMessage("lanselector.code", null, UI.getCurrent().getLocale()));
		table.setFooterVisible(true);
		// Allow dragging items to the reports menu
		table.setDragMode(TableDragMode.MULTIROW);
		table.setMultiSelect(false);

		table.setImmediate(true);

		table.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				Lan lan = (Lan) event.getProperty().getValue();
				String curLan = UI.getCurrent().getLocale().getLanguage();
				if (lan.getCode().equals(curLan)) {
					LOGGER.info("language unchanged, still {}", curLan);
				} else {
					LOGGER.info("change language from {} to {}", curLan, lan.getCode());
				}
				Locale newLocale = new Locale(lan.getCode());
				VaadinSession.getCurrent().setLocale(newLocale);

//				HttpServletRequest originRequest = (VaadinServletRequest) VaadinService.getCurrentRequest();
//				HttpServletResponse originResponse = (VaadinServletResponse) VaadinService.getCurrentResponse();
//				localeResolver.setLocale(originRequest, originResponse, newLocale);
				UI.getCurrent().getPage().reload();
			}
		});
		return table;
	}

	private Collection<Lan> getAvailableLans() {
		List<Lan> lans = Lists.newArrayList();
		lans.add(new Lan("English", "en"));
		lans.add(new Lan("中文", "zh"));
		return lans;
	}

	@SuppressWarnings("serial")
	private class LansContainer extends FilterableListContainer<Lan> {
		public LansContainer(final Collection<Lan> collection) {
			super(collection);
		}
	}

	public static class Lan {
		private String name;
		private String code;

		public Lan(String name, String code) {
			super();
			this.name = name;
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}
	}
}
