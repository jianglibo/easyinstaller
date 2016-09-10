package com.jianglibo.vaadin.dashboard.view.install;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Install;
import com.jianglibo.vaadin.dashboard.repositories.InstallRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.table.TableBase;
import com.jianglibo.vaadin.dashboard.view.ListView;
import com.vaadin.data.Property;

@SuppressWarnings("serial")
public class InstallTable extends TableBase<Install> {
	
	public InstallTable(MessageSource messageSource, Domains domains,InstallContainer container, InstallRepository repository, ListView listview) {
		super(Install.class, domains,container, messageSource);
		container.setEnableSort(true);
	}

	@Override
	public void setFooter() {
		setColumnFooter("createdAt", "");
		setColumnFooter("ip", "Total");
	}
	
	@Override
	protected String formatPropertyValue(final Object rowId, final Object colId, final Property<?> property) {
		String result = super.formatPropertyValue(rowId, colId, property);
		if (colId.equals("createdAt")) {
			result = formatDate(DATEFORMAT, property);
		}
		return result;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

}
