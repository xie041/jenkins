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
 * pdlc超链接
 */
@Extension
public class ZLinkPdlcAction extends ManagementLink implements RootAction {

	private static final String URL = "http://pdlc.dangdang.com/my_schedule.php";

	public String getIconFileName() {
		return "/plugin/dbs/icons/pdlc.png";
	}

	public String getDisplayName() {
		return "PDLC";
	}

	public String getUrlName() {
		return URL;
	}
	
	@Override
	public String getDescription() {
		return "PDLC";
	}

	public void doIndex(StaplerRequest req, StaplerResponse res)
			throws ServletException, IOException {
//		res.sendRedirect(SONAR_UTL);
	}

}
