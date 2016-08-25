package com.jianglibo.vaadin.dashboard.domain;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumn;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumnWrapper;
import com.jianglibo.vaadin.dashboard.annotation.FormFields;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumns;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.repositories.RepositoryCommonCustom;
import com.jianglibo.vaadin.dashboard.repositories.RepositoryCommonMethod;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.util.ClassScanner;

@Component
public class Domains implements ApplicationContextAware{
	
	Map<String, VaadinTableColumns> tableColumns = Maps.newHashMap();
	
	Map<String, FormFields> formFields = Maps.newHashMap();

	Map<String, VaadinTableWrapper> tables = Maps.newHashMap();
	
	Map<String, Object> repositories = Maps.newHashMap();
	
	public RepositoryCommonMethod<?> getRepositoryCommonMethod(String className) {
		return (RepositoryCommonMethod<?>) repositories.get(className);
	}
	
	public <T extends BaseEntity> RepositoryCommonCustom<T> getRepositoryCommonCustom(String className) {
		return (RepositoryCommonCustom<T>) repositories.get(className);
	}
	
	public JpaRepository<Long, BaseEntity> getJpaRepository(String className) {
		return (JpaRepository<Long, BaseEntity>) repositories.get(className);
	}
	
	@PostConstruct
	void after() throws ClassNotFoundException, IOException {
		
		List<Class<?>> clazzes = ClassScanner.findAnnotatedBy("com.jianglibo.vaadin.dashboard.domain", VaadinTable.class);
		
		for(Class<?> clazz : clazzes) {
			VaadinTable vt = clazz.getAnnotation(VaadinTable.class);
			VaadinTableWrapper vtw = new VaadinTableWrapper(vt, clazz.getSimpleName());
			tableColumns.put(vtw.getName(), new VaadinTableColumns(processOneTableColumn(clazz)));
			formFields.put(vtw.getName(), new FormFields(processOneTableForm(clazz)));
			tables.put(vtw.getName(), vtw);
		}
	}
	
	private Collection<VaadinFormFieldWrapper> processOneTableForm(Class<?> clazz) {
		SortedMap<Integer, VaadinFormFieldWrapper> vfs = Maps.newTreeMap();
		
		for(Field field  : clazz.getDeclaredFields()){
		    if (field.isAnnotationPresent(VaadinFormField.class)) {
	        	VaadinFormField vf =  field.getAnnotation(VaadinFormField.class);
	        	int i = vf.order();
	        	while(vfs.containsKey(i)) {
	        		i++;
	        	}
	        	vfs.put(i, new VaadinFormFieldWrapper(vf, field.getName()));
	        }
		}
		
		for(Field field  : clazz.getSuperclass().getDeclaredFields()){
		    if (field.isAnnotationPresent(VaadinFormField.class)) {
	        	VaadinFormField vf =  field.getAnnotation(VaadinFormField.class);
	        	int i = vf.order();
	        	while(vfs.containsKey(i)) {
	        		i++;
	        	}
	        	vfs.put(i, new VaadinFormFieldWrapper(vf, field.getName()));
	        }
		}
		return vfs.values();
	}

	private Collection<VaadinTableColumnWrapper> processOneTableColumn(Class<?> clazz) {
		SortedMap<Integer, VaadinTableColumnWrapper> sm = Maps.newTreeMap();
		
		for(Field field  : clazz.getDeclaredFields()){
		    if (field.isAnnotationPresent(VaadinTableColumn.class)) {
	        	VaadinTableColumn tc =  field.getAnnotation(VaadinTableColumn.class);
	        	int i = tc.order();
	        	while(sm.containsKey(i)) {
	        		i++;
	        	}
	        	sm.put(i, new VaadinTableColumnWrapper(tc, field.getName()));
	        }
		}
		
		for(Field field  : clazz.getSuperclass().getDeclaredFields()){
		    if (field.isAnnotationPresent(VaadinTableColumn.class)) {
	        	VaadinTableColumn tc =  field.getAnnotation(VaadinTableColumn.class);
	        	int i = tc.order();
	        	while(sm.containsKey(i)) {
	        		i++;
	        	}
	        	sm.put(i, new VaadinTableColumnWrapper(tc, field.getName()));
	        }
		}
		return sm.values();
	}
	
	public Map<String, VaadinTableColumns> getTableColumns() {
		return tableColumns;
	}

	public Map<String, VaadinTableWrapper> getTables() {
		return tables;
	}

	public Map<String, FormFields> getFormFields() {
		return formFields;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Map<String, Object> repos =  applicationContext.getBeansWithAnnotation(RepositoryRestResource.class);
		
		for(Map.Entry<String, Object> es: repos.entrySet()) {
			char c = Character.toUpperCase(es.getKey().charAt(0));
			String nn = c + es.getKey().substring(1);
			repositories.put(nn, es.getValue());
		}
	}

}
