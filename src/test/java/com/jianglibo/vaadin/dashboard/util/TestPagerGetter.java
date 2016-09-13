package com.jianglibo.vaadin.dashboard.util;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.jianglibo.vaadin.dashboard.util.HttpPageGetter.NewNew;

public class TestPagerGetter {

	@Test
	public void t() {
		HttpPageGetter hpg = new HttpPageGetter();
		hpg.after();
		
		List<NewNew> news = hpg.getNews();

		assertTrue(news.size() > 0);
		assertTrue(news.get(0).getUrl().contains("https://"));
		
	}
}
