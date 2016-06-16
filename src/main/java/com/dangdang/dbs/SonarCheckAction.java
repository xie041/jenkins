package com.dangdang.dbs;

import hudson.Extension;
import hudson.model.ManagementLink;
import hudson.model.RootAction;

import java.io.IOException;

import javax.servlet.ServletException;

import jenkins.model.Jenkins;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 * @author xieyong
 * sonar超链接
 */
@Extension
public class SonarCheckAction extends ManagementLink implements RootAction {

	public String getIconFileName() {
		return "/plugin/dbs/icons/d.gif";
	}

	public String getDisplayName() {
		return "Sonar代码检查";
	}

	//需要在jenkins中创建sonar_check的job，否则程序会报错，总之必须要求sonar_check存在
	public String getUrlName() {
		return "/job/sonar_check";
	}
	
	@Override
	public String getDescription() {
		return "Sonar代码检查";
	}

	public void doIndex(StaplerRequest req, StaplerResponse res)
			throws ServletException, IOException {
		res.sendRedirect("/job/sonar_check");
	}

}
