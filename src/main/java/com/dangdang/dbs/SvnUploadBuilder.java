package com.dangdang.dbs;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Cause;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.servlet.ServletException;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import com.dangdang.dbs.utils.FormBean;
import com.dangdang.dbs.utils.PoolUtil;
import com.dangdang.dbs.utils.Reminder;
import com.dangdang.dbs.utils.SvnUploadRunner;

/**
 * Sample {@link Builder}.
 * 
 * <p>
 * When the user configures the project and enables this builder,
 * {@link DescriptorImpl#newInstance(StaplerRequest)} is invoked and a new
 * {@link SvnUploadBuilder} is created. The created instance is persisted to the
 * project configuration XML by using XStream, so this allows you to use
 * instance fields (like {@link #name}) to remember the configuration.
 * 
 * <p>
 * When a build is performed, the
 * {@link #perform(AbstractBuild, Launcher, BuildListener)} method will be
 * invoked.
 * 
 * svn代码上传到测试svn路径下
 * 
 * @author xieyong
 * 
 */
public class SvnUploadBuilder extends Builder {

	private final boolean forbidden;
	private final String svnRoot;
	private final String uploadFiles;
	private final String username;
	private final String password;
	private final String message;

	// Fields in config.jelly must match the parameter names in the
	// "DataBoundConstructor"
	@DataBoundConstructor
	public SvnUploadBuilder(boolean forbidden, String svnRoot, String uploadFiles,
			String username, String password, String message) {
		this.forbidden = forbidden;
		this.svnRoot = svnRoot.trim();
		this.uploadFiles = uploadFiles.trim();
		this.username = username.trim();
		this.password = password.trim();
		this.message = message.trim();
	}

	/**
	 * We'll use this from the <tt>config.jelly</tt>.
	 */
	public String getSvnRoot() {
		return svnRoot;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getUploadFiles() {
		return uploadFiles;
	}

	public String getMessage() {
		return message;
	}

	public boolean isForbidden() {
		return forbidden;
	}

	@Override
	public boolean perform(AbstractBuild build, Launcher launcher,
			BuildListener listener) {

		List<Cause> buildStepCause = new ArrayList<Cause>();
		buildStepCause.add(new Cause() {
			public String getShortDescription() {
				return "DBS(Svn Uploader) running....";
			}
		});

		listener.started(buildStepCause); // 向jenkins控制台输出日志
		if (Boolean.valueOf(forbidden)) {
			logger(listener, "Svn Uploader功能未激活，即将忽略...");
			return true;
		}
		
		long start = System.currentTimeMillis();
		FormBean bean = new FormBean();
		bean.setDstURL(svnRoot);
		String fileWillBeCopy = null;
		if(StringUtils.isNotBlank(uploadFiles)){
			fileWillBeCopy = build.getWorkspace()+ File.separator + uploadFiles;
		}else{
			fileWillBeCopy = build.getWorkspace() + "" ;
		}
		bean.setFileWillBeCopy(fileWillBeCopy);
		bean.setUser(username);
		bean.setPwd(password);
		bean.setSvnMsg(message);
		System.out.println(bean);
		CountDownLatch latch = new CountDownLatch(2);
		PoolUtil.pool.execute(new Reminder(latch ,listener));
		Future<String> f = PoolUtil.pool.submit(new SvnUploadRunner(bean,latch));

		try {
			latch.await();
			logger(listener, f.get());
			logger(listener, "cost=" + (System.currentTimeMillis() - start) + "ms");
			suc(listener);
		} catch (InterruptedException | ExecutionException e) {
			logger(listener, e.getMessage());
			return false;
		}
		return true;
	}

	private void suc(BuildListener listener) {
		logger(listener, "+++++++++++++++++++++++++++++++++++++++++");
		logger(listener, "+                                       +");
		logger(listener, "+                                       +");
		logger(listener, "+        DBS SVN UPLOAD FINISHED!       +");
		logger(listener, "+                                       +");
		logger(listener, "+                                       +");
		logger(listener, "+++++++++++++++++++++++++++++++++++++++++");
	}

	private void logger(BuildListener listener, String msg) {
		listener.getLogger().println(msg);
	}
	private void logger(BuildListener listener, Object msg) {
		listener.getLogger().println(msg);
	}

	// Overridden for better type safety.
	// If your plugin doesn't really define any property on Descriptor,
	// you don't have to do this.
	@Override
	public DescriptorImpl getDescriptor() {
		return (DescriptorImpl) super.getDescriptor();
	}

	/**
	 * Descriptor for {@link SvnUploadBuilder}. Used as a singleton. The class is
	 * marked as public so that it can be accessed from views.
	 * 
	 * <p>
	 * See
	 * <tt>src/main/resources/hudson/plugins/hello_world/HelloWorldBuilder/*.jelly</tt>
	 * for the actual HTML fragment for the configuration screen.
	 */
	@Extension
	// This indicates to Jenkins that this is an implementation of an extension
	// point.
	public static final class DescriptorImpl extends
			BuildStepDescriptor<Builder> {
		/**
		 * To persist global configuration information, simply store it in a
		 * field and call save().
		 * 
		 * <p>
		 * If you don't want fields to be persisted, use <tt>transient</tt>.
		 */
		// private boolean useFrench;

		/**
		 * In order to load the persisted global configuration, you have to call
		 * load() in the constructor.
		 */
		// public DescriptorImpl() {
		// load();
		// }

		/**
		 * Performs on-the-fly validation of the form field 'name'.
		 * 
		 * @param value
		 *            This parameter receives the value that the user has typed.
		 * @return Indicates the outcome of the validation. This is sent to the
		 *         browser.
		 *         <p>
		 *         Note that returning {@link FormValidation#error(String)} does
		 *         not prevent the form from being saved. It just means that a
		 *         message will be displayed to the user.
		 */
		public FormValidation doCheckName(
				@QueryParameter("svnRoot") String svnRoot,
				@QueryParameter("uploadFiles") String uploadFiles,
				@QueryParameter("username") String username,
				@QueryParameter("password") String password,
				@QueryParameter("message") String message) throws IOException,
				ServletException {

			return FormValidation.ok();
		}

		public boolean isApplicable(Class<? extends AbstractProject> aClass) {
			// Indicates that this builder can be used with all kinds of project
			// types
			return true;
		}

		/**
		 * This human readable name is used in the configuration screen.
		 */
		public String getDisplayName() {
			return "DBS(Svn Uploader)";
		}

		@Override
		public boolean configure(StaplerRequest req, JSONObject formData)
				throws FormException {
			// To persist global configuration information,
			// set that to properties and call save().
			// useFrench = formData.getBoolean("useFrench");
			// ^Can also use req.bindJSON(this, formData);
			// (easier when there are many fields; need set* methods for this,
			// like setUseFrench)
			save();
			return super.configure(req, formData);
		}

		/**
		 * This method returns true if the global configuration says we should
		 * speak French.
		 * 
		 * The method name is bit awkward because global.jelly calls this method
		 * to determine the initial state of the checkbox by the naming
		 * convention.
		 */
		// public boolean getUseFrench() {
		// return useFrench;
		// }
	}
}
