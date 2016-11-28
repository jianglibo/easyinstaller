package com.jianglibo.vaadin.dashboard.taskrunner;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
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

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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
import com.jianglibo.vaadin.dashboard.init.AppInitializer;
import com.jianglibo.vaadin.dashboard.repositories.BoxGroupHistoryRepository;
import com.jianglibo.vaadin.dashboard.repositories.BoxGroupRepository;
import com.jianglibo.vaadin.dashboard.repositories.BoxHistoryRepository;
import com.jianglibo.vaadin.dashboard.repositories.BoxRepository;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.service.SoftwareDownloader;
import com.jianglibo.vaadin.dashboard.ssh.JschSession;
import com.jianglibo.vaadin.dashboard.ssh.JschSession.JschSessionBuilder;
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
	private BoxGroupHistoryRepository boxGroupHistoryRepository;
	
	@Autowired
	private SoftwareDownloader softwareDownloader;

	@Autowired
	private SshExecRunner sshExecRunner;
	
	@Autowired
	private ApplicationConfig applicationConfig;
	
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
					boxGroupRepository.save(bg);
					Broadcaster.broadcast(new BroadCasterMessage(new GroupTaskFinishMessage(bgHistoriesSofar, taskDesc.getUniqueUiId())));
					
			}

			@Override
			public void onFailure(Throwable t) {
				LOGGER.error(t.getMessage());
			}
		});
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

			try {
				JschSession jsession = new JschSessionBuilder().setHost(box.getIp()).setKeyFile(applicationConfig.getSshKeyFile(box))
						.setPort(box.getPort()).setSshUser(box.getSshUser()).build();
				
				boolean needUploadFile = needUploadActions.contains(oneThreadtaskDesc.getAction());
				
				if (needUploadFile) {
					sshUploadRunner.run(jsession, oneThreadtaskDesc);
				}
				
				if (oneThreadtaskDesc.getBoxHistory().isSuccess() || !needUploadFile) {
					sshExecRunner.run(jsession, oneThreadtaskDesc);
				}
				if (jsession.getSession().isConnected()) {
					jsession.getSession().disconnect();
				}
			} catch (Exception e) {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				oneThreadtaskDesc.getBoxHistory().appendLogAndSetFailure(sw.toString());
				if (e instanceof java.io.InterruptedIOException) {
					oneThreadtaskDesc.getBoxHistory().appendLogAndSetFailure("-----Notice---------\n, This error message maybe don't mean task failed, Pleach check software state manully.");
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
