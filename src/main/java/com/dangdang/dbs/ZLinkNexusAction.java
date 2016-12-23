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
public class ZLinkNexusAction extends ManagementLink implements RootAction {

	private static final String URL = "http://maven.dangdang.com/nexus/index.html";

	public String getIconFileName() {
		return "/plugin/dbs/icons/nexus.png";
	}

	public String getDisplayName() {
		return "Maven私服";
	}

	public String getUrlName() {
		return URL;
	}
	
	@Override
	public String getDescription() {
		return "Maven私服";
	}

	public void doIndex(StaplerRequest req, StaplerResponse res)
			throws ServletException, IOException {
//		res.sendRedirect(SONAR_UTL);
	}

}
