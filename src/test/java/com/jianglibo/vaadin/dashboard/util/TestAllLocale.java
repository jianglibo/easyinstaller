package com.jianglibo.vaadin.dashboard.util;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.springframework.core.io.Resource;

import com.jianglibo.vaadin.dashboard.Tutil;

public class TestAllLocale {

	@Test
	public void tall() {
		Locale list[] = SimpleDateFormat.getAvailableLocales();
		
		for (int i = 0; i < list.length; i++) {
			Locale l = list[i];
			String cn = l.getDisplayCountry(new Locale("en"));
			String c = l.getCountry();
			String lan = l.getLanguage();
			Tutil.printme(cn + "," + c + "," + lan);
		}
	}
	
	@Test
	public void resources() {
		List<Resource> rss = ClassScanner.findResourceInPackage("messages", "/*.properties");
		Tutil.printme(rss.size());
	}
}
