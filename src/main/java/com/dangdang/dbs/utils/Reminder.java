package com.dangdang.dbs.utils;

import hudson.model.BuildListener;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Reminder implements Runnable {
	
	private CountDownLatch latch;
	private BuildListener listener;

	public Reminder(CountDownLatch latch,BuildListener listener){
		this.latch = latch;
		this.listener = listener;
	}

	@Override
	public void run() {
		try {
			for (int i = 0; i < 1000; i++) {
				long count = latch.getCount();
				if (count > 1) {
					if(listener == null){
						System.out.println("svn上传中，请耐心等待....");
					}else{
						listener.getLogger().println("svn上传中，请耐心等待....");
					}
					TimeUnit.SECONDS.sleep(3);
				} else {
					break;
				}
			}
		} catch (InterruptedException e) {
			//do nothing
		} finally{
			latch.countDown();
		}

	}

}
