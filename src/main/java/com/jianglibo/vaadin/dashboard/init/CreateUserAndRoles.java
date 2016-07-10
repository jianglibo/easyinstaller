package com.jianglibo.vaadin.dashboard.init;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jianglibo.vaadin.dashboard.domain.ShellExecRole;
import com.jianglibo.vaadin.dashboard.domain.ShellExecUser;
import com.jianglibo.vaadin.dashboard.repositories.ShellExecRoleRepository;
import com.jianglibo.vaadin.dashboard.repositories.ShellExecUserRepository;
import com.jianglibo.vaadin.dashboard.util.ThrowableUtil;
import com.jianglibo.vaadin.dashboard.vo.RoleNames;



@Component
public class CreateUserAndRoles implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final ShellExecUserRepository userRepo;

    private final ShellExecRoleRepository roleRepo;
    
    public static final String firstEmail = "demo@demo.com";


    @Autowired
    public CreateUserAndRoles(ShellExecUserRepository userRepo, //
            ShellExecRoleRepository roleRepo) {
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
            ShellExecUser shUser = userRepo.findByEmail(firstEmail);
            if (shUser == null) {
                ShellExecRole r = roleRepo.findByName(RoleNames.USER);
                shUser = new ShellExecUser(firstEmail, r);
                userRepo.save(shUser);
            }
        } catch (Exception e) {
            logger.error("create init user throw exception: {}", ThrowableUtil.printToString(e));
        }
    }


    private void initRoles() {
        // @formatter:off
        RoleNames.allFields().stream()
            .map(r -> new ShellExecRole(r))
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
