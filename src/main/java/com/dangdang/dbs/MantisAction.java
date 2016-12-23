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
public class MantisAction extends ManagementLink implements RootAction {

	private static final String URL = "http://mantis.dangdang.com/mantisbt/my_view_page.php";

	public String getIconFileName() {
		return "/plugin/dbs/icons/d.gif";
	}

	public String getDisplayName() {
		return "Mantis";
	}

	public String getUrlName() {
		return URL;
	}
	
	@Override
	public String getDescription() {
		return "Mantis";
	}

	public void doIndex(StaplerRequest req, StaplerResponse res)
			throws ServletException, IOException {
		res.sendRedirect(URL);
	}

}
