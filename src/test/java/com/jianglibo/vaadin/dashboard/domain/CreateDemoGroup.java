package com.jianglibo.vaadin.dashboard.domain;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Sets;
import com.jianglibo.vaadin.dashboard.Tbase;
import com.jianglibo.vaadin.dashboard.repositories.BoxGroupRepository;
import com.jianglibo.vaadin.dashboard.repositories.BoxRepository;

public class CreateDemoGroup extends Tbase {

	@Autowired
	private BoxGroupRepository boxGroupRepository;
	
	@Autowired
	private BoxRepository boxRepository;
	
	
	@Test
	public void t() {
		final Person root = getFirstPerson();
		List<Box> boxes = Stream.of(0, 1, 2).map(i -> {
			Box box = new Box();
			box.setIp("192.168.33.1" + i);
			box.setName("box-" + i);
			box.setCreator(root);
			box.setOstype("CentOs7");
			return boxRepository.save(box);
		}).collect(Collectors.toList());
		
		BoxGroup bg = new BoxGroup.BoxGroupBuilder("firstGroup", root).setBoxes(Sets.newHashSet(boxes)).build();
		boxGroupRepository.save(bg);
		
		boxes.forEach(b -> {
			b.getBoxGroups().add(bg);
			boxRepository.save(b);
		});
	}
}
