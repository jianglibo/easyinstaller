package com.jianglibo.vaadin.dashboard.annotation;

import java.util.List;

import org.springframework.context.MessageSource;

import com.google.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;

public class VaadinTableWrapper {
	
	private VaadinTable vt;
	
	private Class<?> clazz;
	
	private List<VaadinTableColumnWrapper> columns;
	
	private List<VaadinFormFieldWrapper> formFields;

	public VaadinTableWrapper(VaadinTable vt, Class<?> clazz) {
		super();
		this.vt = vt;
		this.clazz = clazz;
	}

	public List<VaadinTableColumnWrapper> getColumns() {
		return columns;
	}

	public void setColumns(List<VaadinTableColumnWrapper> columns) {
		this.columns = columns;
	}

	public List<VaadinFormFieldWrapper> getFormFields() {
		return formFields;
	}

	public void setFormFields(List<VaadinFormFieldWrapper> formFields) {
		this.formFields = formFields;
	}

	public VaadinTable getVt() {
		return vt;
	}

	public void setVt(VaadinTable vt) {
		this.vt = vt;
	}

	public String getName() {
		return clazz.getSimpleName();
	}


	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	public List<String> getSortableContainerPropertyIds() {
		List<String> sortables = Lists.newArrayList();
		for(VaadinTableColumnWrapper vcw: getColumns()) {
			if (vcw.getVtc().sortable()) {
				sortables.add(vcw.getName());
			}
		}
		return sortables;
	}
	
	public String[] getVisibleColumns() {
		List<String> visibles = Lists.newArrayList();
		for(VaadinTableColumnWrapper tc : getColumns()) {
			if (tc.getVtc().visible()) {
				visibles.add(tc.getName());
			}
		}
		return visibles.toArray(new String[]{});
	}
	
	public String[] getAutoCollapseColumns() {
		List<String> visibles = Lists.newArrayList();
		for(VaadinTableColumnWrapper tc : columns) {
			if (tc.getVtc().autoCollapsed()) {
				visibles.add(tc.getName());
			}
		}
		return visibles.toArray(new String[]{});
	}

	
	public String[] getColumnHeaders(VaadinTableWrapper vtw ,MessageSource messageSource) {
		List<String> headers = Lists.newArrayList();
		for(VaadinTableColumnWrapper vtcw : columns) {
			if (vtcw.getVtc().visible()) {
				headers.add(MsgUtil.getFieldMsg(messageSource, vtw.getVt().messagePrefix(), vtcw));
			}
		}
		return headers.toArray(new String[]{});
	}
}
