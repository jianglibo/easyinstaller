package com.jianglibo.vaadin.dashboard;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
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
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import com.google.gwt.thirdparty.guava.common.collect.Maps;
import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.domain.StepDefine;
import com.jianglibo.vaadin.dashboard.domain.OrderedStepDefine;
import com.jianglibo.vaadin.dashboard.repositories.OrderedStepDefineRepository;
import com.jianglibo.vaadin.dashboard.repositories.SoftwareRepository;
import com.jianglibo.vaadin.dashboard.repositories.StepDefineRepository;
import com.jianglibo.vaadin.dashboard.ssh.StepConfig;
import com.jianglibo.vaadin.dashboard.vo.NameOstype;

@Component
public class PreDefinedSteps {

	private static final Logger LOGGER = LoggerFactory.getLogger(PreDefinedSteps.class);

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private StepDefineRepository stepDefineRepository;

	@Autowired
	private OrderedStepDefineRepository orderedStepDefineRepository;

	@Autowired
	private SoftwareRepository softwareRepository;

	@Autowired
	private PreDefinedSoftwares preDefinedSoftwares;
	
	@Autowired
	private GlobalComboOptions globalComboOptions;

	@PostConstruct
	public void post() throws IOException {
		Map<String, Resource> allSteps = Maps.newHashMap();
		Set<String> stepNames = Sets.newHashSet();
		Resource[] classpathResources = applicationContext
				.getResources("classpath:com/jianglibo/vaadin/dashboard/ssh/step/*");
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
					String s = CharStreams
							.toString(new InputStreamReader(allSteps.get(ms).getInputStream(), Charsets.UTF_8));
					String c = CharStreams
							.toString(new InputStreamReader(allSteps.get(cs).getInputStream(), Charsets.UTF_8));
					StepConfig stepConfig = new StepConfig(s);
					String name = stepConfig.getName();
					String ostype = stepConfig.getOstype();
					String runner = stepConfig.getRunner();
					if (Strings.isNullOrEmpty(name) || Strings.isNullOrEmpty(ostype) || Strings.isNullOrEmpty(runner)) {
						LOGGER.error("{} must contains [name and ostype] item.", ms);
					} else {
						StepDefine stepDefine = stepDefineRepository.findByNameAndOstype(name, ostype);
						if (stepDefine == null) {
							stepDefine = new StepDefine();
						}
						stepDefine.setName(name);
						stepDefine.setOstype(ostype);
						stepDefine.setRunner(runner);
						stepDefine.setInfoContent(s);
						stepDefine.setCodeContent(c);
						stepDefineRepository.save(stepDefine);
					}
				} catch (Exception e) {
					LOGGER.error("{} must encoded in utf-8.", ms);
				}
			} else {
				LOGGER.error("found unnormal files in step package. [{}]", sn);
			}
		});

		insertPreDefinedSoftwares();
	}

	private void insertPreDefinedSoftwares() {
		preDefinedSoftwares.getPredefined().stream().forEach(pds -> {
			Software sw = softwareRepository.findByNameAndOstype(pds.getName(), pds.getOstype());
			List<NameOstype> nos = pds.getNameOstypes();
			List<StepDefine> sds = Lists.newArrayList();
			nos.stream().forEach(no -> {
				StepDefine sd = stepDefineRepository.findByNameAndOstype(no.getName(), no.getOstype());
				if (sd == null) {
					LOGGER.error("softwares->predefined->{}:{}, contains unknown stepdefine: {}: {}", pds.getName(),
							pds.getOstype(), no.getName(), no.getOstype());
				} else {
					sds.add(sd);
				}
			});
			

			
			// All step found
			if (nos.size() == sds.size()) {
				if (sw == null) {
					sw = new Software(pds.getName(), pds.getOstype());
				} else {
//					List<OrderedStepDefine> preExists = sw.getOrderedStepDefines();
//					sw.setOrderedStepDefines(Lists.newArrayList());
//					sw.getOrderedStepDefines().forEach(d -> {
//						orderedStepDefineRepository.delete(d);
//					});
				}
				List<OrderedStepDefine> ordered = Lists.newArrayList();
				for(int i = 0; i< sds.size(); i++) {
					OrderedStepDefine od = new OrderedStepDefine(sds.get(i), (i+1) * 50);
					orderedStepDefineRepository.save(od);
					ordered.add(od);
				}
//				sw.setOrderedStepDefines(ordered);
				softwareRepository.save(sw);
			}
		});
		
		Set<String> softwareNames = Sets.newHashSet();
		Set<String> ostypes = Sets.newHashSet();
		
		List<Software> softwares = softwareRepository.findAll();
		softwares.stream().forEach(sf -> {
			softwareNames.add(sf.getName());
			ostypes.add(sf.getOstype());
		});
		
		globalComboOptions.getWholeStringMap().put(GlobalComboOptions.SOFTWARE_NAMES, softwareNames);
		globalComboOptions.getWholeStringMap().put(GlobalComboOptions.OS_TYPES, ostypes);
		

	}

}
