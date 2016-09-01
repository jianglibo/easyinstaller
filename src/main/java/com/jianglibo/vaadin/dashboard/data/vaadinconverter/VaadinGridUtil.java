package com.jianglibo.vaadin.dashboard.data.vaadinconverter;

import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

import com.google.common.collect.Maps;
import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.GridFieldDescription;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;

public class VaadinGridUtil {

	public static GridMeta setupGrid(ApplicationContext applicationContext, String[] allcolnames, MessageSource messageSource, VaadinTableWrapper vtw, String...generatedFields) {
		Map<String, String> convertormap = Maps.newHashMap();
		List<String> colnames = Lists.newArrayList(generatedFields);
		
		for(String s : allcolnames) {
			if (s.contains("!")) {
				String[] ss = s.split("!", 2);
				colnames.add(ss[0]);
				convertormap.put(ss[0], ss[1]);
			} else {
				colnames.add(s);
			}
		}
		
		Grid grid = new Grid();
		grid.setColumns(colnames.toArray(new String[]{}));
		grid.setSelectionMode(SelectionMode.NONE);
		for(String cn : colnames){
			Grid.Column col = grid.getColumn(cn);
			if (convertormap.containsKey(cn)) {
				Converter cv = (Converter) applicationContext.getBean(convertormap.get(cn));
				col.setConverter(cv);
			}
			col.setHeaderCaption(MsgUtil.getFieldMsg(messageSource, vtw.getVt().messagePrefix(), cn));
		}
		return new GridMeta(colnames, grid, convertormap);
	}
	
	public static class GridMeta {
		private String[] colnames;
		private Grid grid;
		private Map<String, String> convertormap;
		
		public GridMeta(List<String> colnames, Grid grid, Map<String, String> convertormap) {
			super();
			this.colnames = colnames.toArray(new String[]{});
			this.grid = grid;
			this.convertormap = convertormap;
		}
		public String[] getColnames() {
			return colnames;
		}
		public void setColnames(String[] colnames) {
			this.colnames = colnames;
		}
		public Grid getGrid() {
			return grid;
		}
		public void setGrid(Grid grid) {
			this.grid = grid;
		}
		public Map<String, String> getConvertormap() {
			return convertormap;
		}
		public void setConvertormap(Map<String, String> convertormap) {
			this.convertormap = convertormap;
		}
		
		
	}
	
}
