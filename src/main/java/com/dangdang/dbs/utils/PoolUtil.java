package com.dangdang.dbs.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PoolUtil {
	
	public static ExecutorService pool = Executors.newFixedThreadPool(8);

}
