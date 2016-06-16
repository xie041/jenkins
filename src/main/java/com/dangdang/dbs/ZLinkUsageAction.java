package com.dangdang.dbs;

import hudson.Extension;
import hudson.model.ManagementLink;
import hudson.model.RootAction;

import java.io.IOException;

import javax.servlet.ServletException;

import jenkins.model.Jenkins;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

@Extension
public class ZLinkUsageAction extends ManagementLink implements RootAction {

	public String getIconFileName() {
		return "/plugin/dbs/icons/dang.png";
	}

	public String getDisplayName() {
		return "DBS功能说明";
	}

	public String getUrlName() {
		return "dbs";
	}
	
	@Override
	public String getDescription() {
		return "DBS功能说明";
	}

	public void doIndex(StaplerRequest req, StaplerResponse res)
			throws ServletException, IOException {
		res.sendRedirect(Jenkins.getInstance().getRootUrl() + "plugin/dbs");
	}

}
