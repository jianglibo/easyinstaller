package com.jianglibo.vaadin.dashboard.util;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

public class TestThreadId {
	
	private volatile int count;
	
	private List<Integer> ints;
	
	private synchronized void add(int i) {
		this.ints.add(i);
	}

	@Test
	public void t() {
		ints = Lists.newArrayList();
		count = 0;
		
		Thread ct = Thread.currentThread();
		
		for(int j =0 ; j < 100; j++) {
			new Thread(() -> {
				add(ThreadId.get());
				count++;
				if (count == 100) {
					ct.interrupt();
				}
			}).start();
		}
		try {
			Thread.sleep(10000000);
		} catch (InterruptedException e) {
		}
		assertThat(ints.size(), equalTo(100));
		
		ints.sort(Integer::compare);
		System.out.println(ints);
		
	}
}
