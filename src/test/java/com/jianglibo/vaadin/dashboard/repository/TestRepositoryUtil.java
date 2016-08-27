package com.jianglibo.vaadin.dashboard.repository;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.jianglibo.vaadin.dashboard.repositories.RepositoryUtil;

public class TestRepositoryUtil {

	@Test
	public void t() {
		assertThat(RepositoryUtil.roundLike("a"), equalTo("%a%"));
	}
}
