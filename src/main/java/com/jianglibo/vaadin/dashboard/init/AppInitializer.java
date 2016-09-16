package com.jianglibo.vaadin.dashboard.init;

import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jianglibo.vaadin.dashboard.domain.Role;
import com.google.common.collect.Sets;
import com.jianglibo.vaadin.dashboard.domain.Person;
import com.jianglibo.vaadin.dashboard.repositories.RoleRepository;
import com.jianglibo.vaadin.dashboard.security.PersonManager;
import com.jianglibo.vaadin.dashboard.security.PersonVo;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.util.ColumnUtil;
import com.jianglibo.vaadin.dashboard.util.ThrowableUtil;
import com.jianglibo.vaadin.dashboard.vo.RoleNames;

@Component
public class AppInitializer implements InitializingBean {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private final PersonRepository userRepo;

	private final RoleRepository roleRepo;
	public final static String firstEmail = "root@is.localhost";

	@Autowired
	private PersonManager personManager;

	@Autowired
	public AppInitializer(PersonRepository userRepo, RoleRepository roleRepo) {
		this.userRepo = userRepo;
		this.roleRepo = roleRepo;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		initRoles();
		if (logger.isDebugEnabled()) {
			logger.debug("checking empty role users.");
		}
		initFisrstUser();
	}

	/**
	 * 
	 */
	@Transactional
	private void initFisrstUser() {
		try {
			Person person = userRepo.findByEmail(firstEmail);

			if (person == null) {
				Role ur = roleRepo.findByName(RoleNames.USER);
				Role superman = roleRepo.findByName(RoleNames.SUPERMAN);
				Set<Role> roles = Sets.newHashSet(ur, superman);
				PersonVo pvo = new PersonVo.PersonVoBuilder("root", firstEmail, "18888888888", "root").setAvatar(ColumnUtil.getFullIconPath("avatar.jpg"))
						.setAuthorities(roles).build();
				personManager.createUser(pvo);
			}
		} catch (Exception e) {
			logger.error("create init user throw exception: {}", ThrowableUtil.printToString(e));
		}
	}

	private void initRoles() {
		// @formatter:off
		RoleNames.allFields().stream().map(r -> new Role(r)).forEach(r -> {
			try {
				roleRepo.save(r);
			} catch (Exception e) {
				logger.info("role {} maybe exists, exception message is: {}", r.getName(), e.getMessage());
			}
		});
		// @formatter:on
	}
}
