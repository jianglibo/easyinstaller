package com.jianglibo.vaadin.dashboard.util;

import static org.junit.Assert.assertTrue;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.Stream;

import org.junit.Test;

public class TestZipSoftwareFolder {
	
	private Path baseFolder = Paths.get("softwares");
	
	@Test
	public void t() {
		assertTrue(true);
	}

//	@Test
	public void packall() throws IOException {
		Path listPath = baseFolder.resolve("softwarelist.txt");
		
		try (Stream<Path> pathes = Files.list(baseFolder)) {
			
			Iterator<String> lineit = pathes.map(SoftwareFolder::new).filter(SoftwareFolder::isValid).map(sfolder -> {
				try {
					SoftwarePackUtil.pack(sfolder.getPath(), sfolder.getZipFilePath());
					return sfolder;
				} catch (Exception e) {
					return null;
				}
			}).filter(java.util.Objects::nonNull).map(SoftwareFolder::fnAndMd5).iterator();
			
			PrintWriter pw = new PrintWriter(new FileWriter(listPath.toFile()));
			while (lineit.hasNext()) {
				pw.println(lineit.next());
			}
			pw.close();
		}
		
		String fn = "tcl--centos7--1";
		
		Path unpackFolder = SoftwarePackUtil.unpack(baseFolder.resolve(fn + ".zip"));
		
		assertTrue(Files.exists(unpackFolder.resolve("code.sh")));
		assertTrue(Files.exists(unpackFolder.resolve("config.yml")));
		assertTrue(Files.exists(unpackFolder.resolve("description.yml")));
	}
}
