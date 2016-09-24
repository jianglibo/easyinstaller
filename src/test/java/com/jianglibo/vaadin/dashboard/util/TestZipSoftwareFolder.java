package com.jianglibo.vaadin.dashboard.util;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class TestZipSoftwareFolder {
	
	private Path baseFolder = Paths.get("softwares");

	@Test
	public void packall() throws IOException {
		Path listPath = baseFolder.resolve("softwarelist.txt");
		
		try (Stream<Path> pathes = Files.list(baseFolder)) {
			List<String> lines = pathes.filter(sf -> Files.isDirectory(sf)).map(sf -> {
				try {
					String fn = sf.getFileName().toString() + ".zip";
					SoftwarePackUtil.pack(sf, baseFolder.resolve(fn));
					return fn;
				} catch (Exception e) {
					return null;
				}
			}).filter(fn -> fn != null).collect(Collectors.toList());
			
			//.reduce("", (r, e) -> r.concat(e).concat("\n"));
			Files.write(listPath, lines);
		} catch (Exception e) {
		}
	}
	
	@Test
	public void packone() throws IOException {
		String fn = "tcl-centos7-1";	
		SoftwarePackUtil.pack(baseFolder.resolve(fn), baseFolder.resolve(fn + ".zip"));
		assertTrue(Files.exists(baseFolder.resolve(fn + ".zip")));
		assertThat(Files.size(baseFolder.resolve(fn + ".zip")), greaterThan(0L));
		
		Path unpackFolder = SoftwarePackUtil.unpack(baseFolder.resolve(fn + ".zip"));
		assertTrue(Files.exists(unpackFolder.resolve("filesToUpload")));
		assertTrue(Files.exists(unpackFolder.resolve("code.sh")));
		assertTrue(Files.exists(unpackFolder.resolve("config.yml")));
		assertTrue(Files.exists(unpackFolder.resolve("description.yml")));
	}

}
