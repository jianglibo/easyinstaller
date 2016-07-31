package com.jianglibo.vaadin.dashboard.domain;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.jianglibo.vaadin.dashboard.annotation.TableColumn;
import com.jianglibo.vaadin.dashboard.annotation.TableColumns;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.util.ClassScanner;

@Component
public class Domains {
	
	Map<String, TableColumns> tableColumns = Maps.newHashMap();

	Map<String, VaadinTable> tables = Maps.newHashMap();
	
	@PostConstruct
	void after() throws ClassNotFoundException, IOException {
		
		List<Class<?>> clazzes = ClassScanner.findAnnotatedBy("com.jianglibo.vaadin.dashboard.domain", VaadinTable.class);
		
		for(Class<?> clazz : clazzes) {
			VaadinTable vt = clazz.getAnnotation(VaadinTable.class);
			tableColumns.put(vt.name(), new TableColumns(processOneTable(clazz)));
			tables.put(vt.name(), vt);
		}
	}
	
	private SortedMap<Integer, TableColumn> processOneTable(Class<?> clazz) {
		SortedMap<Integer, TableColumn> sm = Maps.newTreeMap();
		
		for(Field field  : clazz.getDeclaredFields())
		{
		    if (field.isAnnotationPresent(TableColumn.class))
		        {
		        	TableColumn tc =  field.getAnnotation(TableColumn.class);
		        	sm.put(tc.order(), tc);
		        }
		}
		return sm;
	}
	

	public Map<String, TableColumns> getTableColumns() {
		return tableColumns;
	}

	public Map<String, VaadinTable> getTables() {
		return tables;
	}

}
