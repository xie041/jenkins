package com.dangdang.dbs.utils;

import java.io.File;

import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class SvnImport {

	public static String upload(FormBean bean) {
		// 声明SVN客户端管理类
		SVNClientManager ourClientManager = null;
		/*
		 * For using over http:// and https://
		 */
		DAVRepositoryFactory.setup();
		// 相关变量赋值
		SVNURL repositoryURL = null;
		try {
			repositoryURL = SVNURL.parseURIEncoded(bean.getDstURL());
		} catch (SVNException e) {
			//
			e.printStackTrace();
			return e.toString();
		}
		ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
		// 实例化客户端管理类
		ourClientManager = SVNClientManager.newInstance(
				(DefaultSVNOptions) options, bean.getUser(), bean.getPwd());
		// 要把此目录中的内容导入到版本库
		File impDir = new File(bean.getFileWillBeCopy());
		// 执行导入操作
		SVNCommitInfo commitInfo = null;
		try {
			commitInfo = ourClientManager.getCommitClient().doImport(impDir,
					repositoryURL, bean.getSvnMsg(),
					null, false, false, SVNDepth.INFINITY);
			return commitInfo.getAuthor() + " >>> " + commitInfo.getNewRevision();
		} catch (SVNException e) {
			e.printStackTrace();
			return e.toString();
		}
	}

}
