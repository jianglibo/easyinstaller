package com.jianglibo.vaadin.dashboard.taskrunner;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.BoxGroup;
import com.jianglibo.vaadin.dashboard.domain.BoxHistory;
import com.jianglibo.vaadin.dashboard.repositories.BoxGroupRepository;
import com.jianglibo.vaadin.dashboard.repositories.BoxHistoryRepository;
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
public class TaskRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskRunner.class);

	private ListeningExecutorService service;

	@Autowired
	private BoxHistoryRepository boxHistoryRepository;
	
	@Autowired
	private BoxGroupRepository boxGroupRepository;

	@Autowired
	private SshUploadRunner sshUploadRunner;

	@Autowired
	private SshExecRunner sshExecRunner;

	public TaskRunner() {
		this.service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
	}

	public void submitTasks(TaskDesc initTaskDesc) {
		List<OneThreadTaskDesc> onetds = initTaskDesc.createOneThreadTaskDescs();

		List<ListenableFuture<OneThreadTaskDesc>> llfs = onetds.stream().map(td -> service.submit(new OneTaskCallable(td)))
				.collect(Collectors.toList());

		ListenableFuture<List<OneThreadTaskDesc>> lf = Futures.successfulAsList(llfs);

		/**
		 * this listener got called when a taskDesc complete.
		 */
		Futures.addCallback(lf, new FutureCallback<List<OneThreadTaskDesc>>() {
			@Override
			public void onSuccess(List<OneThreadTaskDesc> result) {
				if (initTaskDesc.isGroupTask()) {
					List<BoxHistory> bhs = Lists.newArrayList();
					result.stream().forEach(td -> {
						BoxHistory bh = boxHistoryRepository.save(td.getBoxHistory());
						Box box = td.getBox();
						box.getBoxHistories().add(bh);
						bhs.add(bh);
					});
					
					BoxGroup bg = initTaskDesc.getBoxGroup();
					bg.getHistories().addAll(bhs);
					boxGroupRepository.save(bg);
				} else { // single box run.
					OneThreadTaskDesc td = result.get(0);
					BoxHistory bh = boxHistoryRepository.save(td.getBoxHistory());
					Box box = td.getBox();
					box.getBoxHistories().add(bh);
				}
			}

			@Override
			public void onFailure(Throwable t) {
				LOGGER.error(t.getMessage());
			}
		});
	}

	private class OneTaskCallable implements Callable<OneThreadTaskDesc> {

		private OneThreadTaskDesc oneThreadtaskDesc;

		public OneTaskCallable(OneThreadTaskDesc taskDesc) {
			this.oneThreadtaskDesc = taskDesc;
		}

		@Override
		public OneThreadTaskDesc call() throws Exception {
			Box box = oneThreadtaskDesc.getBox();
			JschSession jsession = new JschSessionBuilder().setHost(box.getIp()).setKeyFile(box.getKeyFilePath())
					.setPort(box.getPort()).setSshUser(box.getSshUser()).build();
			sshUploadRunner.run(jsession, oneThreadtaskDesc);
			if (oneThreadtaskDesc.getBoxHistory().isSuccess()) {
				sshExecRunner.run(jsession, oneThreadtaskDesc);
			}
			oneThreadtaskDesc.notifyOneTaskFinished();
			if (jsession.getSession().isConnected()) {
				jsession.getSession().disconnect();
			}
			return oneThreadtaskDesc;
		}
	}
}
