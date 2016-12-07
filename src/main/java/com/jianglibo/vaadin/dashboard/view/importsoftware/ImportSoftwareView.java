package com.jianglibo.vaadin.dashboard.view.importsoftware;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.google.common.io.Files;
import com.jianglibo.vaadin.dashboard.service.HttpPageGetter;
import com.jianglibo.vaadin.dashboard.service.SoftwareImportor;
import com.jianglibo.vaadin.dashboard.uicomponent.upload.ImmediateUploader;
import com.jianglibo.vaadin.dashboard.uicomponent.upload.ReceiverWithEventListener;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.jianglibo.vaadin.dashboard.util.NotificationUtil;
import com.jianglibo.vaadin.dashboard.util.StyleUtil;
import com.jianglibo.vaadin.dashboard.util.ThrowableUtil;
import com.jianglibo.vaadin.dashboard.vo.SoftwareImportResult;
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
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@SpringView(name = ImportSoftwareView.VIEW_NAME)
public class ImportSoftwareView extends VerticalLayout implements View {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImportSoftwareView.class);

	private final MessageSource messageSource;

	public final static String VIEW_NAME = "importsoftware";

	private ListViewFragmentBuilder lvfb;
	
	private final SoftwareImportor softwareImporter;
	
	private final HttpPageGetter httpPageGetter;

	private Button backBtn;

	private Label title;

	@Autowired
	public ImportSoftwareView(MessageSource messageSource, SoftwareImportor softwareImporter, HttpPageGetter httpPageGetter) {
		this.messageSource = messageSource;
		this.softwareImporter = softwareImporter;
		this.httpPageGetter = httpPageGetter;
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

		TextField urlField = new TextField();
		urlField.setWidth("80%");

		Button urlBtn = new Button(MsgUtil.getMsgFallbackToSelf(messageSource, "view.importsoftware.", "urlBtn"),
				event -> {
					String s = urlField.getValue();
					boolean goon = true;
					try {
						new URL(s);
					} catch (Exception e) {
						goon = false;
						NotificationUtil.tray(messageSource, "illegalZipUrl", urlField.getValue());
					}
					
					if (goon) {
						urlField.setValue("");
						Path zipFilePath = httpPageGetter.getFile(s, null);
						if (zipFilePath == null) {
							NotificationUtil.tray(messageSource, "zipDownloadFail", s);
						} else {
							try {
								softwareImporter.installSoftwareFromZipFile(zipFilePath);
								NotificationUtil.tray(messageSource, "taskdone", urlField.getValue());
							} catch (Exception e) {
								NotificationUtil.tray(messageSource, "importSoftwareFail", urlField.getValue());
							}
						}
					}
				});

		StyleUtil.setMarginLeftTen(urlBtn);
		
		
		TextField pathField = new TextField();
		pathField.setWidth("80%");

		Button pathBtn = new Button(MsgUtil.getMsgFallbackToSelf(messageSource, "view.importsoftware.", "pathBtn"),
				event -> {
					String paths = pathField.getValue();
					
					if (paths.trim().length() > 0) {
						try {
							List<SoftwareImportResult> sirs = softwareImporter.installSoftwareFromFolder(Paths.get(paths));
							for(SoftwareImportResult sir : sirs) {
								if (!sir.isSuccess()) {
									NotificationUtil.errorRaw(sir.getReason());
									return;
								}
							}
							NotificationUtil.tray(messageSource, "taskdone", paths);
						} catch (Exception e) {
							NotificationUtil.tray(messageSource, "importSoftwareFail", pathField.getValue());
							LOGGER.error(ThrowableUtil.printToString(e));
						}
					}
				});

		StyleUtil.setMarginLeftTen(pathBtn);

		Label uploadLabel = new Label(
				MsgUtil.getMsgFallbackToSelf(messageSource, "view.importsoftware.", "uploadLabel"));

		Label remoteLabel = new Label(
				MsgUtil.getMsgFallbackToSelf(messageSource, "view.importsoftware.", "remoteLabel"));
		Component uploader = new ImmediateUploader(messageSource, new ZipFileUploadReceiver());
		StyleUtil.setMarginRightTen(uploader);

		Label localLabel = new Label(
				MsgUtil.getMsgFallbackToSelf(messageSource, "view.importsoftware.", "localLabel"));

		Label descriptionLabel = new Label();
		descriptionLabel.setContentMode(ContentMode.HTML);
		descriptionLabel
				.setValue(MsgUtil.getMsgFallbackToSelf(messageSource, "view.importsoftware.", "descriptionLabel"));
		vl.addComponents(uploadLabel, uploader, remoteLabel, urlField, urlBtn,localLabel, pathField, pathBtn, descriptionLabel);
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

		title = new Label(MsgUtil.getMsgFallbackToSelf(messageSource, "view.importsoftware.", "title"));
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
	
	private void startImport(ZipFileUploadReceiver zfur) {
		if (zfur.isSuccess()) {
			try {
				softwareImporter.installSoftwareFromZipFile(zfur.getTmpFile());
			} catch (IOException e) {
				NotificationUtil.tray(messageSource, "importSoftwareFail", zfur.getFilename());
			}
		} else {
			NotificationUtil.tray(messageSource, "uploadFail");
		}
	}
	
	// Because it's a inner class, so can direct access main class.
	public class ZipFileUploadReceiver implements ReceiverWithEventListener {
		
		private Path tmpFile;
		private boolean success = false;
		private String filename;
		
		@Override
		public OutputStream receiveUpload(String filename, String mimeType) {
			this.setFilename(filename);
			if (!"zip".equalsIgnoreCase(Files.getFileExtension(filename))) {
				return null;
			} else {
				try {
					tmpFile = java.nio.file.Files.createTempFile(ImportSoftwareView.class.getName(), "");
					return java.nio.file.Files.newOutputStream(tmpFile);
				} catch (IOException e) {
					return null;
				}
			}
		}

		public Path getTmpFile() {
			return tmpFile;
		}

		public void setTmpFile(Path tmpFile) {
			this.tmpFile = tmpFile;
		}

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public String getFilename() {
			return filename;
		}

		public void setFilename(String filename) {
			this.filename = filename;
		}

		@Override
		public void uploadFailed(FailedEvent event) {
			this.setSuccess(false);
			startImport(this);
		}

		@Override
		public void uploadSucceeded(SucceededEvent event) {
			this.setSuccess(true);
			startImport(this);
		}

		@Override
		public void uploadFinished(FinishedEvent event) {
		}
	}
}
