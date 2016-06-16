package com.dangdang.dbs;

import hudson.Extension;
import hudson.model.ManagementLink;
import hudson.model.RootAction;

import java.io.IOException;

import javax.servlet.ServletException;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 * @author xieyong
 * sonar超链接
 */
@Extension
public class ZLinkBugzillaAction extends ManagementLink implements RootAction {

	private static final String URL = "http://bugzilla.dangdang.com/";

	public String getIconFileName() {
		return "/plugin/dbs/icons/bugzilla.png";
	}

	public String getDisplayName() {
		return "Bugzilla";
	}

	public String getUrlName() {
		return URL;
	}
	
	@Override
	public String getDescription() {
		return "Bugzilla bug系统";
	}

	public void doIndex(StaplerRequest req, StaplerResponse res)
			throws ServletException, IOException {
//		res.sendRedirect(SONAR_UTL);
	}

}
