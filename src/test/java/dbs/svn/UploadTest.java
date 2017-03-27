package dbs.svn;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.dangdang.dbs.utils.PoolUtil;
import com.dangdang.dbs.utils.Reminder;
import com.dangdang.dbs.utils.SvnUploadRunner;
import com.dangdang.dbs.utils.SvnUploader;

public class UploadTest {
	
	public static void main(String[] args) {
		
		CountDownLatch latch = new CountDownLatch(2);
		SvnUploader s = new SvnUploader();
		s.setDstURL(SvnUploader.SVN_BASE + "b");
		s.setFileWillBeCopy("D:\\d\\workspace_git\\jenkins\\work\\jobs\\a\\workspace");
		s.setUser("xieyong");
		s.setPwd("tz***");
		s.setSvnMsg("demo");
		
		PoolUtil.pool.execute(new Reminder(latch,null));
		Future<String> f = PoolUtil.pool.submit(new SvnUploadRunner(s,latch));
		try {
			latch.await();
			String r = f.get();
			System.out.println("f_latch_count=" + latch.getCount() + "");
			System.out.println(r);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

}
