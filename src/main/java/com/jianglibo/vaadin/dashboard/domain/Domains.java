package com.jianglibo.vaadin.dashboard.domain;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumn;
import com.jianglibo.vaadin.dashboard.annotation.FormFields;
import com.jianglibo.vaadin.dashboard.annotation.TableColumns;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.util.ClassScanner;

@Component
public class Domains {
	
	Map<String, TableColumns> tableColumns = Maps.newHashMap();
	
	Map<String, FormFields> formFields = Maps.newHashMap();

	Map<String, VaadinTable> tables = Maps.newHashMap();
	
	@PostConstruct
	void after() throws ClassNotFoundException, IOException {
		
		List<Class<?>> clazzes = ClassScanner.findAnnotatedBy("com.jianglibo.vaadin.dashboard.domain", VaadinTable.class);
		
		for(Class<?> clazz : clazzes) {
			VaadinTable vt = clazz.getAnnotation(VaadinTable.class);
			tableColumns.put(vt.name(), new TableColumns(processOneTableColumn(clazz)));
			formFields.put(vt.name(), new FormFields(processOneTableForm(clazz)));
			tables.put(vt.name(), vt);
		}
	}
	
	private SortedMap<Integer, VaadinFormField> processOneTableForm(Class<?> clazz) {
		SortedMap<Integer, VaadinFormField> vfs = Maps.newTreeMap();
		
		for(Field field  : clazz.getDeclaredFields()){
		    if (field.isAnnotationPresent(VaadinFormField.class)) {
	        	VaadinFormField vf =  field.getAnnotation(VaadinFormField.class);
	        	vfs.put(vf.order(), vf);
	        }
		}
		
		for(Field field  : clazz.getSuperclass().getDeclaredFields()){
		    if (field.isAnnotationPresent(VaadinFormField.class)) {
	        	VaadinFormField tc =  field.getAnnotation(VaadinFormField.class);
	        	vfs.put(tc.order(), tc);
	        }
		}
		return vfs;
	}

	private SortedMap<Integer, VaadinTableColumn> processOneTableColumn(Class<?> clazz) {
		SortedMap<Integer, VaadinTableColumn> sm = Maps.newTreeMap();
		
		for(Field field  : clazz.getDeclaredFields()){
		    if (field.isAnnotationPresent(VaadinTableColumn.class)) {
	        	VaadinTableColumn tc =  field.getAnnotation(VaadinTableColumn.class);
	        	sm.put(tc.order(), tc);
	        }
		}
		
		for(Field field  : clazz.getSuperclass().getDeclaredFields()){
		    if (field.isAnnotationPresent(VaadinTableColumn.class)) {
	        	VaadinTableColumn tc =  field.getAnnotation(VaadinTableColumn.class);
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

	public Map<String, FormFields> getFormFields() {
		return formFields;
	}

}
