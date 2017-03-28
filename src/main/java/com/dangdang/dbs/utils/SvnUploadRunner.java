package com.dangdang.dbs.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class SvnUploadRunner implements Callable<String> {
	
	private FormBean bean;
	private CountDownLatch latch;
	
	public SvnUploadRunner(FormBean bean,CountDownLatch latch){
		this.bean = bean;
		this.latch = latch;
	}

	@Override
	public String call() throws Exception {
		
		try {
			return SvnImport.upload(bean);
			
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
			
		} finally {
			
			latch.countDown();
			
		}
	}

}
