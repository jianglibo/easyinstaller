package com.jianglibo.vaadin.dashboard.ssh;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import com.google.common.io.Files;
import com.jianglibo.vaadin.dashboard.Tbase;

public class TestStepLoader extends Tbase {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Test
	public void t() throws IOException {
		Resource[] resources = applicationContext.getResources("classpath:com/jianglibo/vaadin/dashboard/ssh/step/*");
		for(Resource rs: resources) {
			printme(Files.getNameWithoutExtension(rs.getFilename()));
		}
		
		assertThat("should have 2 resource", resources.length, equalTo(2));
	}

}
