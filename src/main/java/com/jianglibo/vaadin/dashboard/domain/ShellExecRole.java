package com.jianglibo.vaadin.dashboard.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "shrole", uniqueConstraints = { @UniqueConstraint(columnNames = "name") })
public class ShellExecRole extends BaseEntity implements GrantedAuthority {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(nullable = false)
    private String name;

    public ShellExecRole() {
    };

    public ShellExecRole(String authority) {
        setName(authority);
    }
    
    
    @PrePersist
    public void beforePersist() {
    	String n = getName().toUpperCase();
    	if (!n.startsWith("ROLE_")) {
    		n = "ROLE_" + n;
    	}
    	setName(n);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    @JsonIgnore
    public String getAuthority() {
        return name;
    }
}
