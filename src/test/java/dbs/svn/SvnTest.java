package dbs.svn;

import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCommitClient;

import com.dangdang.dbs.utils.DbsSVNUtils;

public class SvnTest {

	public static void main(String[] args) throws SVNException {

//		SVNClientManager ma = DbsSVNUtils.authSvn("http://svn.dangdang.com/repos/job/price_pub/dbs/upload", "xieyong", "tzmm.978");
////		SVNUtil.makeDirectory(ma,SVNURL.parseURIEncoded("file:///D:/d/svn/repository/demo/trunk"), "创建trunk分支");
//		SVNCommitInfo info = SVNUtil.importDirectory(ma,new File("D:\\d\\test\\price-api"), SVNURL.parseURIEncoded("file:///D:/d/svn/repository/demo/"), "init", true);
//		System.out.println(info == null);//http://svn.dangdang.com/repos/job/price_pub/dbs/upload
		String url = "http://svn.dangdang.com/repos/job/price_pub/dbs/upload/";
		DbsSVNUtils utils = DbsSVNUtils.getInstance(null);
		SVNClientManager manager = utils.authSvn(url, "xieyong","tzmm.978");
//		boolean exist = DbsSVNUtils.isURLExist(SVNURL.parseURIEncoded(url), "xieyong","tzmm.978");
//		System.out.println(exist);
		
		SVNCommitClient client = manager.getCommitClient();
		SVNURL[] urls = {SVNURL.parseURIEncoded(url+"3.0.0.1")};
		try {
			SVNCommitInfo doDelete = client.doDelete(urls, "delete");
			utils.log(doDelete);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		SVNCommitInfo doMkDir = client.doMkDir(urls, "init");
		utils.log(doMkDir);
	}

}
