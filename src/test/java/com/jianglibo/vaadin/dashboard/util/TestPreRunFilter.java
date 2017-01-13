package com.jianglibo.vaadin.dashboard.util;


import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.jianglibo.vaadin.dashboard.util.PreRunFilter.SoftwareConfigContent;


public class TestPreRunFilter {
	
	private Path ccpp = Paths.get("fixtures", "configcontent");
	
	@Test
	public void hasKey() throws IOException {
		String hasKeyStr = new String(Files.readAllBytes(ccpp.resolve("has-server-to-run-key.yml")));
		PreRunFilter ronr = new PreRunFilter();
		
		SoftwareConfigContent scc = ronr.parse(hasKeyStr);
		
		assertThat(scc.getServerToRun(), Matchers.hasKey("Invoke-DfsCmd"));
		
	}
	
	@Test
	public void noKey() throws IOException {
		String hasKeyStr = new String(Files.readAllBytes(ccpp.resolve("no-server-to-run-key.yml")));
		PreRunFilter ronr = new PreRunFilter();
		
		SoftwareConfigContent scc = ronr.parse(hasKeyStr);
		assertNull(scc.getServerToRun());
	}
}
