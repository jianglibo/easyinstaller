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
import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.domain.StepRun;
import com.jianglibo.vaadin.dashboard.ssh.JschSession;
import com.jianglibo.vaadin.dashboard.ssh.JschSession.JschSessionBuilder;
import com.jianglibo.vaadin.dashboard.sshrunner.BaseRunner;
import com.jianglibo.vaadin.dashboard.sshrunner.SshExecRunner;
import com.jianglibo.vaadin.dashboard.sshrunner.SshUploadRunner;

/**
 * Give a list of box and software pair.
 * 
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
	private SshUploadRunner sshUploadRunner;

	@Autowired
	private SshExecRunner sshExecRunner;

	public TaskRunner() {
		this.service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
	}

	public void submitTasks(TaskDesc taskDesc) {
		ListenableFuture<Void> installResult = service.submit(new OneTaskCallable(taskDesc));
		Futures.addCallback(installResult, new FutureCallback<Void>() {
			public void onSuccess(Void vid) {
				setBoxAvailable(bid);
			}

			public void onFailure(Throwable thrown) {
				setBoxAvailable(bid);
			}
		});
	}

	private class OneTaskCallable implements Callable<Void> {

		private TaskDesc taskDesc;

		public OneTaskCallable(TaskDesc taskDesc) {
			this.taskDesc = taskDesc;
		}

		@Override
		public Void call() throws Exception {
			if (taskDesc.isGroupTask()) {
				
			} else {
				Box box = taskDesc.getBox();
				Software software = taskDesc.getSoftware();
				JschSession jsession = new JschSessionBuilder().setHost(box.getIp()).setKeyFile(box.getKeyFilePath())
						.setPort(box.getPort()).setSshUser(box.getSshUser()).build();
				
				if (software.getFilesToUpload())

			}
			
			for (Install ist : installs) {
				for (StepRun stepRun : ist.getStepRuns()) {
					BaseRunner br = runners.getRunners().get(stepRun.getStepDefine().getRunner());
					br.run(jsession, stepRun);
				}
			}
			if (jsession.getSession().isConnected()) {
				jsession.getSession().disconnect();
			}
			return null;
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
