package com.dangdang.dbs.utils;

import hudson.model.BuildListener;

import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * 系统底层脚本执行工具
 * @author xieyong
 */
public class ShellUtils {

	/**
	 * java在linux环境下执行linux命令，然后返回命令返回值。 cmd /c dir 是执行完dir命令后关闭命令窗口。 cmd /k dir
	 * 是执行完dir命令后不关闭命令窗口。 cmd /c start dir 会打开一个新窗口后执行dir指令，原窗口会关闭。 cmd /k start
	 * dir 会打开一个新窗口后执行dir指令，原窗口不会关闭。
	 * 
	 * 
	 * https://wiki.jenkins-ci.org/display/JENKINS/Hints+for+plugin-development+newbies
	 * @param exec
	 * @param listener
	 * @return
	 */
	public static String runShell(String exec, BuildListener listener) {
		Process process = null;
		try {
			String os = System.getProperty("os.name").toLowerCase();
			if (os.contains("linux")) {
				String[] cmd = { "/bin/sh", "-c", exec };
				process = Runtime.getRuntime().exec(cmd);
			} else if (os.contains("windows")) {
				process = Runtime.getRuntime().exec("cmd /c " + exec);
			} else {
				return null;
			}
			LineNumberReader reader = new LineNumberReader(
					new InputStreamReader(process.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			reader.close();
			return sb.toString();
		} catch (Exception e) {
			listener.getLogger().println(e.getLocalizedMessage());
		} finally {
			if (process != null) {
				process.destroy();
			}
		}
		return null;
	}
}
