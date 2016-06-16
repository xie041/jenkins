package com.dangdang.dbs.utils;

public class AnsiColor {
	
	public static final String HIGHT_LIGHT_BEGIN = "\033[1m";
	public static final String END = "\033[0m";
	public static final String RED = "\033[0;31m";
	public static final String BLACK = "\033[0;30m";
	public static final String GREEN = "\033[0;32m";
	public static final String BLUE = "\033[0;34m";
	public static final String PURPLE = "\033[0;35m";
	public static final String BACKGROUND_YELLOW = "\033[43m";

	public static String rendering(String prefix,String content){
		StringBuilder builder = new StringBuilder(prefix);
		builder.append(content);
		builder.append(END);
		return builder.toString(); 
	}
}
