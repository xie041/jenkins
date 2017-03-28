package com.dangdang.dbs.utils;


/**
 * svn对象
 * @author xieyong
 *
 */
public class FormBean {
	
	public static final String SVN_BASE = "http://svn.dangdang.com/repos/dev/ly_price/pangu/";
	
	private String dstURL;
	private String fileWillBeCopy;
	private String user;
	private String pwd;
	private String svnMsg;
	
	public String getDstURL() {
		return FormBean.SVN_BASE + dstURL;
	}
	public void setDstURL(String dstURL) {
		this.dstURL = dstURL;
	}
	public String getFileWillBeCopy() {
		return fileWillBeCopy;
	}
	public void setFileWillBeCopy(String fileWillBeCopy) {
		this.fileWillBeCopy = fileWillBeCopy.trim();
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user.trim();
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd.trim();
	}
	public String getSvnMsg() {
		return svnMsg;
	}
	public void setSvnMsg(String svnMsg) {
		this.svnMsg = svnMsg.trim();
	}
	@Override
	public String toString() {
		return "FormBean [dstURL=" + dstURL + ", fileWillBeCopy="
				+ fileWillBeCopy + ", user=" + user + ", pwd=" + pwd
				+ ", svnMsg=" + svnMsg + "]";
	}

}
