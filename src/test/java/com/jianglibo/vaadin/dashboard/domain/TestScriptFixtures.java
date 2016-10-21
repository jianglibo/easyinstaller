package com.jianglibo.vaadin.dashboard.domain;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Named;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.collect.Sets;
import com.jianglibo.vaadin.dashboard.Tbase;
import com.jianglibo.vaadin.dashboard.repositories.BoxGroupRepository;
import com.jianglibo.vaadin.dashboard.repositories.BoxRepository;
import com.jianglibo.vaadin.dashboard.security.PersonVo;
import com.jianglibo.vaadin.dashboard.service.AppObjectMappers;
import com.jianglibo.vaadin.dashboard.sshrunner.EnvForCodeExec;
import com.jianglibo.vaadin.dashboard.taskrunner.OneThreadTaskDesc;
import com.jianglibo.vaadin.dashboard.taskrunner.TaskDesc;
import com.jianglibo.vaadin.dashboard.util.SoftwareFolder;

/**
 * 
 * 
 * @author jianglibo@gmail.com
 *
 */
public class TestScriptFixtures extends Tbase {

	@Autowired
	private BoxGroupRepository boxGroupRepository;

	@Autowired
	private BoxRepository boxRepository;
	
	
    private ObjectMapper objectMapperNoIdent() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper;
    }
    

    private ObjectMapper ymlObjectMapper() {
    	return new ObjectMapper(new YAMLFactory());
    }
    
    private ObjectMapper xmlObjectMapper() {
    	JacksonXmlModule module = new JacksonXmlModule();
    	// and then configure, for example:
    	module.setDefaultUseWrapper(false);
    	return new XmlMapper(module);
    	// and you can also configure AnnotationIntrospectors 
    }

	private AppObjectMappers appObjectMappers;

	private Path softwarePath = Paths.get("fixture-softwares");
	
	@Before
	public void b() throws IOException {
		appObjectMappers = new AppObjectMappers(objectMapperNoIdent(), ymlObjectMapper(), xmlObjectMapper());
		if (!Files.exists(softwarePath)) {
			Files.createDirectories(softwarePath);
		}
	}

	@After
	public void after() {
		List<BoxGroup> bgs = boxGroupRepository.findAll();
		bgs.forEach(bg -> {
			bg.getBoxes().stream().forEach(b -> {
				b.getBoxGroups().remove(bg);
				boxRepository.save(b);
			});
			bg.setBoxes(Sets.newHashSet());
			boxGroupRepository.delete(bg);
		});

		boxRepository.deleteAll();
		softwareRepository.deleteAll();
	}

	@Test
	public void writeEnvFixturesFromFolder() throws IOException {
		Person root = getFirstPerson();
		try (Stream<Path> pathstream = Files.walk(softwarePath)) {
			pathstream.filter(p -> SoftwareFolder.descriptionyml.equals(p.getFileName().toString())).filter(p -> {
				Path sp = p.getParent().getParent().resolve("sample-env");
				return Files.exists(sp) && Files.exists(sp.resolve("boxgroup.yaml"));
			}).map(SoftwareFolder::new).map(sfolder -> {
				try {
					BoxGroup bg = ymlObjectMapper.readValue(sfolder.readBoxgroupYaml(), BoxGroup.class);
					bg.setConfigContent(sfolder.readBoxgroupConfigContent());
					BoxGroup bgInDb = boxGroupRepository.findByName(bg.getName());

					if (bgInDb != null) {
						bgInDb.getBoxes().forEach(b -> {
							b.getBoxGroups().remove(bgInDb);
							boxRepository.save(b);
						});
						boxGroupRepository.delete(bgInDb);
					}

					bg.setCreator(root);

					bg = boxGroupRepository.save(bg);

					final Set<BoxGroup> bgs = Sets.newHashSet(bg);

					Set<Box> boxes = bg.getBoxes().stream().map(box -> {
						Box boxInDb = boxRepository.findByIp(box.getIp());
						if (boxInDb != null) {
							boxRepository.delete(boxInDb);
						}
						box.setCreator(root);
						box.setBoxGroups(bgs);
						return boxRepository.save(box);
					}).collect(Collectors.toSet());

					bg.setBoxes(boxes);
					bgs.add(boxGroupRepository.save(bg));
					sfolder.setBoxGroup(bg);
					return sfolder;
				} catch (Exception e) {
					e.printStackTrace();
					fail("bg create failed.");
				}
				return null;
			})
			.map(sfolder -> {
				try {
					Software sf = ymlObjectMapper.readValue(sfolder.readDescriptionyml(), Software.class);
					sf.setConfigContent(sfolder.getSoftwareConfigContent(sf.getConfigContent()));
					sfolder.setSoftware(sf);
					return (SoftwareFolder)sfolder;
				} catch (Exception e) {
					e.printStackTrace();
					fail(e.getMessage());
					return null;
				}
			}).map(sfolder -> {
				try {
					Software sf = sfolder.getSoftware();
					Software sfInDb = softwareRepository.findByNameAndOstypeAndSversion(sf.getName(), sf.getOstype(),
							sf.getSversion());
					if (sfInDb != null) {
						softwareRepository.delete(sfInDb);
					}
					sf.setCreator(root);
					softwareRepository.save(sf);
					
					TaskDesc td = new TaskDesc("", new PersonVo.PersonVoBuilder(getFirstPerson()).build(), sfolder.getBoxGroup(),
							Sets.newHashSet(), sf, "install");

					OneThreadTaskDesc ottd = td.createOneThreadTaskDescs().get(0);

					EnvForCodeExec efce = new EnvForCodeExec.EnvForCodeExecBuilder(appObjectMappers, ottd,
							"/opt/easyinstaller").build();

					assertThat("should have 3 boxes", efce.getBoxGroup().getBoxes().size(), equalTo(3));
					assertThat("first box should be 10", efce.getBoxGroup().getBoxes().get(0).getIp(),
							equalTo("192.168.2.10"));
					assertThat("first box should be 11", efce.getBoxGroup().getBoxes().get(1).getIp(),
							equalTo("192.168.2.11"));
					assertThat("first box should be 12", efce.getBoxGroup().getBoxes().get(2).getIp(),
							equalTo("192.168.2.14"));
					
					Path testFolder = sfolder.getTestPath();
					
					switch (sf.getPreferredFormat()) {
					case "JSON":
						String json = appObjectMappers.getObjectMapperNoIdent().writeValueAsString(efce);
						Files.write(testFolder.resolve("envforcodeexec.json"), json.getBytes());
						break;
					case "YAML":
						String yml = appObjectMappers.getYmlObjectMapper().writeValueAsString(efce);
						Files.write(testFolder.resolve("envforcodeexec.yaml"), yml.getBytes());
						break;
					case "XML":
						String xml = appObjectMappers.getXmlObjectMapper().writeValueAsString(efce);
						Files.write(testFolder.resolve("envforcodeexec.xml"), xml.getBytes());
						break;
					default:
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					fail("softwareprocess failed.");
				}
				return 1;
			}).count();
		} catch (Exception e) {
			fail("walk softwarePath failed.");
		}
	}


//	@Test
//	public void verify() throws IOException {
//		testBoxGroup();
//		try (Stream<Path> folders = Files.list(softwarePath)) {
//			folders.filter(Files::isDirectory).map(SoftwareFolder::new).filter(SoftwareFolder::isValid)
//					.forEach(sfolder -> {
//						try {
//							Software sf = ymlObjectMapper.readValue(sfolder.readDescriptionyml(), Software.class);
//							sf.setName(sfolder.getName());
//							sf.setOstype(sfolder.getOstype());
//							sf.setSversion(sfolder.getSversion());
//							sf.setConfigContent(sfolder.getConfigContent(sf.getConfigContent()));
//							ConfigContent cconfig = new ConfigContent(sf.getConfigContent());
//							cconfig.getConverted(appObjectMappers, sf.getPreferredFormat());
//
//							EnvForCodeExec efce = null;
//							// do convert.
//							if (!(Strings.isNullOrEmpty(cconfig.getFrom()) || Strings.isNullOrEmpty(cconfig.getTo()))) {
//								if ("XML".equals(cconfig.getTo())) {
//									String xml = com.google.common.io.Files
//											.asCharSource(sfolder.getTestPath().resolve(tbasename + "xml").toFile(),
//													Charsets.UTF_8)
//											.read();
//									efce = appObjectMappers.getXmlObjectMapper().readValue(xml, EnvForCodeExec.class);
//									assertTrue("should be xml content.",
//											efce.getSoftware().getConfigContent().startsWith("<LinkedHashMap>"));
//								}
//							}
//
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					});
//		}
//	}
}
