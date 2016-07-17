package com.jianglibo.vaadin.dashboard.view.installationpackages;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class PackageUploader extends CustomComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PackageUploader() {
        HorizontalLayout panelContent = new HorizontalLayout();
        panelContent.setMargin(true); // Very useful

        // Compose from multiple components
        Label label = new Label("hello");
        label.setSizeUndefined(); // Shrink
        panelContent.addComponent(label);
        panelContent.addComponent(new Button("Ok"));

        // Set the size as undefined at all levels
        panelContent.setSizeUndefined();
        setSizeUndefined();

        // The composition root MUST be set
        setCompositionRoot(panelContent);
        setSizeUndefined();
	}
}
