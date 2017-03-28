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
		SvnImport.upload(bean);
	}
}
