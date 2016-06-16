package dbs.svn;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.io.ISVNReporter;
import org.tmatesoft.svn.core.io.ISVNReporterBaton;

public class MyReporterBaton implements ISVNReporterBaton {
	
	public void report(ISVNReporter reporter) throws SVNException {

		// for the root directory
		reporter.setPath("", null, 4, false);

		// for "/dirA"
		reporter.setPath("/dirA", null, 5, false);

		// for "/dirA/dirB"
		reporter.setPath("/dirA/dirB", null, 6, false);

		// for "/dirA/dirB/file1.txt"
		reporter.setPath("/dirA/dirB/file1.txt", null, 6, false);

		// for "/dirA/dirC"
		reporter.setPath("/dirA/dirC", null, 4, false);

		// and so on for all entries which revisions differ from 8

		/*
		 * always called at the end of the report - when the state of the entire
		 * tree is described.
		 */
		reporter.finishReport();
	}
}
