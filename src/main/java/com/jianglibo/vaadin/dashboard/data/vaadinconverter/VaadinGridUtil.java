package com.jianglibo.vaadin.dashboard.data.vaadinconverter;

import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

import com.google.common.collect.Maps;
import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.Grid;

public class VaadinGridUtil {

	public static void setupColumns(ApplicationContext applicationContext, String[] allcolnames, Grid grid, MessageSource messageSource, VaadinTableWrapper vtw) {
		Map<String, String> convertormap = Maps.newHashMap();
		List<String> colnames = Lists.newArrayList();
		
		for(String s : allcolnames) {
			if (s.contains("!")) {
				String[] ss = s.split("!", 2);
				colnames.add(ss[0]);
				convertormap.put(ss[0], ss[1]);
			} else {
				colnames.add(s);
			}
		}
		
		
		grid.setColumns(colnames);
		
		for(String cn : colnames){
			Grid.Column col = grid.getColumn(cn);
			if (convertormap.containsKey(cn)) {
				Converter cv = (Converter) applicationContext.getBean(convertormap.get(cn));
				col.setConverter(cv);
			}
			col.setHeaderCaption(MsgUtil.getFieldMsg(messageSource, vtw.getVt().messagePrefix(), cn));
		}
	}
	
}
