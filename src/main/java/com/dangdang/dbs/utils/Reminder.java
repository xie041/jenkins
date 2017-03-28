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
						if(i == 5){
							listener.getLogger().println("svn上传中，请耐心等待....");
						}else if(i == 10){
							listener.getLogger().println("兴许还要一会，别急");
						}else if(i == 30){
							listener.getLogger().println("去接一杯水吧，等你回来就OK了");
						}else if(i == 45){
							listener.getLogger().println("请一定相信我，肯定会上传成功的");
						}else if(i == 50){
							listener.getLogger().println("SVN服务器太TMD慢了，我也没办法");
						}else if(i > 60){
							listener.getLogger().println("什么破服务器，我都想砸了");
						}
					}
					TimeUnit.SECONDS.sleep(3);
				} else {
					listener.getLogger().println("上传完毕!");
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
