package com.dangdang.dbs.utils;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNClientManager;

public class SvnUploadRunner implements Callable<String> {
	
	private SvnUploader info;
	private CountDownLatch latch;
	
	public SvnUploadRunner(SvnUploader info,CountDownLatch latch){
		this.info = info;
		this.latch = latch;
	}

	@Override
	public String call() throws Exception {
		
		try {
			SVNURL dstURL = SVNURL.parseURIEncoded(info.getDstURL());
			String fileWillBeCopy = info.getFileWillBeCopy();
			File localPath = new File(fileWillBeCopy);
			if (!localPath.exists()) {
				return "上传文件未找到!";
			}

			DbsSVNUtils utils = DbsSVNUtils.getInstance(null);

			SVNClientManager manager = utils.authSvn(info.getDstURL(),info.getUser(),info.getPwd());
			if (manager == null) {
				return "svn建立连接失败，svn路径不存在或者svn用户名密码不正确!";
			}
			
			SVNCommitInfo delDir = utils.delDir(manager, dstURL, "自动删除svn路径");
			if(delDir == null){
				return "删除svn路径失败:"+dstURL;
			}
			SVNCommitInfo makeDirectory = utils.makeDirectory(manager,dstURL, "自动创建svn路径");
			if(makeDirectory == null){
				return "自动创建svn路径失败:"+dstURL;
			}
			SVNCommitInfo info = utils.importDirectory(manager, localPath, dstURL,"demo", true);
			
			return info.getAuthor() + ":" + info.getNewRevision();
			
		} catch (Exception e) {
			
			return e.getMessage();
			
		} finally {
			
			latch.countDown();
			
		}
	}

}
