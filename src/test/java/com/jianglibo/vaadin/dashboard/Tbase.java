package com.jianglibo.vaadin.dashboard;


import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.jianglibo.vaadin.dashboard.VaadinApplication;
import com.jianglibo.vaadin.dashboard.domain.AppRole;
import com.jianglibo.vaadin.dashboard.domain.Person;
import com.jianglibo.vaadin.dashboard.init.CreateUserAndRoles;
import com.jianglibo.vaadin.dashboard.repositories.AppRoleRepository;
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
//
//    @Autowired
//    protected AccessQuotaRepository aqRepo;
//
//    @Autowired
//    protected TaskItemRepository tiRepo;
//
//    @Autowired
//    protected AppRepository appRepo;
//    
//    @Autowired
//    protected AppLinkRepository appLinkRepo;
//
//    @Autowired
//    protected ReportTargetRepository rtRepo;
//
//    @Autowired
//    protected AppVersionRepository appVersionRepo;
//
    @Autowired
    protected WebApplicationContext context;

    @Autowired
    protected PersonRepository userRepo;

    @Autowired
    protected AppRoleRepository roleRepo;

//    @Autowired
//    protected KeypairRepository kpRepo;
//    
//    @Autowired
//    protected LoopholeRepository loopholeRepo;
//    
//    @Autowired
//    protected FoundedLoopholeRepository flRepo;
//    
//    @Autowired
//    private ObjectMapper objectMapper;
//    
//    public ObjectMapper getObjectMapper() {
//        return objectMapper;
//    }
//
    
	public static void printme(Object o) {
		System.out.println(o);
	}
	
    @Before
    public void before() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        if (userRepo.findByEmail(CreateUserAndRoles.firstEmail) == null) {
            createAuser();
        }
    }

//    protected void deleteAllKps() {
//        loginBuser();
//        kpRepo.findAll().forEach(kp -> kpRepo.delete(kp));
//        logout();
//    }
//
//    protected void deleteAllAppLinks() {
//        loginBuser();
//        appLinkRepo.findAll().forEach(al -> appLinkRepo.delete(al));
//        logout();
//    }
//
//    protected void deleteAllApps() {
//        loginBuser();
//        appRepo.findAll().forEach(a -> appRepo.delete(a));
//        logout();
//    }
//
//    protected void deleteAllAppVersions() {
//        loginBuser();
//        appVersionRepo.findAll().forEach(av -> appVersionRepo.delete(av));
//        logout();
//    }
//
//    protected void deleteAllUsers() {
//        deleteAllTaskItems();
//        deleteAllAppLinks();
//        deleteAllAppVersions();
//        deleteAllApps();
//        deleteAllReportTargets();
//        deleteAllKps();
//        loginBuser();
//        userRepo.findAll().forEach(a -> userRepo.delete(a));
//        logout();
//    }
//
//    protected void deleteAllTaskItems() {
//        loginBuser();
//        tiRepo.findAll().forEach(ti -> deleteOneItem(ti));
//        logout();
//    }
//
//    protected void deleteOneItem(TaskItem ti) {
//        ti.setAppVersion(null);
//        tiRepo.delete(ti);
//    }
//
//    public void deleteAllReportTargets() {
//        loginBuser();
//        rtRepo.findAll().forEach(rt -> rtRepo.delete(rt));
//        logout();
//    }
//
    public Person createAuser() {
        return createOneUserWithEmail(CreateUserAndRoles.firstEmail, RoleNames.USER, RoleNames.WORKER);
    }
    
//    public ShellExecUser createBuser() {
//        return createOneUserWithOpenId(userBucOpenId, RoleNames.USER, RoleNames.USER_MANAGER, RoleNames.WORKER);
//    }
//
//    public void dropAuser() {
//        loginBuser();
//        userRepo.delete(getAuser());
//        logout();
//    }
//
//    public void dropBuser() {
//        userRepo.delete(getBuser());
//    }
//
    @After
    public void defaultAfter() {
        mvc = null;
    }
//
//    public void loginAuser() {
//        ScanhubSecurityUtil.doLogin(getAuser());
//    }
//
//    public void login(ScanhubUser user) {
//        ScanhubSecurityUtil.doLogin(user);
//    }
//
//    public void loginBuser() {
//        ScanhubSecurityUtil.doLogin(getBuser());
//    }
//
//    public void logout() {
//        SecurityContextHolder.clearContext();
//    }
//
//    public ScanhubUser getAuser() {
//        ScanhubUser auser = userRepo.findByUcOpenId(Tbase.userAucOpenId);
//        if (auser == null) {
//            auser = createAuser();
//        }
//        return auser;
//    }
//
//    public ScanhubUser getBuser() {
//        ScanhubUser buser = userRepo.findByUcOpenId(Tbase.userBucOpenId);
//        if (buser == null) {
//            buser = createBuser();
//        }
//        if (!buser.getRoles().stream().anyMatch(r -> RoleNames.USER_MANAGER.equals(r.getName()))) {
//            buser.getRoles().add(findRole(RoleNames.USER_MANAGER));
//        }
//        return buser;
//    }
//
//    public Pageable createApageable(int page, int size) {
//        return new PageRequest(page, size);
//    }
//
//    public Pageable createApageable(int size) {
//        return createApageable(0, size);
//    }
//
//    public Pageable createApageable() {
//        return createApageable(10);
//    }
//
    public AppRole findRole(String rn) {
        return roleRepo.findByName(rn);
    }
//
//    public ScanhubUser createOneUser(String... roleNames) {
//        String uuid = UuidUtil.uuidNoDash();
//
//        Set<String> roles = Sets.newHashSet(roleNames);
//
//        if (!roles.contains(RoleNames.USER)) {
//            roles.add(RoleNames.USER);
//        }
//
//        return createOneUserWithOpenId(uuid, roles.toArray(new String[] {}));
//    }
//
    public Person createOneUserWithEmail(String email, String... roleNames) {
    	
    	
        Person shUser = new Person(email);
        shUser.setRoles(new HashSet<>(Stream.of(roleNames).map(rn -> roleRepo.findByName(rn)).collect(Collectors.toList())));
        if (roleNames.length == 0) {
            roleNames = defaultRns;
        }
        shUser.setRoles(Stream.of(roleNames).map(rn -> findRole(rn)).collect(Collectors.toSet()));
        return userRepo.save(shUser);
    }
//
//    public Authentication getUserauthentication(ScanhubUser user) {
//        ScanhubLoginAuthenticationToken saut = new ScanhubLoginAuthenticationToken(new ScanhubUserVo(user));
//        saut.setAuthenticated(true);
//        return saut;
//    }
//
//    public Authentication getUserAauthentication() {
//        return getUserauthentication(getAuser());
//    }
//
//    public Authentication getUserBauthentication() {
//        return getUserauthentication(getBuser());
//    }
//
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
