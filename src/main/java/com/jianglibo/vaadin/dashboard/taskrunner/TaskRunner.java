package com.jianglibo.vaadin.dashboard.taskrunner;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.hash.Hashing;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.jianglibo.vaadin.dashboard.Broadcaster;
import com.jianglibo.vaadin.dashboard.Broadcaster.BroadCasterMessage;
import com.jianglibo.vaadin.dashboard.Broadcaster.BroadCasterMessageBody;
import com.jianglibo.vaadin.dashboard.Broadcaster.BroadCasterMessageType;
import com.jianglibo.vaadin.dashboard.config.ApplicationConfig;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.BoxGroup;
import com.jianglibo.vaadin.dashboard.domain.BoxGroupHistory;
import com.jianglibo.vaadin.dashboard.domain.BoxHistory;
import com.jianglibo.vaadin.dashboard.domain.PkSource;
import com.jianglibo.vaadin.dashboard.init.AppInitializer;
import com.jianglibo.vaadin.dashboard.repositories.BoxGroupHistoryRepository;
import com.jianglibo.vaadin.dashboard.repositories.BoxGroupRepository;
import com.jianglibo.vaadin.dashboard.repositories.BoxHistoryRepository;
import com.jianglibo.vaadin.dashboard.repositories.BoxRepository;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.repositories.PkSourceRepository;
import com.jianglibo.vaadin.dashboard.service.AppObjectMappers;
import com.jianglibo.vaadin.dashboard.service.SoftwareDownloader;
import com.jianglibo.vaadin.dashboard.ssh.JschSession;
import com.jianglibo.vaadin.dashboard.ssh.JschSession.JschSessionBuilder;
import com.jianglibo.vaadin.dashboard.sshrunner.SshDownloader;
import com.jianglibo.vaadin.dashboard.sshrunner.SshExecRunner;
import com.jianglibo.vaadin.dashboard.sshrunner.SshUploadRunner;
import com.jianglibo.vaadin.dashboard.util.NotificationUtil;
import com.jianglibo.vaadin.dashboard.vo.FileToUploadVo;

/**
 * Give a list of box and software pair.
 * 
 * 
 * @author jianglibo@gmail.com
 *
 */
@Component
public class TaskRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskRunner.class);

	private ListeningExecutorService service;

	@Autowired
	private BoxHistoryRepository boxHistoryRepository;
	
	@Autowired
	private BoxGroupRepository boxGroupRepository;
	
	@Autowired
	private BoxRepository boxRepository;
	
	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private SshUploadRunner sshUploadRunner;
	
	@Autowired
	private SshDownloader sshDownloader;
	
	@Autowired
	private PkSourceRepository pkSourceRepository;

	
	@Autowired
	private BoxGroupHistoryRepository boxGroupHistoryRepository;
	
	@Autowired
	private SoftwareDownloader softwareDownloader;

	@Autowired
	private SshExecRunner sshExecRunner;
	
	@Autowired
	private ApplicationConfig applicationConfig;
	
	@Autowired
	private AppObjectMappers appObjectmappers;
	
	@Autowired
	private MessageSource messageSource;
	
	private AtomicInteger runningThreads = new AtomicInteger(0);
	
	private int bgHistoriesSofar = 0;
	
	private static Set<String> needUploadActions = Sets.newHashSet("install", "changeYumSource");

	public TaskRunner() {
		this.service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
	}
	
	public void submitTasks(TaskDesc taskDesc) {
		for(Box box : taskDesc.getBoxes()) {
			if (applicationConfig.getSshKeyFile(box).isEmpty()) {
				NotificationUtil.error(messageSource, "noKeyFilePath", applicationConfig.getDefaultSshKeyFile().toAbsolutePath().toString());
				return;
			}
		}
		boolean allExists = true;
		List<String> fns = Lists.newArrayList();
		for(FileToUploadVo ftuv : taskDesc.getSoftware().getFileToUploadVos()) {
			Path p = applicationConfig.getLocalFolderPath().resolve(ftuv.getRelative());
			if (!Files.exists(p)) {
				if (ftuv.isRemoteFile()) {
					softwareDownloader.submitTasks(ftuv);
				}
				allExists = false;
				fns.add(p.toString());
			}
		}
		if (!allExists) {
			NotificationUtil.error(messageSource, "filesToUploadNotExists", Joiner.on(",").join(fns));
			return;
		}
		
		NotificationUtil.tray(messageSource, "tasksent");
		
		// filter boxes they have no role in software's possible roles.
		List<OneThreadTaskDesc> onetds = taskDesc.createOneThreadTaskDescs().stream().filter(otd -> {
			
			Set<String> possibleRoles = otd.getSoftware().getPossibleRoleSetUpcase();
			
			if (possibleRoles.isEmpty()) {
				return true;
			}
			
			Set<String> boxRoles = otd.getBox().getRoleSetUpCase();
			
			for(String r : boxRoles) {
				if (possibleRoles.contains(r)) {
					return true;
				}
			}
			return false;
		}).collect(Collectors.toList());

		List<ListenableFuture<OneThreadTaskDesc>> llfs = onetds.stream().map(td -> service.submit(new OneTaskCallable(td)))
				.collect(Collectors.toList());
		
		int n = this.getRunningThreads().addAndGet(llfs.size());
		
		Broadcaster.broadcast(new BroadCasterMessage(new RunningThreadsMessage(n)));

		// successfulAsList means when one task failed, the value will be null.
		ListenableFuture<List<OneThreadTaskDesc>> lf = Futures.successfulAsList(llfs);

		/**
		 * this listener got called when a taskDesc complete.
		 */
		Futures.addCallback(lf, new FutureCallback<List<OneThreadTaskDesc>>() {
			@Override
			public void onSuccess(List<OneThreadTaskDesc> result) {
					Set<BoxHistory> bhs = Sets.newHashSet();
					long successes = result.stream().filter(Objects::nonNull).map(td -> {
						BoxHistory bh = boxHistoryRepository.save(td.getBoxHistory());
						bh.getBox().getHistories().add(bh);
						bhs.add(bh);
						boxRepository.save(bh.getBox());
						return bh;
					}).filter(BoxHistory::isSuccess).count();
					
					BoxGroupHistory bgh = new BoxGroupHistory(taskDesc.getSoftware(), taskDesc.getBoxGroup(), taskDesc.getAction(), bhs, (successes == taskDesc.getBoxGroup().getBoxes().size()));
					bgh.setRunner(personRepository.findByEmail(AppInitializer.firstEmail));
					
					if (successes == result.size()) {
						bgh.setSuccess(true);
					}
					bgh = boxGroupHistoryRepository.save(bgh);
					
					for(BoxHistory bh: bhs) {
						bh.setBoxGroupHistory(bgh);
						boxHistoryRepository.save(bh);
					}
					bgHistoriesSofar++;
					
					BoxGroup bg = taskDesc.getBoxGroup();
					bg.getHistories().add(bgh);
					List<BoxGroupHistory> newBhistories = bg.getHistories();
					newBhistories.add(bgh);
					bg.setHistories(newBhistories);
					bg.setInstallResults(extractInstallResults(bg, bgh));
					boxGroupRepository.save(bg);
					
					saveNeedDownloadFiles(bg,bgh);
					
					Broadcaster.broadcast(new BroadCasterMessage(new GroupTaskFinishMessage(bgHistoriesSofar, taskDesc.getUniqueUiId())));
			}

			private void saveNeedDownloadFiles(BoxGroup bg, BoxGroupHistory bgh) {
				try {
					for (BoxHistory bh : bgh.getBoxHistories()) {
						JschSession jsession = new JschSessionBuilder().setHost(bh.getBox().getIp()).setKeyFile(applicationConfig.getSshKeyFile(bh.getBox()))
								.setPort(bh.getBox().getPort()).setSshUser(bh.getBox().getSshUser()).build();
						try {
							List<DownloadBLock> dbs = extractDownloadResultMap(bh.getLogLines());
							
							dbs.stream().flatMap(db -> db.getFiles().stream()).forEach(ndf -> {
								Path downloaded = sshDownloader.download(jsession, ndf.getFullName(), ndf.getName());
								if (downloaded == null) {
									LOGGER.error("download {} from {} failed", ndf.getFullName(), bh.getBox().getIp());
								} else {
									try {
										String extNoDot = com.google.common.io.Files.getFileExtension(ndf.getName());
										String md5 = com.google.common.io.Files.asByteSource(downloaded.toFile()).hash(Hashing.md5()).toString();
										PkSource ps = pkSourceRepository.findByFileMd5(md5);
										if (ps == null) {
											File nf = new File(downloaded.toFile().getParentFile(), md5 + "." + extNoDot);
											if (!nf.exists()) {
												com.google.common.io.Files.move(downloaded.toFile(), nf);
											}
											ps = new PkSource.PkSourceBuilder(md5, ndf.getName(), nf.length(), extNoDot, Files.probeContentType(nf.toPath())).build();
											pkSourceRepository.save(ps);
										} else {
											ps.setUpdatedAt(Date.from(Instant.now()));
											pkSourceRepository.save(ps);
										}
									} catch (IOException e) {
										LOGGER.error("save {} as pksource failed", downloaded.toAbsolutePath().toString());
									}
								}
							});
						} catch (Exception e) {
						} finally {
							if (jsession != null) {
								if (jsession.getSession().isConnected()) {
									jsession.getSession().disconnect();
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable t) {
				LOGGER.error(t.getMessage());
			}
		});
	}
	
	protected List<List<String>> getResultBlocks(List<String> lines, String startPtn, String endPtn) {
		boolean startFlag = false;
		boolean endFlag = false;
		List<List<String>> blocks = Lists.newArrayList();
		List<String> block = Lists.newArrayList();
		for(String line : lines) {
			if (line.contains(endPtn)) {
				endFlag = true;
				if (startFlag) {
					blocks.add(block);
				}
				startFlag = false;
				block = Lists.newArrayList();
			} else {
				endFlag = false;
			}
			if (startFlag && !endFlag) {
				block.add(line);
			}
			if (line.contains(startPtn)) {
				startFlag = true;
			}
		}
		return blocks;
	}
	
	protected List<List<String>> getInstallResultBlocks(List<String> lines) {
		return  getResultBlocks(lines, BoxHistory._INSTALL_RESULT_BEGIN_, BoxHistory._INSTALL_RESULT_END_);
	}
	
	// extract all blocks of installResults.
	protected Map<String,Object> extractInstallResultMap(List<String> lines) {
		Map<String, Object> mp = Maps.newHashMap();
		
		getInstallResultBlocks(lines).forEach(bl -> {
			String s = Joiner.on("").join(bl);
			try {
				JavaType jt = appObjectmappers.getObjectMapperNoIdent().getTypeFactory().constructParametrizedType(Map.class,Map.class, String.class, Object.class);
				Map<String, Object> onemp = appObjectmappers.getObjectMapperNoIdent().readValue(s, jt);
				onemp.entrySet().forEach(entry -> {
					mp.put(entry.getKey(), entry.getValue());
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		return mp;
	}
	
	// need static for jackson to work.
	protected static class NeedDownloadFile {
		private String name;
		private String fullName;
		
		public NeedDownloadFile() {
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getFullName() {
			return fullName;
		}
		public void setFullName(String fullName) {
			this.fullName = fullName;
		}
	}
	
	protected static class DownloadBLock {
		private List<NeedDownloadFile> files;
		
		public DownloadBLock() {
		}

		public List<NeedDownloadFile> getFiles() {
			return files;
		}

		public void setFiles(List<NeedDownloadFile> files) {
			this.files = files;
		}
	}
	
	// extract all blocks of downloads in results lines. is for one boxhistory!
	protected List<DownloadBLock> extractDownloadResultMap(List<String> lines) {
		List<DownloadBLock> mp = Lists.newArrayList();
		
		getResultBlocks(lines, BoxHistory._DOWNLOAD_BEGIN_, BoxHistory._DOWNLOAD_END_).forEach(bl -> {
			String s = Joiner.on("").join(bl);
			try {
				mp.add(appObjectmappers.getObjectMapperNoIdent().readValue(s, DownloadBLock.class));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		return mp;
	}
	
	// existing installResults will be replaced by same key, So if each box return different key result, they shoud't overlap each other.
	private String extractInstallResults(BoxGroup bg, BoxGroupHistory bgh) {
		Map<String, Object> mp = Maps.newHashMap();
		try {
			if (bg.getInstallResults() != null && bg.getInstallResults().trim().length() > 0) {
				JavaType jt = appObjectmappers.getObjectMapperNoIdent().getTypeFactory().constructParametrizedType(Map.class,Map.class, String.class, Object.class);
				mp = appObjectmappers.getObjectMapperNoIdent().readValue(bg.getInstallResults(), jt);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (BoxHistory bh : bgh.getBoxHistories()) {
			for (Entry<String, Object> entry: extractInstallResultMap(bh.getLogLines()).entrySet()) {
				mp.put(entry.getKey(), entry.getValue());
			};
		}
		try {
			return appObjectmappers.getObjectMapperNoIdent().writeValueAsString(mp);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{}";
	}


	public int getBgHistoriesSofar() {
		return bgHistoriesSofar;
	}

	public void setBgHistoriesSofar(int bgHistoriesSofar) {
		this.bgHistoriesSofar = bgHistoriesSofar;
	}
	


	public AtomicInteger getRunningThreads() {
		return runningThreads;
	}

	public void setRunningThreads(AtomicInteger runningThreads) {
		this.runningThreads = runningThreads;
	}



	public static class GroupTaskFinishMessage implements BroadCasterMessageBody {
		
		private final int bgHistoriesSofar;
		
		private final String uniqueUiId;
		
		public GroupTaskFinishMessage(int bgHistoriesSofar, String uniqueUiId) {
			super();
			this.bgHistoriesSofar = bgHistoriesSofar;
			this.uniqueUiId = uniqueUiId;
			
		}
		
		public BroadCasterMessageType getBroadCasterMessageType() {
			return BroadCasterMessageType.GROUP_TASK_FINISH;
		}

		public int getBgHistoriesSofar() {
			return bgHistoriesSofar;
		}

		public String getUniqueUiId() {
			return uniqueUiId;
		}
	}

	public static class OneTaskFinishMessage implements BroadCasterMessageBody {
		
		private OneThreadTaskDesc ottd;
		
		private int runningThreads;
		
		public OneTaskFinishMessage(OneThreadTaskDesc ottd,int runningThreads) {
			super();
			this.setOttd(ottd);
			this.setRunningThreads(runningThreads);
		}
		
		public BroadCasterMessageType getBroadCasterMessageType() {
			return BroadCasterMessageType.ONE_TASK_FINISH;
		}

		public OneThreadTaskDesc getOttd() {
			return ottd;
		}

		public void setOttd(OneThreadTaskDesc ottd) {
			this.ottd = ottd;
		}

		public int getRunningThreads() {
			return runningThreads;
		}

		public void setRunningThreads(int runningThreads) {
			this.runningThreads = runningThreads;
		}
	}

	public static class RunningThreadsMessage implements BroadCasterMessageBody {
		
		private int runningThreads;
		
		public RunningThreadsMessage(int runningThreads) {
			super();
			this.setRunningThreads(runningThreads);
		}
		
		public BroadCasterMessageType getBroadCasterMessageType() {
			return BroadCasterMessageType.RUNNING_THREADS;
		}

		public int getRunningThreads() {
			return runningThreads;
		}

		public void setRunningThreads(int runningThreads) {
			this.runningThreads = runningThreads;
		}
	}
	
	private class OneTaskCallable implements Callable<OneThreadTaskDesc> {

		private OneThreadTaskDesc oneThreadtaskDesc;

		public OneTaskCallable(OneThreadTaskDesc taskDesc) {
			this.oneThreadtaskDesc = taskDesc;
		}

		@Override
		public OneThreadTaskDesc call() throws Exception {
			Map<String, Long> map = oneThreadtaskDesc.getSoftware().getTimeOutMaps();
			if (map.containsKey(oneThreadtaskDesc.getAction())) {
				Thread t = new Thread(new StopableRunnable());
				t.start();
				Thread.sleep(map.get(oneThreadtaskDesc.getAction()));
				if(t.isAlive()) {
					t.interrupt();
				}
			} else {
				runOneThreadTaskDesc();
			}
			return oneThreadtaskDesc;
		}

		private void runOneThreadTaskDesc() {
			Box box = oneThreadtaskDesc.getBox();
			JschSession jsession = null;
			try {
				jsession = new JschSessionBuilder().setHost(box.getIp()).setKeyFile(applicationConfig.getSshKeyFile(box))
						.setPort(box.getPort()).setSshUser(box.getSshUser()).build();
				
				boolean needUploadFile = needUploadActions.contains(oneThreadtaskDesc.getAction()) || oneThreadtaskDesc.getAction().toUpperCase().startsWith("INSTALL");
				
				if (needUploadFile) {
					sshUploadRunner.run(jsession, oneThreadtaskDesc);
				}
				
				if (oneThreadtaskDesc.getBoxHistory().isSuccess() || !needUploadFile) {
					sshExecRunner.run(jsession, oneThreadtaskDesc);
				}
			} catch (Exception e) {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				oneThreadtaskDesc.getBoxHistory().appendLogAndSetFailure(sw.toString());
				if (e instanceof java.io.InterruptedIOException) {
					oneThreadtaskDesc.getBoxHistory().appendLogAndSetFailure("-----Notice---------\n, This error message maybe don't mean task failed, Pleach check software state manully.");
				}
			} finally {
				if (jsession != null) {
					if (jsession.getSession().isConnected()) {
						jsession.getSession().disconnect();
					}
				}
			}
			int n = TaskRunner.this.getRunningThreads().decrementAndGet(); 
			Broadcaster.broadcast(new BroadCasterMessage(new OneTaskFinishMessage(oneThreadtaskDesc, n)));
		}
		
		private class StopableRunnable implements Runnable {
			@Override
			public void run() {
				runOneThreadTaskDesc();
			}
		}
	}
}
