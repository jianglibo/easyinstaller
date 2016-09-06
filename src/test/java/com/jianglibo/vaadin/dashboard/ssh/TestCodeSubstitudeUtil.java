package com.jianglibo.vaadin.dashboard.ssh;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import com.google.common.collect.Lists;
import com.google.gwt.thirdparty.guava.common.collect.Maps;

public class TestCodeSubstitudeUtil {
	
	@Test
	public void yml() {
		Yaml yaml = new Yaml();
		Object m = yaml.loadAs("a:\n  b:\n    - 1\n    - 2\n    - 3\n    - 4\n", Map.class);
		m = yaml.load(this.getClass().getResourceAsStream("/codecfg-sample.yml"));
		assertTrue(m instanceof Map);
		Map a = (Map) ((Map) m).get("a");
		Object b = a.get("b");
		assertTrue(b instanceof List);
	}
	
	@Test
	public void arrayptn() {
		Matcher m = CodeSubstitudeUtil.ARRAY_VAR_PTN.matcher("a[0]");
		m.matches();
		assertThat(m.group(1), equalTo("a"));
		assertThat(m.group(2), equalTo("[0]"));
	}
	
	@Test
	public void tre() {
		Pattern p = Pattern.compile("\\]%([a-zA-Z]+(\\.[a-zA-Z]+|\\[\\d+\\])*?)+%\\[");
		Matcher m = p.matcher("]%abc[0].ddd%[");
		assertTrue(m.find());
		assertThat(m.group(1), equalTo("abc[0].ddd"));

		m = p.matcher("]%abc[0][0].ddd%[");
		assertTrue(m.find());

		m = p.matcher("]%abc.%[");
		assertTrue(!m.find());
		
		m = p.matcher("]%abc[0].abc[0].ddd%[");
		assertTrue(m.find());
		assertThat(m.group(1), equalTo("abc[0].abc[0].ddd"));

	}
	
	@Test
	public void tappendreplace() {
		 Pattern p = Pattern.compile("cat");
		 Matcher m = p.matcher("one cat two cats in the yard");
		 StringBuffer sb = new StringBuffer();
		 while (m.find()) {
		     m.appendReplacement(sb, "dog");
		 }
		 m.appendTail(sb);
		 System.out.println(sb.toString());
	}
	
	@Test
	public void tsplit() {
		String key = "a.b.c";
		String[] ss = key.split("\\.");
		assertThat(Lists.newArrayList(ss), contains("a", "b", "c"));
	}

	@Test
	public void  t1() {
		String s = "ax ]%b%[ c";
		Map<String, Object> map = Maps.newHashMap();
		map.put("b", "charb");
		String ns = CodeSubstitudeUtil.process(s, map);
		assertThat(ns, equalTo("ax charb c"));
	}
	
	@Test
	public void  tMulitpleChars() {
		String s = "ax ]%bb%[ c";
		Map<String, Object> map = Maps.newHashMap();
		map.put("bb", "charb");
		String ns = CodeSubstitudeUtil.process(s, map);
		assertThat(ns, equalTo("ax charb c"));
	}
	
	@Test
	public void  tNested() {
		String s = "ax ]%bb.c%[ c";
		Map<String, Object> map = Maps.newHashMap();
		Map<String, Object> mapn1 = Maps.newHashMap();
		mapn1.put("c", "charb");
		map.put("bb", mapn1);
		String ns = CodeSubstitudeUtil.process(s, map);
		assertThat(ns, equalTo("ax charb c"));
	}
	
	@Test
	public void  tNotMatchs() {
		String s = "ax ]%ba%[ c";
		Map<String, Object> map = Maps.newHashMap();
		map.put("kkk", "charb");
		String ns = CodeSubstitudeUtil.process(s, map);
		assertThat(ns, equalTo(s));
	}
	
	@Test
	public void  tList() {
		String s = "ax ]%b[0]%[ c";
		Map<String, Object> map = Maps.newHashMap();
		map.put("b", Lists.newArrayList("charb"));
		String ns = CodeSubstitudeUtil.process(s, map);
		assertThat(ns, equalTo("ax charb c"));
	}
	
	@Test
	public void  tListNested() {
		Yaml yaml = new Yaml();
		Map m = (Map) yaml.load(this.getClass().getResourceAsStream("/codecfg-sample.yml"));
		String s = "ax ]%root.listLevelOne[0].listLevelTwo[2]%[ c";
		String ns = CodeSubstitudeUtil.process(s, m);
		assertThat(ns, equalTo("ax 3 c"));
	}
}
