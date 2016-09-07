package com.jianglibo.vaadin.dashboard.taskrunner;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.gwt.thirdparty.guava.common.collect.Maps;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.Install;
import com.jianglibo.vaadin.dashboard.domain.StepRun;
import com.jianglibo.vaadin.dashboard.ssh.JschSession;
import com.jianglibo.vaadin.dashboard.ssh.JschSession.JschSessionBuilder;
import com.jianglibo.vaadin.dashboard.sshrunner.BaseRunner;
import com.jianglibo.vaadin.dashboard.sshrunner.Runners;

/**
 * It's not safe to run one box's installation in different thread because of
 * order.
 * 
 * For a box, process installation by the order. First arrive first got
 * executed.
 * 
 * @author jianglibo@gmail.com
 *
 */
@Component
public class TaskRunner implements ApplicationContextAware {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskRunner.class);

	private ListeningExecutorService service;

	private final Lock lock = new ReentrantLock();
	
	private ApplicationContext applicationContext;
	
	@Autowired
	private Runners runners;

	// list of installation must belongs to same box;
	private final Map<Long, Queue<List<Install>>> boxidMap2WaitingToInstalls = Maps.newHashMap();

	private final Map<Long, Boolean> boxidMap2AvailableState = Maps.newHashMap();

	public TaskRunner() {
		this.service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
	}

	public void submitTasks(List<Install> installs) {
		lock.lock(); // block until condition holds
		try {
			if (checkValidate(installs)) {
				Long boxid = installs.get(0).getBox().getId();
				if (boxidMap2WaitingToInstalls.get(boxid) == null) {
					boxidMap2WaitingToInstalls.put(boxid, new LinkedBlockingQueue<List<Install>>());
				}
				boxidMap2WaitingToInstalls.get(boxid).add(installs);
				if (!boxidMap2AvailableState.containsKey(boxid)) {
					boxidMap2AvailableState.put(boxid, true);
				}
			}
		} finally {
			lock.unlock();
		}
	}

	private boolean checkValidate(List<Install> installs) {
		if (installs == null || installs.isEmpty()) {
			return false;
		}
		Box box = installs.get(0).getBox();
		for (Install ist : installs) {
			if (ist.getBox().getId() != box.getId()) {
				LOGGER.error("all installs in a batch submit must belong to same box: {}", ist.toString());
				return false;
			}
		}
		return true;
	}

	private List<Long> getAvailableBox() {
		lock.lock(); // block until condition holds
		try {
			return boxidMap2AvailableState.entrySet().stream().filter(se -> se.getValue()).map(se -> se.getKey())
					.collect(Collectors.toList());
		} finally {
			lock.unlock();
		}
	}

	private void setBoxAvailable(Long bid) {
		lock.lock(); // block until condition holds
		try {
			boxidMap2AvailableState.put(bid, true);
		} finally {
			lock.unlock();
		}
	}

	@Scheduled(fixedDelay = 1000)
	protected void consumeTasks() {
		getAvailableBox().stream().forEach(bid -> {
			Queue<List<Install>> waitings = boxidMap2WaitingToInstalls.get(bid);
			if (waitings != null) {
				executeOne(waitings.poll());
			}
		});
	}

	private void executeOne(List<Install> installs) {
		if (installs == null) {
			return;
		}
		Long bid = installs.get(0).getBox().getId();
		ListenableFuture<Void> installResult = service.submit(new OneBoxTaskCallable(installs));
		Futures.addCallback(installResult, new FutureCallback<Void>() {
			public void onSuccess(Void vid) {
				setBoxAvailable(bid);
			}

			public void onFailure(Throwable thrown) {
				setBoxAvailable(bid);
			}
		});
	}

	private class OneBoxTaskCallable implements Callable<Void> {

		private List<Install> installs;

		public OneBoxTaskCallable(List<Install> installs) {
			this.installs = installs;
		}

		@Override
		public Void call() throws Exception {
			JschSession jsession = new JschSessionBuilder().build();
			for (Install ist : installs) {
				for(StepRun stepRun: ist.getStepRuns()) {
					BaseRunner br = runners.getRunners().get(stepRun.getStepDefine().getRunner());
					br.run(jsession, stepRun);
				}
			}
			return null;
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
