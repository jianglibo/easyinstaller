package com.jianglibo.vaadin.dashboard.domain;

import java.util.Set;

import com.google.common.collect.Sets;

@SuppressWarnings("serial")
public class Ugroup extends BaseEntity {
	
	private String name;
	
	private Set<Person> people = Sets.newHashSet();
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Person> getPeople() {
		return people;
	}

	public void setPeople(Set<Person> people) {
		this.people = people;
	}

	@Override
	public String getDisplayName() {
		return null;
	}

}
