package com.jianglibo.vaadin.dashboard.window.localeselector;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.id.CompositeNestedGeneratedValueGenerator.GenerationContextLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.vaadin.maddon.FilterableListContainer;

import com.google.common.collect.Lists;
import com.google.gwt.thirdparty.guava.common.collect.Sets;
import com.jianglibo.vaadin.dashboard.DashboardUI;
import com.jianglibo.vaadin.dashboard.LocalizedSystemMessageProvider;
import com.jianglibo.vaadin.dashboard.component.MovieDetailsWindow;
import com.jianglibo.vaadin.dashboard.domain.Transaction;
import com.jianglibo.vaadin.dashboard.event.ui.DashboardEventBus;
import com.jianglibo.vaadin.dashboard.wrapper.Wrapper;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.Action;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ClassResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServletRequest;
import com.vaadin.server.VaadinServletResponse;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.server.SpringVaadinServletRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.TableDragMode;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@Scope("prototype")
public class LocaleSelector implements Wrapper<Button> , ApplicationContextAware {

	@Autowired
	private MessageSource messageSource;
	
	private static final Set<String> supportedLanguages = Sets.newHashSet("en", "zh");
	
	@Autowired
	private LocaleResolver localeResolver;
	
	
	private ApplicationContext applicationContext;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LocaleSelector.class);

	private static final DateFormat DATEFORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
	private static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#.##");
	
	public static Locale getLocaleSupported(Locale locale) {
		if (supportedLanguages.contains(locale.getLanguage())) {
			return locale;
		} else {
			return new Locale("en");
		}
	}

	private Button btn;
	
	private Table table;

	@Override
	public Button unwrap() {
		Locale lo = VaadinSession.getCurrent().getLocale();
		btn = new Button(messageSource.getMessage("lanselector.btn", null, UI.getCurrent().getLocale()));
		
		btn.setStyleName(Reindeer.BUTTON_LINK);
//		label.setSizeUndefined();
		btn.setIcon(FontAwesome.LANGUAGE);

		final Window window = new Window(messageSource.getMessage("lanselector.title", null, UI.getCurrent().getLocale()));
		window.setWidth(300.0f, Unit.PIXELS);
		
		window.setContent(buildWindowContent());
		window.setModal(true);
		window.setSizeFull();
		
		btn.addClickListener(ce -> {
			UI.getCurrent().addWindow(window);
//			Notification.show("The button was clicked",
//                    Type.TRAY_NOTIFICATION);
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
    
    private boolean filterByProperty(final String prop, final Item item,
            final String text) {
        if (item == null || item.getItemProperty(prop) == null
                || item.getItemProperty(prop).getValue() == null) {
            return false;
        }
        String val = item.getItemProperty(prop).getValue().toString().trim()
                .toLowerCase();
        if (val.contains(text.toLowerCase().trim())) {
            return true;
        }
        return false;
    }
    
    private Component buildFilter() {
        final TextField filter = new TextField();
        filter.addTextChangeListener(new TextChangeListener() {
            @Override
            public void textChange(final TextChangeEvent event) {
                Filterable data = (Filterable) table.getContainerDataSource();
                data.removeAllContainerFilters();
                data.addContainerFilter(new Filter() {
                    @Override
                    public boolean passesFilter(final Object itemId,
                            final Item item) {

                        if (event.getText() == null
                                || event.getText().equals("")) {
                            return true;
                        }

                        return filterByProperty("country", item,
                                event.getText())
                                || filterByProperty("city", item,
                                        event.getText())
                                || filterByProperty("title", item,
                                        event.getText());

                    }

                    @Override
                    public boolean appliesToProperty(final Object propertyId) {
                        if (propertyId.equals("country")
                                || propertyId.equals("city")
                                || propertyId.equals("title")) {
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
        filter.addShortcutListener(new ShortcutListener("Clear",
                KeyCode.ESCAPE, null) {
            @Override
            public void handleAction(final Object sender, final Object target) {
                filter.setValue("");
                ((Filterable) table.getContainerDataSource())
                        .removeAllContainerFilters();
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
		
//		table.setColumnCollapsible("time", false);
//		table.setColumnCollapsible("price", false);

//		table.setColumnReorderingAllowed(true);
		
		table.setContainerDataSource(new LansContainer(getAvailableLans()));
		
//		table.setSortContainerPropertyId("time");
//		table.setSortAscending(false);
//
//		table.setColumnAlignment("seats", Align.RIGHT);
//		table.setColumnAlignment("price", Align.RIGHT);

		table.setVisibleColumns("name", "code");
		
		table.setColumnHeaders(messageSource.getMessage("lanselector.name", null, UI.getCurrent().getLocale()), messageSource.getMessage("lanselector.code", null, UI.getCurrent().getLocale()));
		table.setFooterVisible(true);
//		table.setColumnFooter("time", "Total");

//		table.setColumnFooter("price", "$" + DECIMALFORMAT.format(DashboardUI.getDataProvider().getTotalSum()));

		// Allow dragging items to the reports menu
		table.setDragMode(TableDragMode.MULTIROW);
		table.setMultiSelect(false);

		table.addActionHandler(new TransactionsActionHandler());

		// table.addValueChangeListener(new ValueChangeListener() {
		// @Override
		// public void valueChange(final ValueChangeEvent event) {
		// if (table.getValue() instanceof Set) {
		// Set<Object> val = (Set<Object>) table.getValue();
		// createReport.setEnabled(val.size() > 0);
		// }
		// }
		// });
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
				
				HttpServletRequest originRequest = (VaadinServletRequest)VaadinService.getCurrentRequest();
				HttpServletResponse originResponse = (VaadinServletResponse)VaadinService.getCurrentResponse();
				localeResolver.setLocale(originRequest, originResponse, newLocale);
//				lsmp.changeLocale(newLocale);
//				HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
//				RequestContext rc = new RequestContext(request);

//				RequestContext rc = applicationContext.getBean(RequestContext.class);
//				rc.changeLocale(newLocale);
				UI.getCurrent().getPage().reload();
			}
		});
		
//		table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
//		    @Override
//		    public void itemClick(ItemClickEvent itemClickEvent) {
//		        System.out.println(itemClickEvent.getItemId().toString());
//		    }
//		});

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

	@SuppressWarnings("serial")
	private class TransactionsActionHandler implements Handler {
		private final Action report = new Action("Create Report");

		private final Action discard = new Action("Discard");

		private final Action details = new Action("Movie details");

		@Override
		public void handleAction(final Action action, final Object sender, final Object target) {
			if (action == report) {
//				createNewReportFromSelection();
			} else if (action == discard) {
				Notification.show("Not implemented in this demo");
			} else if (action == details) {
				Item item = ((Table) sender).getItem(target);
				if (item != null) {
					Long movieId = (Long) item.getItemProperty("movieId").getValue();
					MovieDetailsWindow.open(DashboardUI.getDataProvider().getMovie(movieId), null, null);
				}
			}
		}

		@Override
		public Action[] getActions(final Object target, final Object sender) {
			return new Action[] { details, report, discard };
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

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		
	}
}
