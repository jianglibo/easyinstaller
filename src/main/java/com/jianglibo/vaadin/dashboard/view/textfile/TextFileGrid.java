package com.jianglibo.vaadin.dashboard.view.textfile;

import java.util.List;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.annotation.VaadinGridWrapper;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.TextFile;
import com.jianglibo.vaadin.dashboard.uicomponent.grid.BaseGrid;
import com.jianglibo.vaadin.dashboard.util.ColumnUtil;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.ui.Notification;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickEvent;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickListener;

@SuppressWarnings("serial")
public class TextFileGrid extends BaseGrid<TextFile, FreeContainer<TextFile>> {

	public TextFileGrid(FreeContainer<TextFile> dContainer,VaadinGridWrapper vgw, MessageSource messageSource, List<String> sortableContainerPropertyIds, List<String> columnNames, String messagePrefix) {
		super(vgw, dContainer, messageSource, sortableContainerPropertyIds, columnNames, messagePrefix);
		delayCreateContent();
	}

	@Override
	protected void addGeneratedProperty(GeneratedPropertyContainer gpcontainer, String extraName) {
		switch (extraName) {
		case "!edit":
			ColumnUtil.addGeneratedPropertyWithEmptyContent(gpcontainer, extraName);
			break;
		default:
			ColumnUtil.addGeneratedPropertyWithEmptyContent(gpcontainer, extraName);
			break;
		}
	}
	
	@Override
	protected void setupColumn(Column col, String extraName) {
		switch (extraName) {
		case "!edit":
			ColumnUtil.setExternalImageRender(col,  ColumnUtil.EDIT_ICON_URL, new RendererClickListener() {
				@Override
				public void click(RendererClickEvent event) {
					Notification.show(event.getItemId().toString());
				}
			});
			break;
		default:
			break;
		}
	}

	@Override
	protected void setupGrid() {
		FooterRow footer = addFooterRowAt(0);
		FooterCell fc = footer.getCell("upldatedAt");
		if (fc != null) {
			fc.setText("0");
			getdContainer().addItemSetChangeListener(event -> {
				fc.setText("" + event.getContainer().size());
			});
		}
	}

}
