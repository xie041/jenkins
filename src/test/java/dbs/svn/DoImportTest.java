package dbs.svn;

import com.dangdang.dbs.utils.FormBean;
import com.dangdang.dbs.utils.SvnImport;

public class DoImportTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		FormBean bean = new FormBean();
		//"b", "xieyong", "tzmm", "E:\\sv", "main test"
		bean.setDstURL("b");
		bean.setFileWillBeCopy("E:\\sv");
		bean.setUser("xieyong");
		bean.setPwd("tzmm.978");
		bean.setSvnMsg("x");
		SvnImport.upload(bean);
	}
}
