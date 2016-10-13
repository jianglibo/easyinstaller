package com.jianglibo.vaadin.dashboard.domain;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import com.google.common.collect.Sets;
import com.jianglibo.vaadin.dashboard.Tbase;
import com.jianglibo.vaadin.dashboard.repositories.BoxGroupRepository;
import com.jianglibo.vaadin.dashboard.repositories.BoxRepository;
import com.jianglibo.vaadin.dashboard.security.PersonVo;
import com.jianglibo.vaadin.dashboard.service.AppObjectMappers;
import com.jianglibo.vaadin.dashboard.sshrunner.EnvForCodeExec;
import com.jianglibo.vaadin.dashboard.taskrunner.OneThreadTaskDesc;
import com.jianglibo.vaadin.dashboard.taskrunner.TaskDesc;
import com.jianglibo.vaadin.dashboard.util.SoftwareFolder;

public class TestDomains extends Tbase {
	
	@Autowired
	private BoxGroupRepository boxGroupRepository;
	
	@Autowired
	private BoxRepository boxRepository;
	
	@Autowired
	private AppObjectMappers appObjectMappers;
	
	private Path softwarePath = Paths.get("softwares");
	
	@After
	public void after() {
		List<BoxGroup> bgs = boxGroupRepository.findAll();
		bgs.forEach(bg -> {
			bg.getBoxes().stream().forEach(b -> {
				b.getBoxGroups().remove(bg);
				boxRepository.save(b);
			});
			bg.setBoxes(Sets.newHashSet());
			boxGroupRepository.delete(bg);
		});
		
		boxRepository.deleteAll();
		softwareRepository.deleteAll();
	}
	
	@Test
	public void testBoxGroup() {
		try {
			Resource[] resources = context.getResources("classpath:fixtures/domain/boxgroup.yaml");
			Optional<BoxGroup> bgOp = Stream.of(resources).map(r -> {
				try {
					return ymlObjectMapper.readValue(r.getInputStream(), BoxGroup.class);
				} catch (Exception e) {
					return null;
				}
			}).filter(b -> b != null).findFirst();
			
			Person root = getFirstPerson();
			
			BoxGroup bg = bgOp.get();
			
			BoxGroup bgInDb = boxGroupRepository.findByName(bg.getName()); 
			
			if (bgInDb != null) {
				bgInDb.getBoxes().forEach(b -> {
					b.getBoxGroups().remove(bgInDb);
					boxRepository.save(b);
				});
				boxGroupRepository.delete(bgInDb);
			}
			
			bg.setCreator(root);
			
			bg = boxGroupRepository.save(bg);
			
			final Set<BoxGroup> bgs = Sets.newHashSet(bg);
			
			Set<Box> boxes = bgOp.get().getBoxes().stream().map(box -> {
				Box boxInDb = boxRepository.findByIp(box.getIp());
				if (boxInDb != null) {
					boxRepository.delete(boxInDb);
				}
				box.setCreator(root);
				box.setBoxGroups(bgs);
				return boxRepository.save(box);
			}).collect(Collectors.toSet());
			
			bg.setBoxes(boxes);
			bgs.add(boxGroupRepository.save(bg));
			
			try (Stream<Path> folders = Files.list(softwarePath)) {
				folders.filter(Files::isDirectory).map(SoftwareFolder::new).filter(SoftwareFolder::isValid).forEach(sfolder -> {
					try {
						Software sf = ymlObjectMapper.readValue(sfolder.readDescriptionyml(), Software.class);
						sf.setName(sfolder.getName());
						sf.setOstype(sfolder.getOstype());
						sf.setSversion(sfolder.getSversion());
						sf.setConfigContent(sfolder.getConfigContent(sf.getConfigContent()));
						
						Software sfInDb = softwareRepository.findByNameAndOstypeAndSversion(sf.getName(), sf.getOstype(), sf.getSversion());
						if (sfInDb != null) {
							softwareRepository.delete(sfInDb);
						}
						sf.setCreator(root);
						softwareRepository.save(sf);
						TaskDesc td = new TaskDesc("", new PersonVo.PersonVoBuilder(getFirstPerson()).build(), bgs.iterator().next(),Sets.newHashSet(), sf, "install");
						
						OneThreadTaskDesc ottd = td.createOneThreadTaskDescs().get(0);
						
						EnvForCodeExec efce = new EnvForCodeExec.EnvForCodeExecBuilder(appObjectMappers, ottd, "/opt/easyinstaller").build();
						
						String yml = appObjectMappers.getYmlObjectMapper().writeValueAsString(efce);
						Path testFolder =  sfolder.getTestPath();
						Files.write(testFolder.resolve("envforcodeexec.yaml"), yml.getBytes());
						
						String xml = appObjectMappers.getXmlObjectMapper().writeValueAsString(efce);
						Files.write(testFolder.resolve("envforcodeexec.xml"), xml.getBytes());
						
						String json = appObjectMappers.getObjectMapper().writeValueAsString(efce);
						Files.write(testFolder.resolve("envforcodeexec.json"), json.getBytes());
						
						String[] ss = yml.split("\r?\n");
						String[] cc = Stream.of(ss).filter(s -> s.contains("configContent:")).collect(Collectors.toList()).toArray(new String[]{});
						int i = ss.length;
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			}
		} catch (IOException e) {
			assertTrue("testBoxGroup", false);
		}
	}
}
