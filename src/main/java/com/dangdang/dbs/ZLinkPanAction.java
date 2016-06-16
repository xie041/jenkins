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
public class ZLinkPanAction extends ManagementLink implements RootAction {

	private static final String URL = "https://pan.dangdang.com/";

	public String getIconFileName() {
		return "/plugin/dbs/icons/pan.png";
	}

	public String getDisplayName() {
		return "当当云盘";
	}

	public String getUrlName() {
		return URL;
	}
	
	@Override
	public String getDescription() {
		return "当当云盘";
	}

	public void doIndex(StaplerRequest req, StaplerResponse res)
			throws ServletException, IOException {
//		res.sendRedirect(SONAR_UTL);
	}

}
