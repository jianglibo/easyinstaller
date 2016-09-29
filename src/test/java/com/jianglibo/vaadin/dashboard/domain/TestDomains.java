package com.jianglibo.vaadin.dashboard.domain;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import com.google.common.collect.Sets;
import com.jianglibo.vaadin.dashboard.Tbase;
import com.jianglibo.vaadin.dashboard.repositories.BoxGroupRepository;
import com.jianglibo.vaadin.dashboard.repositories.BoxRepository;
import com.jianglibo.vaadin.dashboard.security.PersonVo;
import com.jianglibo.vaadin.dashboard.sshrunner.EnvForCodeExec;
import com.jianglibo.vaadin.dashboard.taskrunner.OneThreadTaskDesc;
import com.jianglibo.vaadin.dashboard.taskrunner.TaskDesc;

public class TestDomains extends Tbase {
	
	@Autowired
	private BoxGroupRepository boxGroupRepository;
	
	@Autowired
	private BoxRepository boxRepository;
	
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
			
			final BoxGroup finalBg = bg;
			
			Set<Box> boxes = bgOp.get().getBoxes().stream().map(box -> {
				Box boxInDb = boxRepository.findByIp(box.getIp());
				if (boxInDb != null) {
					boxRepository.delete(boxInDb);
				}
				box.setCreator(root);
				box.setBoxGroups(Sets.newHashSet(finalBg));
				return boxRepository.save(box);
			}).collect(Collectors.toSet());
			
			bg.setBoxes(boxes);
			boxGroupRepository.save(bg);
			
			Software sf = getSoftwareFixtures().get(0);
			Software sfInDb = softwareRepository.findByNameAndOstypeAndSversion(sf.getName(), sf.getOstype(), sf.getSversion());
			if (sfInDb != null) {
				softwareRepository.delete(sfInDb);
			}
			sf.setCreator(root);
			softwareRepository.save(sf);
			
			TaskDesc td = new TaskDesc(new PersonVo.PersonVoBuilder(getFirstPerson()).build(), bg, sf, "install", null);
			
			OneThreadTaskDesc ottd = td.createOneThreadTaskDescs().get(0);
			
			EnvForCodeExec efce = new EnvForCodeExec(ottd, "/opt/easyinstaller");
			
			String yml = ymlObjectMapper.writeValueAsString(efce);
			printme(yml);
			assertTrue(bgOp.isPresent());
		} catch (IOException e) {
			assertTrue("testBoxGroup", false);
		}
	}

}
