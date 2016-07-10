package com.jianglibo.vaadin.dashboard.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewProvider;

/**
 * when use with spring, this can archived by annotation.So it's not necessary.
 * @author jianglibo@gmail.com
 *
 */
public class DboardViewProvider implements ViewProvider {

	@Override
	public String getViewName(String viewAndParameters) {
		return null;
	}

	@Override
	public View getView(String viewName) {
		return null;
	}

}
