package com.jianglibo.vaadin.dashboard.init;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jianglibo.vaadin.dashboard.domain.AppRole;
import com.jianglibo.vaadin.dashboard.domain.Person;
import com.jianglibo.vaadin.dashboard.repositories.AppRoleRepository;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.util.ThrowableUtil;
import com.jianglibo.vaadin.dashboard.vo.RoleNames;



@Component
public class CreateUserAndRoles implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final PersonRepository userRepo;

    private final AppRoleRepository roleRepo;
    
    public static final String firstEmail = "demo@demo.com";


    @Autowired
    public CreateUserAndRoles(PersonRepository userRepo, //
            AppRoleRepository roleRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initRoles();
        if (logger.isDebugEnabled()) {
            logger.debug("checking empty role users.");
        }
        initDemoUser();
    }

    /**
     * 
     */
    @Transactional
    private void initDemoUser() {
        try {
            Person shUser = userRepo.findByEmail(firstEmail);
            if (shUser == null) {
                AppRole r = roleRepo.findByName(RoleNames.USER);
                shUser = new Person(firstEmail, r);
                userRepo.save(shUser);
            }
        } catch (Exception e) {
            logger.error("create init user throw exception: {}", ThrowableUtil.printToString(e));
        }
    }


    private void initRoles() {
        // @formatter:off
        RoleNames.allFields().stream()
            .map(r -> new AppRole(r))
            .forEach(r -> {
                try {
                    roleRepo.save(r);
                } catch (Exception e) {
                    logger.info("role {} maybe exists, exception message is: {}", r.getName(), e.getMessage());
                }
            });
        // @formatter:on
    }
}
