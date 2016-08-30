package com.jianglibo.vaadin.dashboard;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.google.common.base.Charsets;
import com.google.common.collect.Sets;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import com.google.gwt.thirdparty.guava.common.collect.Maps;
import com.jianglibo.vaadin.dashboard.domain.InstallStepDefine;
import com.jianglibo.vaadin.dashboard.repositories.InstallStepRepository;
import com.jianglibo.vaadin.dashboard.ssh.StepConfig;

@Component
public class PreDefinedSteps {

	private static final Logger LOGGER = LoggerFactory.getLogger(PreDefinedSteps.class);

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private InstallStepRepository installStepRepository;

	@PostConstruct
	public void post() throws IOException {
		Map<String, Resource> allSteps = Maps.newHashMap();
		Set<String> stepNames = Sets.newHashSet();
		Resource[] classpathResources = applicationContext.getResources("classpath:com/jianglibo/vaadin/dashboard/ssh/step/*");
		Resource[] filepathResources = applicationContext.getResources("file:step/*");
		for (Resource rs : classpathResources) {
			String fn = rs.getFilename();
			allSteps.put(fn, rs);
			stepNames.add(Files.getNameWithoutExtension(fn));
		}
		for (Resource rs : filepathResources) {
			String fn = rs.getFilename();
			allSteps.put(fn, rs);
			stepNames.add(Files.getNameWithoutExtension(fn));
		}
		
		stepNames.forEach(sn -> {
			String ms = sn + ".yml";
			String cs = sn + ".code";
			if (allSteps.containsKey(ms) && allSteps.containsKey(cs)) {
				try {
					String s = CharStreams.toString(new InputStreamReader(allSteps.get(ms).getInputStream(), Charsets.UTF_8));
					String c = CharStreams.toString(new InputStreamReader(allSteps.get(cs).getInputStream(), Charsets.UTF_8));
					StepConfig stepConfig = new StepConfig(s);
					String name = stepConfig.getName();
					String ostype = stepConfig.getOstype();
					if (name == null || ostype == null) {
						LOGGER.error("{} must contains [name and ostype] item.", ms);
					} else {
						InstallStepDefine installStep = installStepRepository.findByNameAndOstype(name, ostype);
						if (installStep == null) {
							installStep = new InstallStepDefine();
						}
						installStep.setName(name);
						installStep.setOstype(ostype);
						installStep.setKvpairs(s);
						installStep.setCodeContent(c);
						installStepRepository.save(installStep);
					}
				} catch (Exception e) {
					LOGGER.error("{} must encoded in utf-8.", ms);
				}
			} else {
				LOGGER.error("found unnormal files in step package. [{}]", sn);
			}
		});
	}

//	public Map<String, String> translateStepMap(String s) throws IOException {
//		Map<String, Object> mp = (Map<String, Object>) yaml.load(s);
//		return null;
//		Map<String, String> map = Maps.newHashMap();
//		List<String> pairs = CharStreams.readLines(new StringReader(s));
//		pairs.forEach(kv -> {
//			String[] ss = kv.split("=", 2);
//			if (ss.length == 2) {
//				map.put(ss[0].trim(), ss[1].trim());
//			} else {
//				LOGGER.error("line '{}' can not split to a pair.", kv);
//			}
//		});
//		return map;
//	}
}
