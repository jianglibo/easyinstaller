package com.jianglibo.vaadin.dashboard.view.envfixture;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.activity.InvalidActivityException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.collect.Sets;
import com.jianglibo.vaadin.dashboard.config.ApplicationConfig;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.BoxGroup;
import com.jianglibo.vaadin.dashboard.domain.BoxGroupHistory;
import com.jianglibo.vaadin.dashboard.domain.Person;
import com.jianglibo.vaadin.dashboard.init.AppInitializer;
import com.jianglibo.vaadin.dashboard.repositories.BoxGroupHistoryRepository;
import com.jianglibo.vaadin.dashboard.repositories.BoxGroupRepository;
import com.jianglibo.vaadin.dashboard.repositories.BoxHistoryRepository;
import com.jianglibo.vaadin.dashboard.repositories.BoxRepository;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.security.PersonVo;
import com.jianglibo.vaadin.dashboard.service.AppObjectMappers;
import com.jianglibo.vaadin.dashboard.service.SoftwareImportor;
import com.jianglibo.vaadin.dashboard.sshrunner.EnvForCodeExec;
import com.jianglibo.vaadin.dashboard.taskrunner.OneThreadTaskDesc;
import com.jianglibo.vaadin.dashboard.taskrunner.TaskDesc;
import com.jianglibo.vaadin.dashboard.util.SoftwareFolder;
import com.jianglibo.vaadin.dashboard.util.StrUtil;
import com.jianglibo.vaadin.dashboard.vo.SoftwareImportResult;

@Component
public class EnvFixtureCreator {

	private static final Logger LOGGER = LoggerFactory.getLogger(EnvFixtureCreator.class);
	
	private final PersonRepository personRepository;

	private final AppObjectMappers appObjectMappers;

	private final BoxGroupRepository boxGroupRepository;

	private final BoxRepository boxRepository;

	private final SoftwareImportor softwareImportor;
	
	private final ApplicationConfig applicationConfig;
	
	private final BoxGroupHistoryRepository bghRepo;
	
	private final BoxHistoryRepository bhRepo;

	@Autowired
	public EnvFixtureCreator(SoftwareImportor softwareImportor, PersonRepository personRepository, AppObjectMappers appObjectMappers,
			BoxGroupRepository boxGroupRepository, BoxRepository boxRepository,BoxGroupHistoryRepository bghRepo,BoxHistoryRepository bhRepo, ApplicationConfig applicationConfig) {
		super();
		this.personRepository = personRepository;
		this.appObjectMappers = appObjectMappers;
		this.boxGroupRepository = boxGroupRepository;
		this.boxRepository = boxRepository;
		this.softwareImportor = softwareImportor;
		this.applicationConfig = applicationConfig;
		this.bghRepo = bghRepo;
		this.bhRepo = bhRepo;
	}

	public static class InValidScriptProjectPathException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
	}
	
	public BoxGroup importBoxGroup(String ymlContent) throws JsonParseException, JsonMappingException, IOException {
		return importBoxGroup(ymlContent, null, null);
	}
	
	protected BoxGroup importBoxGroup(String ymlContent,SoftwareFolder sfolder, Person person) throws JsonParseException, JsonMappingException, IOException {
		BoxGroup bg = appObjectMappers.getYmlObjectMapper().readValue(ymlContent, BoxGroup.class);
		
		if (sfolder != null) {
			bg.setConfigContent(sfolder.readBoxgroupConfigContent());
		}
		
		if (person == null) {
			person = personRepository.findByEmail(AppInitializer.firstEmail);
		}
		
		final Person fperson = person;
		
		BoxGroup bgInDb = boxGroupRepository.findByName(bg.getName());

		if (bgInDb != null) {
			bgInDb.getBoxes().forEach(b -> {
				b.getBoxGroups().remove(bgInDb);
				boxRepository.save(b);
			});
			bgInDb.getHistories().forEach(h -> {
				h.getBoxHistories().forEach(bh -> {
					bhRepo.delete(bh);
				});
				bghRepo.delete(h);
			});
			BoxGroup bgInDb1 = boxGroupRepository.findByName(bg.getName());
			boxGroupRepository.delete(bgInDb1);
		}

		bg.setCreator(person);
		
		Set<Box> waitingToSaves = bg.getBoxes();
		bg.setBoxes(Sets.newHashSet());
		bg = boxGroupRepository.save(bg);

		final Set<BoxGroup> bgs = Sets.newHashSet(bg);

		Set<Box> boxes = waitingToSaves.stream().map(box -> {
			Box boxInDb = boxRepository.findByIp(box.getIp());
			Set<String> roles = Sets.newHashSet();
			if (boxInDb != null) {
				roles = boxInDb.getRoleSetUpCase();
				boxRepository.delete(boxInDb);
			}
			roles.addAll(box.getRoleSetUpCase());
			box.setRoles(StrUtil.commaJoiner.join(roles));
			box.setCreator(fperson);
			box.setBoxGroups(bgs);
			return boxRepository.save(box);
		}).collect(Collectors.toSet());

		bg.setBoxes(boxes);
		bgs.add(boxGroupRepository.save(bg));
		return bg;
	}

	public void create(Path scriptPath) throws IOException {
		if (!Files.exists(scriptPath) || !Files.isDirectory(scriptPath)) {
			throw new InvalidActivityException("invalidPath");
		}

		Person person = personRepository.findByEmail(AppInitializer.firstEmail);
		try (Stream<Path> pathstream = Files.walk(scriptPath)) {
			long processed = pathstream.filter(p -> SoftwareFolder.descriptionyml.equals(p.getFileName().toString())).filter(p -> {
				Path sp = p.getParent().getParent().resolve("sample-env");
				return Files.exists(sp) && Files.exists(sp.resolve("boxgroup.yaml"));
			}).map(SoftwareFolder::new).map(sfolder -> {
				try {
					BoxGroup bg = importBoxGroup(sfolder.readBoxgroupYaml(), sfolder, person);
//					BoxGroup bg = appObjectMappers.getYmlObjectMapper().readValue(sfolder.readBoxgroupYaml(),
//							BoxGroup.class);
//					bg.setConfigContent(sfolder.readBoxgroupConfigContent());
//					BoxGroup bgInDb = boxGroupRepository.findByName(bg.getName());
//
//					if (bgInDb != null) {
//						bgInDb.getBoxes().forEach(b -> {
//							b.getBoxGroups().remove(bgInDb);
//							boxRepository.save(b);
//						});
//						boxGroupRepository.delete(bgInDb);
//					}
//
//					bg.setCreator(person);
//
//					bg = boxGroupRepository.save(bg);
//
//					final Set<BoxGroup> bgs = Sets.newHashSet(bg);
//
//					Set<Box> boxes = bg.getBoxes().stream().map(box -> {
//						Box boxInDb = boxRepository.findByIp(box.getIp());
//						Set<String> roles = Sets.newHashSet();
//						if (boxInDb != null) {
//							roles = boxInDb.getRoleSetUpCase();
//							boxRepository.delete(boxInDb);
//						}
//						roles.addAll(box.getRoleSetUpCase());
//						box.setRoles(StrUtil.commaJoiner.join(roles));
//						box.setCreator(person);
//						box.setBoxGroups(bgs);
//						return boxRepository.save(box);
//					}).collect(Collectors.toSet());
//
//					bg.setBoxes(boxes);
//					bgs.add(boxGroupRepository.save(bg));
					sfolder.setBoxGroup(bg);
					return sfolder;
				} catch (Exception e) {
					LOGGER.error(e.getMessage());
					return null;
				}
			}).filter(Objects::nonNull).map(sfolder -> {
				try {
					List<SoftwareImportResult> sirs = softwareImportor.installSoftwareFromFolder(sfolder.getBasePath());
					for(SoftwareImportResult sir : sirs) {
						if (sir.isSuccess()) {
							sfolder.setSoftware(sir.getSoftware());
							return sfolder;
						}
					}
					return null;
				} catch (Exception e) {
					LOGGER.error(e.getMessage());
					return null;
				}
			}).filter(Objects::nonNull).map(sfolder -> {
				try {
					TaskDesc td = new TaskDesc("", new PersonVo.PersonVoBuilder(person).build(), sfolder.getBoxGroup(),
							Sets.newHashSet(), sfolder.getSoftware(), "install", "");
					
					List<OneThreadTaskDesc> ottds = td.createOneThreadTaskDescs(); 

					OneThreadTaskDesc ottd;
					
					String boxIp = sfolder.getBoxIp(); 
					
					if ( boxIp == null) {
						ottd = ottds.get(0);
					} else {
						ottd = ottds.stream().filter(one -> boxIp.equals(one.getBox().getIp())).findAny().orElse(null);
					}
					if (ottd == null) {
						ottd = ottds.get(0);
					}

					EnvForCodeExec efce = new EnvForCodeExec.EnvForCodeExecBuilder(appObjectMappers, ottd,
							applicationConfig.getRemoteFolder()).build();

					Path testFolder = sfolder.getTestPath();

					switch (sfolder.getSoftware().getPreferredFormat()) {
					case "JSON":
						String json = appObjectMappers.getObjectMapperNoIdent().writeValueAsString(efce);
						Files.write(testFolder.resolve("envforcodeexec.json"), json.getBytes());
						return 1;
					case "YAML":
						String yml = appObjectMappers.getYmlObjectMapper().writeValueAsString(efce);
						Files.write(testFolder.resolve("envforcodeexec.yaml"), yml.getBytes());
						break;
					case "XML":
						String xml = appObjectMappers.getXmlObjectMapper().writeValueAsString(efce);
						Files.write(testFolder.resolve("envforcodeexec.xml"), xml.getBytes());
						return 1;
					default:
						break;
					}
				} catch (Exception e) {
					LOGGER.error(e.getMessage());
				}
				return 0;
			}).count();
			
			if (processed == 0) {
				throw new InvalidActivityException("noProjectFound");
			}
		} catch (Exception e2) {
			LOGGER.error(e2.getMessage());
			throw new InvalidActivityException("walkPathFailed");
		}
	}

}
