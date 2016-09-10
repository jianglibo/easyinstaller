package com.jianglibo.vaadin.dashboard.view.pksource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.PkSource;
import com.jianglibo.vaadin.dashboard.formatter.FileLengthFormat;
import com.jianglibo.vaadin.dashboard.repositories.PkSourceRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.table.TableBase;
import com.jianglibo.vaadin.dashboard.view.ListView;
import com.vaadin.data.Property;

@SuppressWarnings("serial")
public class PkSourceTable extends TableBase<PkSource> {
	
	@Autowired
	private PkSourceContainer container;
	
	@Autowired
	public PkSourceTable(MessageSource messageSource, Domains domains,PkSourceRepository repository, ListView listview) {
		super(PkSource.class, domains, messageSource);
		container = new PkSourceContainer(repository, domains, listview, this);
		container.setEnableSort(true);
	}


	@Override
	public void setFooter() {
		setColumnFooter("createdAt", "");
	}
	
	@Override
	protected String formatPropertyValue(Object rowId, Object colId, Property<?> property) {
		String result =  super.formatPropertyValue(rowId, colId, property);
		if (colId.equals("createdAt")) {
			result = formatDate(DATEFORMAT, property);
		} else if (colId.equals("length")){
			return FileLengthFormat.format((Long) property.getValue());
		}
		return result;
	}


	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}
	
}
