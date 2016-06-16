package com.dangdang.dbs;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Cause;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.ArgumentListBuilder;
import hudson.util.FormValidation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import static com.dangdang.dbs.utils.AnsiColor.*;

/**
 * Sample {@link Builder}.
 * 
 * <p>
 * When the user configures the project and enables this builder,
 * {@link DescriptorImpl#newInstance(StaplerRequest)} is invoked and a new
 * {@link DbsBuilder} is created. The created instance is persisted to the
 * project configuration XML by using XStream, so this allows you to use
 * instance fields (like {@link #name}) to remember the configuration.
 * 
 * <p>
 * When a build is performed, the
 * {@link #perform(AbstractBuild, Launcher, BuildListener)} method will be
 * invoked.
 * 
 * 
 * 
 * https://wiki.jenkins-ci.org/display/JENKINS/Extend+Jenkins
 * @author xieyong
 */
public class DbsBuilder extends Builder {

	private static final String PROJECT_STARTUP_SH = "bin/startup.sh";//项目启动脚本
	private static final String DEPLOY_SHELL_TOMCAT = "/home/d/tools/bin/dbs_tomcat.sh";
	private static final String DEPLOY_SHELL_JAR = "/home/d/tools/bin/dbs_jar.sh";
	private static final String RUN_MODE_TOMCAT = "tomcat";
	private static final String RUN_MODE_JAR = "jar";

	private final boolean iswork;
	private final String serverList;
	private final String runMode;
	private final String name;
	private final String scpFiles;
	private final String shell;
	private final boolean shellExec;

	// Fields in config.jelly must match the parameter names in the
	// "DataBoundConstructor"
	@DataBoundConstructor
	public DbsBuilder(boolean iswork,String serverList, String runMode,
			String name, String scpFiles,String shell,boolean shellExec) {
		this.iswork = iswork;
		this.serverList = serverList.trim();
		this.runMode = runMode.trim();
		this.name = name.trim();
		this.scpFiles = scpFiles.trim();
		this.shell = shell.trim();
		this.shellExec = shellExec;
	}

	/**
	 * 备注：如果属性没有提供get方法，会导致页面填值保存后，再次刷新，却没有值的情况
	 * We'll use this from the <tt>config.jelly</tt>.
	 */
	public String getServerList() {
		return serverList;
	}

	public String getRunMode() {
		return runMode;
	}

	public String getName() {
		return name;
	}

	public String getScpFiles() {
		return scpFiles;
	}

	public boolean getIswork() {
		return iswork;
	}

	public String getShell() {
		return shell;
	}

	public boolean isShellExec() {
		return shellExec;
	}

	@Override
	public boolean perform(AbstractBuild build, Launcher launcher,
			BuildListener listener) {

		List<Cause> buildStepCause = new ArrayList<Cause>();
		buildStepCause.add(new Cause() {
			public String getShortDescription() {
				return  rendering(HIGHT_LIGHT_BEGIN,"DBS(Server Builder) running....");
			}
		});
		if (!iswork) {
			logger(listener, rendering(RED ,"Server Builder Functional Not Activation，Will return!"));
			return true;
		}
		listener.started(buildStepCause); // 向jenkins控制台输出日志

		boolean result = executeShell(build, launcher, listener);
		if (!result) {
			return false;
		}
		
		suc(listener);

		return true;
	}

	private void suc(BuildListener listener) {
		logger(listener, "-------------------------------------------------------------------------------------------------");
		logger(listener, ".                                                                                               .");
		logger(listener, rendering(HIGHT_LIGHT_BEGIN, ".         DBS Builder FINISHED! (Any Question Please contact xieyong@dangdang.com)              ."));
		logger(listener, ".                                                                                               .");
		logger(listener, "-------------------------------------------------------------------------------------------------");
	}

	private boolean executeShell(AbstractBuild build, Launcher launcher,
			BuildListener listener) {
		String[] servers = serverList.split(",");
		logger(listener, rendering(HIGHT_LIGHT_BEGIN,"发布的服务数量:" + servers.length));
		//如果没有填写shell路径，则走默认bin/startup.sh
		String shellPath = "".equals(shell) ? PROJECT_STARTUP_SH : shell;
		if(shellExec){
			logger(listener, rendering(HIGHT_LIGHT_BEGIN, "项目发布到开发机后，会自动执行启动脚本"+shellPath));
		}
		if (launcher.isUnix()) {
			///home/d/tools/bin/dbs_tomcat.sh /home/d/jenkins/workspace/compete-web 10.255.209.112 /home/d/www/dbs target/compete-price-web bin/startup.sh true
			for (int i = 0; i < servers.length; i++) {
				ArgumentListBuilder args = new ArgumentListBuilder();
				if (RUN_MODE_TOMCAT.equals(runMode)) {
					args.add(DEPLOY_SHELL_TOMCAT);
				} else if (RUN_MODE_JAR.equals(runMode)) {
					args.add(DEPLOY_SHELL_JAR);
				} else {
					continue;
				}
				//add params
				args.add(build.getWorkspace());//$2
				args.add(servers[i]);//$3
				args.add(name);//$4
				args.add(scpFiles);//$5
				args.add(shellPath);
				args.add(shellExec);
				int r = 0;
				try {
					r = launcher.launch().cmds(args).stdout(listener).join();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (r != 0) {
					listener.finished(Result.FAILURE);
					return false;
				}

			}
		} else {
			logger(listener, rendering(HIGHT_LIGHT_BEGIN, "Windows not supported , Just support linux"));
			return false;
		}
		return true;
	}

	private void logger(BuildListener listener, String msg) {
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
	 * Descriptor for {@link DbsBuilder}. Used as a singleton. The class is
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
				@QueryParameter("serverList") String serverList,
				@QueryParameter("runMode") String runMode,
				@QueryParameter("name") String name,
				@QueryParameter("scpFiles") String scpFiles)
				throws IOException, ServletException {

			if (serverList.length() == 0) {
				return FormValidation.error("请输入服务器列表");
			}
			if (serverList.length() < 11)
				return FormValidation
						.warning("请输入服务器ip，例如：192.168.11.15,192.168.14.21");
			
			
			if(StringUtils.isEmpty(runMode)){
				return FormValidation.warning("目前支持tomcat和main方法运行，请输入tomcat或者jar");
			}
			if(!("tomcat".equals(runMode) || "jar".equals(runMode))){
				return FormValidation.error("目前只能为tomcat或者jar");
			}
			
			if (StringUtils.isEmpty(name)) {
				return FormValidation.error("部署的目标机器tomcat目录不能为空");
			}
			if (name.length() < 3){
				return FormValidation.warning("tomcat目录名称不少于3个字母");
			}
			
			if (StringUtils.isEmpty(scpFiles)) {
				return FormValidation.error("要复制的文件不能为空，可以为war或者目录");
			}
			if (scpFiles.length() < 3){
				return FormValidation.warning("文件名是否正确，请检查");
			}

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
			return "DBS(Server Builder)";
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
