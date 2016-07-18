package com.jianglibo.vaadin.dashboard.view.installationpackages;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;

import com.google.common.eventbus.Subscribe;
import com.jianglibo.vaadin.dashboard.domain.PkSource;
import com.jianglibo.vaadin.dashboard.domain.Transaction;
import com.jianglibo.vaadin.dashboard.event.DashboardEvent.BrowserResizeEvent;
import com.jianglibo.vaadin.dashboard.event.DashboardEvent.TransactionReportEvent;
import com.jianglibo.vaadin.dashboard.event.DashboardEventBus;
import com.jianglibo.vaadin.dashboard.repositories.PkSourceRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.upload.ImmediateUploader;
import com.jianglibo.vaadin.dashboard.view.reports.ReportsView;
import com.jianglibo.vaadin.dashboard.view.upload.UploadView.ImageUploader;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.TableDragMode;
import com.vaadin.ui.Upload.ChangeEvent;
import com.vaadin.ui.Upload.ChangeListener;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringView(name=InstallationPackageView.VIEW_NAME)
public class InstallationPackageView extends VerticalLayout implements View {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private final PkSourceRepository pkSourceRepository;
	
	private final MessageSource messageSource;

	public static final String VIEW_NAME = "installationPackage";
    
    public static final FontAwesome ICON_VALUE = FontAwesome.FILE_ARCHIVE_O;
    
    private ApplicationContext applicationContext;
    
    private final Table table;

	private Button createReport;
	
//	private Upload upload;
    private static final DateFormat DATEFORMAT = new SimpleDateFormat(
            "MM/dd/yyyy hh:mm:ss a");
    private static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#.##");
    
    private static final String[] DEFAULT_COLLAPSIBLE = { "length", "originFrom",  "createdAt"};
    
	@Autowired
    public InstallationPackageView(PkSourceRepository pkSourceRepository, MessageSource messageSource, ApplicationContext applicationContext) {
		this.pkSourceRepository = pkSourceRepository;
		this.messageSource = messageSource;
		this.applicationContext = applicationContext;
        setSizeFull();
        addStyleName("transactions");
//        DashboardEventBus.register(this);

        addComponent(buildToolbar());
        
//        HorizontalLayout vl = new HorizontalLayout();
//        vl.setSpacing(true);
//        vl.setSizeFull();
//        
//        final ProgressBar bar = new ProgressBar(0.0f);
//        vl.addComponent(bar);
//        vl.setComponentAlignment(bar, Alignment.MIDDLE_CENTER);
//        vl.setWidth("250px");
//        vl.addComponent(new Button("Increase",
//            new ClickListener() {
//            @Override
//            public void buttonClick(ClickEvent event) {
//                float current = bar.getValue();
//                if (current < 1.0f)
//                    bar.setValue(current + 0.10f);
//            }
//        }));
//        
//        addComponent(vl);
//        setExpandRatio(vl, 1);
        table = buildTable();
        addComponent(table);
        setExpandRatio(table, 1);
	}
	
    @Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
//        DashboardEventBus.unregister(this);
    }

    private Component buildToolbar() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);
        Responsive.makeResponsive(header);

        Label title = new Label(messageSource.getMessage("view.installationpackage.title", null, UI.getCurrent().getLocale()));
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(title);

//        createReport = buildCreateReport();
//        Component createReport = new PackageUploader();
        
        
//        FileUploadReceiver furc = applicationContext.getBean(FileUploadReceiver.class); 
//        upload = new Upload("", furc);
//        upload.addSucceededListener(furc);
//        upload.addChangeListener(new ChangeListener() {
//			
//			@Override
//			public void filenameChanged(ChangeEvent event) {
//				
//				
//			}
//		});
//        upload.addStartedListener(new StartedListener() {
//			
//			@Override
//			public void uploadStarted(StartedEvent event) {
//				event.getFilename();
//		        new Notification("Starting...",
//		                "",
//		                Notification.Type.HUMANIZED_MESSAGE)
//		   .show(Page.getCurrent());
//				
//			}
//		});
//        upload.setButtonCaption(messageSource.getMessage("view.installationpackage.uploadbtn",null, UI.getCurrent().getLocale()));
        HorizontalLayout tools = new HorizontalLayout(new ImmediateUploader(), new PkSourceFilterBuilder(this).build());
        tools.setSpacing(true);
        tools.addStyleName("toolbar");
        

        
        header.addComponent(tools);

        return header;
    }

    private Button buildCreateReport() {
        final Button createReport = new Button("Create Report");
        createReport
                .setDescription("Create a new report from the selected transactions");
        createReport.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                createNewReportFromSelection();
            }
        });
        createReport.setEnabled(false);
        return createReport;
    }



    private Table buildTable() {
        final Table table = new Table() {
            @Override
            protected String formatPropertyValue(final Object rowId,
                    final Object colId, final Property<?> property) {
                String result = super.formatPropertyValue(rowId, colId,
                        property);
                if (colId.equals("time")) {
                    result = DATEFORMAT.format(((Date) property.getValue()));
                } else if (colId.equals("price")) {
                    if (property != null && property.getValue() != null) {
                        return "$" + DECIMALFORMAT.format(property.getValue());
                    } else {
                        return "";
                    }
                }
                return result;
            }
        };
        table.setSizeFull();
        table.addStyleName(ValoTheme.TABLE_BORDERLESS);
        table.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        table.addStyleName(ValoTheme.TABLE_COMPACT);
        table.setSelectable(true);

        table.setColumnCollapsingAllowed(true);
        table.setColumnCollapsible("pkname", false);

        table.setColumnReorderingAllowed(true);
        List<PkSource> pkSources = pkSourceRepository.findAll();
        table.setContainerDataSource(new PkSourceContainer(pkSources));
        table.setSortContainerPropertyId("createdAt");
        table.setSortAscending(false);

//        table.setColumnAlignment("seats", Align.RIGHT);
//        table.setColumnAlignment("price", Align.RIGHT);
        Collection c = table.getContainerPropertyIds();
        table.setVisibleColumns("pkname","originFrom","length", "createdAt");
        table.setColumnHeaders("pkname","originFrom","length", "createdAt");

        table.setFooterVisible(true);
        table.setColumnFooter("pkname", "Total");

        table.setColumnFooter("createdAt", String.valueOf(pkSourceRepository.count()));

        // Allow dragging items to the reports menu
        table.setDragMode(TableDragMode.MULTIROW);
        table.setMultiSelect(true);

        table.addActionHandler(new PkSourceActionHandler());

        table.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(final ValueChangeEvent event) {
                if (table.getValue() instanceof Set) {
                    Set<Object> val = (Set<Object>) table.getValue();
                    createReport.setEnabled(val.size() > 0);
                }
            }
        });
        table.setImmediate(true);
        return table;
    }

    private boolean defaultColumnsVisible() {
        boolean result = true;
        for (String propertyId : DEFAULT_COLLAPSIBLE) {
            if (table.isColumnCollapsed(propertyId) == Page.getCurrent()
                    .getBrowserWindowWidth() < 800) {
                result = false;
            }
        }
        return result;
    }

    @Subscribe
    public void browserResized(final BrowserResizeEvent event) {
        // Some columns are collapsed when browser window width gets small
        // enough to make the table fit better.
        if (defaultColumnsVisible()) {
            for (String propertyId : DEFAULT_COLLAPSIBLE) {
                table.setColumnCollapsed(propertyId, Page.getCurrent()
                        .getBrowserWindowWidth() < 800);
            }
        }
    }

    void createNewReportFromSelection() {
        UI.getCurrent().getNavigator()
                .navigateTo(ReportsView.VIEW_NAME);
        DashboardEventBus.post(new TransactionReportEvent(
                (Collection<Transaction>) table.getValue()));
    }
    
    public Table getTable() {
		return table;
	}

    @Override
    public void enter(final ViewChangeEvent event) {
    }

//	@Override
//	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//		this.applicationContext = applicationContext;
//	}
}
