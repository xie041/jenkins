package com.dangdang.dbs;

import hudson.Extension;
import hudson.FilePath;
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

import javax.servlet.ServletException;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import com.dangdang.dbs.utils.ShellUtils;

import static com.dangdang.dbs.utils.AnsiColor.*;

/**
 * Sample {@link Builder}.
 * 
 * <p>
 * When the user configures the project and enables this builder,
 * {@link DescriptorImpl#newInstance(StaplerRequest)} is invoked and a new
 * {@link DbsChecker} is created. The created instance is persisted to the
 * project configuration XML by using XStream, so this allows you to use
 * instance fields (like {@link #name}) to remember the configuration.
 * 
 * <p>
 * When a build is performed, the
 * {@link #perform(AbstractBuild, Launcher, BuildListener)} method will be
 * invoked.
 * 
 * @author xieyong
 */
public class DbsChecker extends Builder {

//	private static final String HELP_URL = "如有疑问，请访问："
//			+ BACKGROUND_YELLOW
//			+ "http://twiki.dangdang.com/twiki/bin/view.pl/Tech/Help_9"
//			+ END + " 来解决Maven冲突";
	private static final String HELP_URL = new StringBuilder()
			.append("如有疑问，请访问：")
			.append(BACKGROUND_YELLOW)
			.append("http://twiki.dangdang.com/twiki/bin/view.pl/Tech/Help_9")
			.append(END)
			.append(" 来解决Maven冲突").toString();

	private final boolean duplicate;
	private final boolean conflict;
	private final boolean snapshot;

	// Fields in config.jelly must match the parameter names in the
	// "DataBoundConstructor"
	@DataBoundConstructor
	public DbsChecker(boolean duplicate, boolean conflict, boolean snapshot) {
		this.duplicate = duplicate;
		this.conflict = conflict;
		this.snapshot = snapshot;
	}

	/**
	 * We'll use this from the <tt>config.jelly</tt>.
	 */
	public boolean getDuplicate() {
		return duplicate;
	}

	public boolean getConflict() {
		return conflict;
	}

	public boolean getSnapshot() {
		return snapshot;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean perform(AbstractBuild build, Launcher launcher,
			BuildListener listener) {

		List<Cause> buildStepCause = new ArrayList<Cause>();
		buildStepCause.add(new Cause() {
			public String getShortDescription() {
				return rendering(HIGHT_LIGHT_BEGIN, "DBS(Maven Pom Checker) running....");
			}
		});
		listener.started(buildStepCause); // 向jenkins控制台输出日志

		String pom = getPom(build);
		FilePath workspace = build.getWorkspace();
		listener.getLogger().println(workspace);

		File f = new File(pom);
		if (!f.exists()) {
			logger(listener, pom + "文件不存在");
			return false;
		}

		if (conflict) {
			boolean result = getConflict(pom, listener);
			if (!result) {
				return false;
			}
		}

		if (duplicate) {
			boolean result = checkDuplicate(pom, listener);
			if (!result) {
				return false;
			}
		}

		if (snapshot) {
			boolean result = checkSnapshotDependency(pom, listener);
			if (!result) {
				return false;
			}
		}

		suc(listener);

		return true;
	}

	private void suc(BuildListener listener) {
		logger(listener,"-------------------------------------------------------------------------------------------------");
		logger(listener,".                                                                                               .");
		logger(listener,rendering(HIGHT_LIGHT_BEGIN,"     DBS POM CHECK FINISHED! (Any Question Please contact xieyong@dangdang.com)         ."));
		logger(listener,".                                                                                               .");
		logger(listener,"-------------------------------------------------------------------------------------------------");
	}

	private void logger(BuildListener listener, String msg) {
		listener.getLogger().println(msg);
	}

	private boolean getConflict(String pom, BuildListener listener) {
		logger(listener,rendering(HIGHT_LIGHT_BEGIN,"开始检测maven依赖冲突..."));
		StringBuilder builder = new StringBuilder("mvn -f ");
		builder.append(pom);
		builder.append(" -U dependency:tree -Dverbose | grep conflict");
		String result = ShellUtils.runShell(builder.toString(), listener);
		logger(listener, rendering(RED,result));
		if (result != null && result.contains("conflict")) {
			logger(listener, rendering(HIGHT_LIGHT_BEGIN,"有Maven依赖冲突，请解决后再发布,") + HELP_URL);
			return false;
		}
		logger(listener, rendering(HIGHT_LIGHT_BEGIN,"恭喜！没有发现类冲突"));
		return true;
	}

	private boolean checkDuplicate(String pom, BuildListener listener) {
		logger(listener,rendering(HIGHT_LIGHT_BEGIN,"开始检测maven依赖重复类..."));
		StringBuilder builder = new StringBuilder("mvn -f ");
		builder.append(pom);
		builder.append(" -U clean package -Dmaven.test.skip=true enforcer:enforce -DcheckDeployRelease_skip=true");
		builder.append(" | grep -100 Duplicate");

		String result = ShellUtils.runShell(builder.toString(), listener);
		logger(listener, result);
		if (result != null && result.contains("Duplicate")) {
			logger(listener, rendering(HIGHT_LIGHT_BEGIN,"发现重复类，请解决后再发布,") + HELP_URL);
			return false;
		}
		logger(listener,rendering(HIGHT_LIGHT_BEGIN,"恭喜，没有发现重复类"));
		return true;
	}

	private boolean checkSnapshotDependency(String pom, BuildListener listener) {
		logger(listener,rendering(HIGHT_LIGHT_BEGIN,"开始检测maven依赖是否包含snapshot版本..."));
		StringBuilder builder = new StringBuilder("cat ");
		builder.append(pom);
		builder.append(" | grep -3 \"SNAPSHOT\" ");
		String result = ShellUtils.runShell(builder.toString(), listener);
		logger(listener, result);
		if (StringUtils.isNotEmpty(result)) {
			logger(listener,rendering(HIGHT_LIGHT_BEGIN,"发现Snapshot版本依赖，请解决后再发布"));
			return false;
		}
		logger(listener,rendering(HIGHT_LIGHT_BEGIN,"恭喜！没有发现Snapshot版本依赖"));
		return true;
	}

	private String getPom(AbstractBuild build) {
		return build.getWorkspace() + File.separator + "pom.xml";
	}

	// Overridden for better type safety.
	// If your plugin doesn't really define any property on Descriptor,
	// you don't have to do this.
	@Override
	public DescriptorImpl getDescriptor() {
		return (DescriptorImpl) super.getDescriptor();
	}

	/**
	 * Descriptor for {@link DbsChecker}. Used as a singleton. The class is
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
		public FormValidation doCheckName() throws IOException,
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
			return "DBS(Maven Pom Checker)";
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
