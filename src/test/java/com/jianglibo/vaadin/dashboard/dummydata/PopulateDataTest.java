package com.jianglibo.vaadin.dashboard.dummydata;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jianglibo.vaadin.dashboard.Tbase;
import com.jianglibo.vaadin.dashboard.Tutil;
import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.repositories.SoftwareRepository;

public class PopulateDataTest extends Tbase {

	@Autowired
	private SoftwareRepository softwareRepository;

	@Test
	public void software() {
		long count = softwareRepository.count();
		if (count > 90) {
			return;
		}
		for (String s : Tutil.randomStrings(100)) {
			Software sf = new Software();
			sf.setName(s);
		}
		softwareRepository
				.save(Tutil.randomStrings(100).stream().map(s -> new Software(s, "Centos")).collect(Collectors.toList()));
		
		count = softwareRepository.count();
		assertThat(count, equalTo(100L));
	}

}
