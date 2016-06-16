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
public class ZLinkTwikiAction extends ManagementLink implements RootAction {

	private static final String URL = "http://twiki.dangdang.com/twiki/bin/view.pl";

	public String getIconFileName() {
		return "/plugin/dbs/icons/twiki.jpg";
	}

	public String getDisplayName() {
		return "Twiki";
	}

	public String getUrlName() {
		return URL;
	}
	
	@Override
	public String getDescription() {
		return "wiki系统";
	}

	public void doIndex(StaplerRequest req, StaplerResponse res)
			throws ServletException, IOException {
//		res.sendRedirect(SONAR_UTL);
	}

}
