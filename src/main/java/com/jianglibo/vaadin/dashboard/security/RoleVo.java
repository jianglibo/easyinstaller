package com.jianglibo.vaadin.dashboard.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;

import com.jianglibo.vaadin.dashboard.domain.Role;

public class RoleVo implements GrantedAuthority {

    /**
     * 
     */
    private static final long serialVersionUID = -8053409030105242773L;
    
    private long id;
    private String name;
    
    public RoleVo(Role role) {
        setId(role.getId());
        setName(role.getName());
    }

    public RoleVo(String authority) {
        setName(authority);
    }
    
    public static Set<RoleVo> convertFromRoles(Collection<Role> roles) {
        return roles.stream().map(r -> new RoleVo(r)).collect(Collectors.toSet());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
