package com.jianglibo.vaadin.dashboard.util;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;

public class TestPowershell {
	
    private String psfilePath = "../install-scripts/src/main/resources/com/jianglibo/easyinstaller/scriptsnippets/powershell/HostModifier.ps1";
	
    private Path hostp = Paths.get(System.getenv("windir")).resolve("System32/drivers/etc/hosts");
    
    private File psfile;
    
    @Before
    public void before() throws IOException {
    	psfile = File.createTempFile("easyinstaller", ".Ps1");
    }
    
    @After
    public void after() {
    	psfile.delete();
    }
    
//	@Test
    // will throw unauthorizedAccessException
	public void thosts() throws IOException, InterruptedException {
		Assume.assumeTrue(Paths.get(psfilePath).toFile().exists());
		Files.write(Joiner.on(System.lineSeparator()).join(Files.readLines(Paths.get(psfilePath).toFile(), StandardCharsets.UTF_8)), psfile, StandardCharsets.UTF_8);
		String[] cmd = {"powershell", "-File", psfile.getAbsolutePath(), "-hostfile", StrUtil.doubleQuotation(hostp.toAbsolutePath().toString()), "-items", StrUtil.doubleQuotation("1.1.1.1 a.a.a.a")};
		ProcessBuilder pb =  new ProcessBuilder(cmd);
		pb.redirectErrorStream(true);
		Process p = pb.start();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ByteStreams.copy(p.getInputStream(), baos);
		System.out.println(baos.toString("utf-8"));
		p.waitFor();
		boolean exists = Files.readLines(hostp.toFile(), StandardCharsets.UTF_8).stream().anyMatch(l -> l.contains("1.1.1.1"));
		assertTrue(exists);
		String[] cmd1 = {"powershell", "-File", psfile.getAbsolutePath(), "-hostfile", StrUtil.doubleQuotation(hostp.toAbsolutePath().toString()), "-items", StrUtil.doubleQuotation("1.1.1.1 a.a.a.a"), "-delete"};
		pb =  new ProcessBuilder(cmd1);
		pb.redirectErrorStream(true);
		p = pb.start();
		baos = new ByteArrayOutputStream();
		ByteStreams.copy(p.getInputStream(), baos);
		p.waitFor();

		exists = Files.readLines(hostp.toFile(), StandardCharsets.UTF_8).stream().anyMatch(l -> l.contains("1.1.1.1"));
		assertFalse(exists);
	}
	
	@Test
	public void te() {
		Path hostp = Paths.get(System.getenv("windir")).resolve("System32/drivers/etc/hosts");
		assertTrue(hostp.toFile().exists());
		assertThat(System.getenv("windir"), equalTo("c:\\windows".toUpperCase()));
	}
	
	@Test
	public void t() throws IOException, InterruptedException {
		File psfile = File.createTempFile("easyinstaller", ".Ps1");
		String content = "Write-Output 'abc'";
		Files.write(content, psfile, StandardCharsets.UTF_8);
		String[] cmd = {"powershell", "-File", psfile.getAbsolutePath()};
		 ProcessBuilder pb =
				   new ProcessBuilder(cmd);
				 Map<String, String> env = pb.environment();
//				 env.put("VAR1", "myValue");
//				 env.remove("OTHERVAR");
//				 env.put("VAR2", env.get("VAR1") + "suffix");
//				 pb.directory(new File("myDir"));
//				 File log = new File("log");
				 pb.redirectErrorStream(true);
//				 pb.redirectOutput(Redirect.appendTo(log));
				 Process p = pb.start();
				 
				 ByteArrayOutputStream baos = new ByteArrayOutputStream();
				 ByteStreams.copy(p.getInputStream(), baos);
				 p.waitFor();
		psfile.delete();

		assertThat(baos.toString().trim(), equalTo("abc"));
	}

}
