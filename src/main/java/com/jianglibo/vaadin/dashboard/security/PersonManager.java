package com.jianglibo.vaadin.dashboard.security;

import java.util.UUID;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import com.jianglibo.vaadin.dashboard.domain.Person;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;

@Component
public class PersonManager implements UserDetailsManager {

    private static Pattern mobilePtn = Pattern.compile("^[0-9+-]+$");

    private static Logger logger = LoggerFactory.getLogger(PersonManager.class);

    private final PersonRepository personRepo;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonManager(AuthenticationManager authenticationManager, PersonRepository personRepo, PasswordEncoder passwordEncoder) {
        this.personRepo = personRepo;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public PersonVo loadUserByUsername(String emailOrMobile) throws UsernameNotFoundException {
        Person person;

        if (emailOrMobile.indexOf('@') != -1) {
            person = personRepo.findByEmail(emailOrMobile);
        } else if(mobilePtn.matcher(emailOrMobile).matches()) {
        	person = personRepo.findByName(emailOrMobile);
        } else{
            person = personRepo.findByMobile(emailOrMobile);
        }
        if (person == null) {
            throw new UsernameNotFoundException(emailOrMobile);
        }
        return new PersonVo.PersonVoBuilder(person).build();
    }

    @Override
    public void createUser(UserDetails user) {
        PersonVo personVo = (PersonVo) user;
        Person person = new Person(personVo, passwordEncoder.encode(personVo.getPassword()));
        if (person.getName() == null) {
            person.setName(UUID.randomUUID().toString().replaceAll("-", ""));
        }
        personRepo.save(person);
    }


    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();

        if (currentUser == null) {
            // This would indicate bad coding somewhere
            throw new AccessDeniedException("Can't change password as no Authentication object found in context " + "for current user.");
        }

        String username = currentUser.getName();
        // If an authentication manager has been set, re-authenticate the user with the
        // supplied password.
        if (authenticationManager != null) {
            logger.debug("Reauthenticating user '" + username + "' for password change request.");
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, oldPassword));
        } else {
            logger.debug("No authentication manager set. Password won't be re-checked.");
        }

        logger.debug("Changing password for user '" + username + "'");
        PersonVo pvo = (PersonVo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Person p = personRepo.findOne(pvo.getId());
        p.setPassword(passwordEncoder.encode(newPassword));
        personRepo.save(p);
        SecurityContextHolder.getContext().setAuthentication(createNewAuthentication(currentUser, newPassword));
        // userCache.removeUserFromCache(username);
    }

    protected Authentication createNewAuthentication(Authentication currentAuth, String newPassword) {
        UserDetails user = loadUserByUsername(currentAuth.getName());
        UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        newAuthentication.setDetails(currentAuth.getDetails());
        return newAuthentication;
    }

    public boolean userExists(String emailOrUsernameOrMobile, String tag) {
        Person person;
        switch (tag) {
        case "username":
            person = personRepo.findByName(emailOrUsernameOrMobile);
            break;
        case "email":
            person = personRepo.findByEmail(emailOrUsernameOrMobile);
            break;
        case "mobile":
            person = personRepo.findByMobile(emailOrUsernameOrMobile);
            break;
        default:
            person = null;
        }
        if (person == null) {
            return false;
        } else {
            return true;
        }
    }

    public Person findByName(String username) {
        return personRepo.findByName(username);
    }

    @Override
    public boolean userExists(String username) {
        return false;
    }
}
