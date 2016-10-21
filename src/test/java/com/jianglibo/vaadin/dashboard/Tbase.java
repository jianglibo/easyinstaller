package com.jianglibo.vaadin.dashboard;


import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.VaadinApplication;
import com.jianglibo.vaadin.dashboard.config.ApplicationConfig;
import com.jianglibo.vaadin.dashboard.domain.Role;
import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.domain.Person;
import com.jianglibo.vaadin.dashboard.init.AppInitializer;
import com.jianglibo.vaadin.dashboard.repositories.RoleRepository;
import com.jianglibo.vaadin.dashboard.repositories.SoftwareRepository;
import com.jianglibo.vaadin.dashboard.security.PersonVo;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.vo.RoleNames;


/**
 * @author jianglibo@gmail.com
 *         2015年8月17日
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VaadinApplication.class)
@WebIntegrationTest
public abstract class Tbase {

    private static final String[] defaultRns = { RoleNames.USER };

    protected MockMvc mvc;

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    protected PersonRepository personRepository;
    
    @Autowired
    protected SoftwareRepository softwareRepository;

    @Autowired
    protected RoleRepository roleRepo;
    
    @Autowired
    protected ObjectMapper ymlObjectMapper;
    
    @Autowired
    protected ApplicationConfig applicationConfig;

	public static void printme(Object o) {
		System.out.println(o);
	}
	
    @Before
    public void before() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        if (personRepository.findByEmail(AppInitializer.firstEmail) == null) {
            createAuser();
        }
    }
    
    public Person getFirstPerson() {
    	return personRepository.findByEmail(AppInitializer.firstEmail);
    }
    
    public Person createAuser() {
        return createOneUserWithEmail(AppInitializer.firstEmail, RoleNames.USER);
    }

    @After
    public void defaultAfter() {
        mvc = null;
    }

    public Role findRole(String rn) {
        return roleRepo.findByName(rn);
    }

    public Person createOneUserWithEmail(String email, String... roleNames) {
        if (roleNames.length == 0) {
            roleNames = defaultRns;
        }
        Set<Role> roles = new HashSet<>(Stream.of(roleNames).map(rn -> roleRepo.findByName(rn)).collect(Collectors.toList()));
        PersonVo pvo = new PersonVo.PersonVoBuilder(UUID.randomUUID().toString(), email, UUID.randomUUID().toString(), UUID.randomUUID().toString()).setAuthorities(roles).build();
        Person shUser =  new Person(pvo, UUID.randomUUID().toString());
        return personRepository.save(shUser);
        
    }

    public String getRestUri(String uri) {
        if (!uri.startsWith("/")) {
            uri = "/" + uri;
        }
        return getApiPrefix() + uri;
    }

    public String getApiPrefix() {
        return "/api/v1";
    }
}
