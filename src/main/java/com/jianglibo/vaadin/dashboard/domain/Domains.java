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
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumn;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumnWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.repositories.RepositoryCommonCustom;
import com.jianglibo.vaadin.dashboard.repositories.RepositoryCommonMethod;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGrid;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumn;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumnWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.util.ClassScanner;
import com.jianglibo.vaadin.dashboard.util.SortUtil;

@Component
public class Domains implements ApplicationContextAware {

	Map<String, VaadinGridWrapper> grids = Maps.newHashMap();

	// Map<String, FormFields> formFields = Maps.newHashMap();

	Map<String, VaadinTableWrapper> tables = Maps.newHashMap();

	Map<String, Object> repositories = Maps.newHashMap();

	public RepositoryCommonMethod<?> getRepositoryCommonMethod(String className) {
		return (RepositoryCommonMethod<?>) getRepository(className);
	}

	public <T extends BaseEntity> RepositoryCommonCustom<T> getRepositoryCommonCustom(String className) {
		return (RepositoryCommonCustom<T>) getRepository(className);
	}

	public <T extends BaseEntity> JpaRepository<Long, T> getJpaRepository(String className) {
		return (JpaRepository<Long, T>) getRepository(className);
	}

	private Object getRepository(String className) {
		if (repositories.containsKey(className)) {
			return repositories.get(className);
		} else {
			return repositories.get(className + "Repository");
		}
	}

	@PostConstruct
	void after() throws ClassNotFoundException, IOException {

		List<Class<?>> clazzesWithVt = ClassScanner.findAnnotatedBy("com.jianglibo.vaadin.dashboard.domain",
				VaadinTable.class);

		List<Class<?>> clazzesWithVg = ClassScanner.findAnnotatedBy("com.jianglibo.vaadin.dashboard.domain",
				VaadinGrid.class);

		Map<Class<?>, List<VaadinFormFieldWrapper>> ffmap = Maps.newHashMap();

		for (Class<?> clazz : clazzesWithVt) {
			VaadinTable vt = clazz.getAnnotation(VaadinTable.class);
			VaadinTableWrapper vtw = new VaadinTableWrapper(vt, clazz);
			vtw.setColumns(processOneTableColumn(clazz, vt));
			List<VaadinFormFieldWrapper> ff = Lists.newArrayList(processOneTableForm(clazz));
			vtw.setFormFields(ff);
			ffmap.put(clazz, ff);
			tables.put(vtw.getName(), vtw);
		}

		for (Class<?> clazz : clazzesWithVg) {
			VaadinGrid vg = clazz.getAnnotation(VaadinGrid.class);
			VaadinGridWrapper vgw = new VaadinGridWrapper(vg, clazz);
			vgw.setColumns(processOneGridColumn(clazz, vg));
			vgw.setFormFields(ffmap.get(clazz));
			grids.put(vgw.getName(), vgw);
		}
	}

	private List<VaadinGridColumnWrapper> processOneGridColumn(Class<?> clazz, VaadinGrid vg) {
		SortedMap<Integer, VaadinGridColumnWrapper> sm = Maps.newTreeMap();

		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(VaadinGridColumn.class)) {
				VaadinGridColumn vgc = field.getAnnotation(VaadinGridColumn.class);
				int i = vgc.order();
				while (sm.containsKey(i)) {
					i++;
				}
				sm.put(i, new VaadinGridColumnWrapper(vgc, field));
			}
		}

		for (Field field : clazz.getSuperclass().getDeclaredFields()) {
			if (field.isAnnotationPresent(VaadinGridColumn.class)) {
				if (!vg.showCreatedAt()) {
					if ("createdAt".equals(field.getName())) {
						continue;
					}
				}
				VaadinGridColumn vgc = field.getAnnotation(VaadinGridColumn.class);
				int i = vgc.order();
				while (sm.containsKey(i)) {
					i++;
				}
				sm.put(i, new VaadinGridColumnWrapper(vgc, field));
			}
		}
		return Lists.newArrayList(sm.values());
	}

	private Collection<VaadinFormFieldWrapper> processOneTableForm(Class<?> clazz) {
		SortedMap<Integer, VaadinFormFieldWrapper> vfs = Maps.newTreeMap();

		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(VaadinFormField.class)) {
				VaadinFormField vf = field.getAnnotation(VaadinFormField.class);
				int i = vf.order();
				while (vfs.containsKey(i)) {
					i++;
				}
				vfs.put(i, new VaadinFormFieldWrapper(vf, field));
			}
		}

		for (Field field : clazz.getSuperclass().getDeclaredFields()) {
			if (field.isAnnotationPresent(VaadinFormField.class)) {
				VaadinFormField vf = field.getAnnotation(VaadinFormField.class);
				int i = vf.order();
				while (vfs.containsKey(i)) {
					i++;
				}
				vfs.put(i, new VaadinFormFieldWrapper(vf, field));
			}
		}
		return vfs.values();
	}

	private List<VaadinTableColumnWrapper> processOneTableColumn(Class<?> clazz, VaadinTable vt) {
		SortedMap<Integer, VaadinTableColumnWrapper> sm = Maps.newTreeMap();

		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(VaadinTableColumn.class)) {
				VaadinTableColumn tc = field.getAnnotation(VaadinTableColumn.class);
				int i = tc.order();
				while (sm.containsKey(i)) {
					i++;
				}
				sm.put(i, new VaadinTableColumnWrapper(tc, field));
			}
		}

		for (Field field : clazz.getSuperclass().getDeclaredFields()) {
			if (field.isAnnotationPresent(VaadinTableColumn.class)) {
				if (!vt.showCreatedAt()) {
					if ("createdAt".equals(field.getName())) {
						continue;
					}
				}
				VaadinTableColumn tc = field.getAnnotation(VaadinTableColumn.class);
				int i = tc.order();
				while (sm.containsKey(i)) {
					i++;
				}
				sm.put(i, new VaadinTableColumnWrapper(tc, field));
			}
		}
		return Lists.newArrayList(sm.values());
	}

	public List<VaadinFormFieldWrapper> getFormFields(Class<?> clazz) {

		if (getTables().get(clazz.getSimpleName()) != null) {
			return getTables().get(clazz.getSimpleName()).getFormFields();
		} else {
			return getGrids().get(clazz.getSimpleName()).getFormFields();
		}
	}

	public Sort getDefaultSort(Class<?> clazz) {

		if (getTables().get(clazz.getSimpleName()) != null) {
			return SortUtil.fromString(getTables().get(clazz.getSimpleName()).getVt().defaultSort());
		} else {
			return SortUtil.fromString(getGrids().get(clazz.getSimpleName()).getVg().defaultSort());
		}
	}

	public Map<String, VaadinTableWrapper> getTables() {
		return tables;
	}

	public Map<String, VaadinGridWrapper> getGrids() {
		return grids;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Map<String, Object> repos = applicationContext.getBeansWithAnnotation(RepositoryRestResource.class);

		for (Map.Entry<String, Object> es : repos.entrySet()) {
			char c = Character.toUpperCase(es.getKey().charAt(0));
			String nn = c + es.getKey().substring(1);
			repositories.put(nn, es.getValue());
		}
	}

}
