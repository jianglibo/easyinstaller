package com.jianglibo.vaadin.dashboard.taskrunner;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.ssh.JschSession;
import com.jianglibo.vaadin.dashboard.ssh.JschSession.JschSessionBuilder;
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
		List<TaskDesc> tds;

		if (taskDesc.isGroupTask()) {
			tds = taskDesc.getBoxGroup().getBoxes().stream().map(b -> new TaskDesc(b, taskDesc.getSoftware()))
					.collect(Collectors.toList());
		} else {
			tds = Lists.newArrayList(taskDesc);
		}

		List<ListenableFuture<TaskDesc>> llfs = tds.stream().map(td -> service.submit(new OneTaskCallable(td)))
				.collect(Collectors.toList());

		ListenableFuture<List<TaskDesc>> lf = Futures.successfulAsList(llfs);

		Futures.addCallback(lf, new FutureCallback<List<TaskDesc>>() {

			@Override
			public void onSuccess(List<TaskDesc> result) {
			}

			@Override
			public void onFailure(Throwable t) {
			}
		});
	}

	private class OneTaskCallable implements Callable<TaskDesc> {

		private TaskDesc taskDesc;

		public OneTaskCallable(TaskDesc taskDesc) {
			this.taskDesc = taskDesc;
		}

		@Override
		public TaskDesc call() throws Exception {
			Box box = taskDesc.getBox();
			JschSession jsession = new JschSessionBuilder().setHost(box.getIp()).setKeyFile(box.getKeyFilePath())
					.setPort(box.getPort()).setSshUser(box.getSshUser()).build();

			sshUploadRunner.run(jsession, box, taskDesc);
			if (taskDesc.getHistory(box).isSuccess()) {
				sshExecRunner.run(jsession, box, taskDesc);
			}
			if (jsession.getSession().isConnected()) {
				jsession.getSession().disconnect();
			}
			return taskDesc;
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
