package com.jianglibo.vaadin.dashboard.vo;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestFileToUploadVo {

	@Test
	public void tfile() {
		FileToUploadVo ftuv = new FileToUploadVo("a.txt");
		assertFalse(ftuv.isRemoteFile());
		assertThat(ftuv.getRelative(), equalTo("a.txt"));

		ftuv = new FileToUploadVo("55/bb/a.txt");
		assertFalse(ftuv.isRemoteFile());
		assertThat(ftuv.getRelative(), equalTo("55/bb/a.txt"));
	}
	
	@Test
	public void turl() {
		FileToUploadVo ftuv = new FileToUploadVo("http://jianglibo.com/a.txt");
		assertTrue(ftuv.isRemoteFile());
		assertThat(ftuv.getRelative(), equalTo("a.txt"));

		ftuv = new FileToUploadVo("https://jianglibo.com/a.txt");
		assertTrue(ftuv.isRemoteFile());
		assertThat(ftuv.getRelative(), equalTo("a.txt"));
	}

}
