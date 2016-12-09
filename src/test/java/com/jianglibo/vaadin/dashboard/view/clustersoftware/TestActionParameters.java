package com.jianglibo.vaadin.dashboard.view.clustersoftware;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.Tbase;
import com.jianglibo.vaadin.dashboard.service.AppObjectMappers;
import com.jianglibo.vaadin.dashboard.util.SoftwareUtil;

public class TestActionParameters extends Tbase {
	
	@Autowired
	private AppObjectMappers appObjectMappers;
	
	@Test
	public void testFormat() throws JsonParseException, JsonMappingException, IOException {
		Matcher m = ActionParameters.formatIndicatorPtn.matcher("# {{{YamL}}}");
		assertThat(m.matches(), equalTo(true));
		assertThat(m.group(1), equalTo("YamL"));
		
		
		m = ActionParameters.formatIndicatorPtn.matcher("# {{{{{YamL}}}}}}");
		assertThat(m.matches(), equalTo(true));
		assertThat(m.group(1), equalTo("YamL"));
		
		m = ActionParameters.formatIndicatorPtn.matcher("# {{{YamL}}}{{{xml}}}");
		assertThat(m.matches(), equalTo(true));
		assertThat(m.group(1), equalTo("YamL"));

//		List<String> lines = Lists.newArrayList();
//		lines.add("# {{{xml}}}");
//		lines.add("xxxx");
//		
//		JavaType jt = appObjectMappers.getYmlObjectMapper().getTypeFactory().constructParametrizedType(Map.class,Map.class, String.class, Object.class);
//		Map<String, Object> map = appObjectMappers.getYmlObjectMapper().readValue(Joiner.on(System.lineSeparator()).join(lines), jt);
		
	}
	
	
	@Test
	public void keyPtn() {
		boolean b = ActionParameters.levelOnePtn.matcher("akd-ksid:    ").matches();
		assertTrue("one", b);
		b = ActionParameters.levelOnePtn.matcher("akd-ksid:  #  ").matches();
		assertTrue("one", b);
		
		b = ActionParameters.levelOnePtn.matcher("akd-ksid:  #skdj kdsldsls#  ").matches();
		assertTrue("one", b);

		b = ActionParameters.levelOnePtn.matcher("akd-ksid:").matches();
		assertTrue("one", b);
		
		b = ActionParameters.levelOnePtn.matcher("#akd-ksid:").matches();
		assertTrue("one", !b);

		b = ActionParameters.levelOnePtn.matcher("  akd-ksid:").matches();
		assertTrue("one", !b);
		
		Matcher m = ActionParameters.levelOnePtn.matcher("akd-ksid:  #skdj kdsldsls#  ");
		b = m.matches();
		assertThat(m.group(1), equalTo("akd-ksid"));
		
	}
	
	@Test(expected = JsonMappingException.class)
	public void testValidJson() throws JsonParseException, JsonMappingException, JsonProcessingException, IOException {
		JavaType jt = appObjectMappers.getYmlObjectMapper().getTypeFactory().constructParametrizedType(Map.class,Map.class, String.class, Object.class);
		appObjectMappers.getObjectMapperNoIdent().writeValueAsString(appObjectMappers.getYmlObjectMapper().readValue("", jt));

	}
	
	@Test
	public void testValidYaml() throws JsonParseException, JsonMappingException, IOException {
		String yml = "abc:";
		JavaType jt = appObjectMappers.getYmlObjectMapper().getTypeFactory().constructParametrizedType(Map.class,Map.class, String.class, Object.class);
		Map<String, Object> map = appObjectMappers.getYmlObjectMapper().readValue(yml, jt);
		
		assertThat(map.get("abc"), equalTo(""));
		
		yml = "abc:          ";
		map = appObjectMappers.getYmlObjectMapper().readValue(yml, jt);
		assertThat(map.get("abc"), equalTo(""));
//invalid
//		yml = " ";
//		map = appObjectMappers.getYmlObjectMapper().readValue(yml, jt);
		
//invalid
//		yml = "";
//		map = appObjectMappers.getYmlObjectMapper().readValue(yml, jt);

// invalid
//		yml = "abc:#";
//		appObjectMappers.getYmlObjectMapper().readValue(yml, jt);
		
		yml = "abc: #";
		appObjectMappers.getYmlObjectMapper().readValue(yml, jt);
//invalid		
//		yml = "abc:abc #";
//		appObjectMappers.getYmlObjectMapper().readValue(yml, jt);
		
		yml = "abc: abc#";
		map = appObjectMappers.getYmlObjectMapper().readValue(yml, jt);
		assertThat(map.get("abc"), equalTo("abc#"));
		
		yml = "abc: abc #";
		map = appObjectMappers.getYmlObjectMapper().readValue(yml, jt);
		assertThat(map.get("abc"), equalTo("abc"));
		
		yml = "abc: \"abc #\"";
		map = appObjectMappers.getYmlObjectMapper().readValue(yml, jt);
		assertThat(map.get("abc"), equalTo("abc #"));
		
		yml = "abc:\r\n - a\r\n - b";
		map = appObjectMappers.getYmlObjectMapper().readValue(yml, jt);
		assertThat(map.get("abc").getClass(), equalTo(ArrayList.class));
		
		yml = "abc:\r\n -a\r\n -b";
		map = appObjectMappers.getYmlObjectMapper().readValue(yml, jt);
		assertThat(map.get("abc"), equalTo("-a -b"));
	}
	
	@Test
	public void testGetParameterYmlStr() throws JsonParseException, JsonMappingException, IOException {
		Path actionsyaml = Paths.get("fixtures", "installscripts","centos7-ps-2.7.3", "src", "actions.yaml");
		String content = Joiner.on(System.lineSeparator()).join(Files.readAllLines(actionsyaml, StandardCharsets.UTF_8));
		ActionParameters aps = new ActionParameters(appObjectMappers, content, SoftwareUtil.unparseLs(System.lineSeparator()));
		String ymlContent = aps.getParameterYmlStr("create-user");
		
		Map<String, Object> ms = aps.convertToMap(ymlContent);
		
		assertThat(ms, hasKey("rootpass"));
		assertThat(ms, hasKey("userpass"));
		assertThat(ms, hasKey("comment"));
		assertThat(ms, hasKey("user"));
	}
	
	@Test
	public void testComplexYmlStr() throws JsonParseException, JsonMappingException, IOException {
		List<String> lines = Lists.newArrayList();
		
		lines.add("abc:");
		lines.add("  ddd:");
		lines.add("    - a");
		
		ActionParameters aps = new ActionParameters(appObjectMappers, Joiner.on(System.lineSeparator()).join(lines), SoftwareUtil.unparseLs(System.lineSeparator()));
		String ymlContent = aps.getParameterYmlStr("abc");
		
		assertThat(ymlContent, equalTo(Joiner.on(System.lineSeparator()).join(Lists.newArrayList("ddd:", "  - a"))));
		
		Map<String, Object> ms = aps.convertToMap(ymlContent);
		assertThat(ms, hasKey("ddd"));
		
		List<String> ls = (List<String>) ms.get("ddd");
		assertThat(ls.get(0), equalTo("a"));
	}

}
