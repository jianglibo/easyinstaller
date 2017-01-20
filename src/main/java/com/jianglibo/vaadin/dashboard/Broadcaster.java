package com.jianglibo.vaadin.dashboard;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("serial")
public class Broadcaster implements Serializable {

	static ExecutorService executorService = Executors.newSingleThreadExecutor();

	public interface BroadcastListener {
		void receiveBroadcast(BroadCasterMessage message);
	}

	private static LinkedList<BroadcastListener> listeners = new LinkedList<BroadcastListener>();

	public static synchronized void register(BroadcastListener listener) {
		listeners.add(listener);
	}

	public static synchronized void unregister(BroadcastListener listener) {
		listeners.remove(listener);
	}

	public static synchronized void broadcast(final BroadCasterMessage message) {
		for (final BroadcastListener listener : listeners)
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					listener.receiveBroadcast(message);
				}
			});
	}
	
	public static interface BroadCasterMessageBody {
		BroadCasterMessageType getBroadCasterMessageType();
	}
	
	public static enum BroadCasterMessageType {
		NEW_SOFTWARE, NEW_NEWS, DOWNLOAD, ONE_TASK_FINISH, GROUP_TASK_FINISH, RUNNING_THREADS,RETURNCLIENT_DOWNLOAD
	}
	
	public static class BroadCasterMessage {
		private BroadCasterMessageBody body;
		private BroadCasterMessageType bcmt;
		
		public BroadCasterMessage(BroadCasterMessageBody body) {
			super();
			this.body = body;
			this.bcmt = body.getBroadCasterMessageType();
		}
		public BroadCasterMessageBody getBody() {
			return body;
		}
		public void setBody(BroadCasterMessageBody body) {
			this.body = body;
		}
		public BroadCasterMessageType getBcmt() {
			return bcmt;
		}
		public void setBcmt(BroadCasterMessageType bcmt) {
			this.bcmt = bcmt;
		}
	}

}
