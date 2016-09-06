package com.jianglibo.vaadin.dashboard.util;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import com.jianglibo.vaadin.dashboard.Tutil;
import com.jianglibo.vaadin.dashboard.config.ApplicationConfigCustom;

public class TestYml {
	
	@Test
	public void tProperty() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Bean b = new Bean();
		Class<?> c = PropertyUtils.getPropertyType(b, "nb");
		assertThat(NestBean.class.getName(),equalTo(c.getName()));
		
	}
	
	@Test
	public void tApplicationCofigCustom() throws IllegalAccessException, InvocationTargetException {
		ApplicationConfigCustom acc = new ApplicationConfigCustom();
		acc.setUploadDst(null);
		Yaml yaml = new Yaml();
		String dumped = yaml.dumpAsMap(acc);
		
		acc = yaml.loadAs(dumped, ApplicationConfigCustom.class);
		Object o = yaml.load(dumped);
		BeanUtils.populate(acc, (Map<String, ? extends Object>) o);
		Tutil.printme(o);
	}
	
	@Test
	public void tNestBeanPopulater() {
		Bean b = new Bean("astring", 55);
		String dumpedmap = new Yaml().dumpAsMap(b);
		Map m = (Map) new Yaml().load(dumpedmap);
		Bean nb = new Bean();
		NestBeanPopulater.populate(nb, m, "nb");
		assertThat(nb.getNb().getI(), equalTo(66));
	}
	
	
	@Test
	public void tbeanutil() throws IllegalAccessException, InvocationTargetException {
		Bean b = new Bean("astring", 55);
		String dumpedmap = new Yaml().dumpAsMap(b);
		Map m = (Map) new Yaml().load(dumpedmap);
		
		Map nbm = (Map) m.get("nb");
		
		NestBean nestb = new NestBean();
		BeanUtils.populate(nestb, nbm);
		
		m.put("nb", nestb);
		
		Bean nb = new Bean();
		nb.setNb(new NestBean());
		BeanUtils.populate(nb, m);
		
		assertThat(b.getI(), equalTo(55));
		assertThat(b.getS(), equalTo("astring"));
		assertThat(b.getNb().getI(), equalTo(66));

	}

	@Test
	public void t(){
		Bean b = new Bean("astring", 55);
		String dumped = new Yaml().dump(b);
		String dumpedmap = new Yaml().dumpAsMap(b); 
		assertThat("should start with className",dumped, startsWith("!!"));
		assertThat("should start with className",dumpedmap, not(startsWith("!!")));
		b = new Yaml().loadAs(dumpedmap, Bean.class);
	}
	
	public static class NestBean {
		private String s;
		private int i;
		
		public NestBean(){}
		public NestBean(String s, int i) {
			super();
			this.s = s;
			this.i = i;
		}
		public String getS() {
			return s;
		}
		public void setS(String s) {
			this.s = s;
		}
		public int getI() {
			return i;
		}
		public void setI(int i) {
			this.i = i;
		}

	}
	
	public static class Bean {
		private String s;
		private int i;
		
		private NestBean nb = new NestBean("nests", 66);
		
		public Bean(){}
		
		public Bean(String s, int i) {
			super();
			this.s = s;
			this.i = i;
		}
		public String getS() {
			return s;
		}
		public void setS(String s) {
			this.s = s;
		}
		public int getI() {
			return i;
		}
		public void setI(int i) {
			this.i = i;
		}
		public NestBean getNb() {
			return nb;
		}
		public void setNb(NestBean nb) {
			this.nb = nb;
		}
		
		
	}
}
