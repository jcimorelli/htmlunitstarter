package net.htmlunitstarter.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.env.Environment;

@Configurable
public class HtmlUnitStarterProperties {

	@Autowired
	private Environment environment;

	private static final HtmlUnitStarterProperties instance = new HtmlUnitStarterProperties();

	public static String getDbHost() {
		return instance.environment.getProperty("dbHost");
	}

	public static String getDbUser() {
		return instance.environment.getProperty("dbUser");
	}

	public static String getDbPassword() {
		return instance.environment.getProperty("dbPassword");
	}

	public static Boolean isLogTrace() {
		return Boolean.valueOf(instance.environment.getProperty("logTrace"));
	}

	public static Boolean isLogDebug() {
		return Boolean.valueOf(instance.environment.getProperty("logDebug"));
	}

	public static String getSiteUsername() {
		return instance.environment.getProperty("siteUsername");
	}

	public static String getSitePassword() {
		return instance.environment.getProperty("sitePassword");
	}

	public static String getSmtpUsername() {
		return instance.environment.getProperty("smtpUsername");
	}

	public static String getSmtpPassword() {
		return instance.environment.getProperty("smtpPassword");
	}

	public static String getSmtpHost() {
		return instance.environment.getProperty("smtpHost");
	}

	public static String getSmtpPort() {
		return instance.environment.getProperty("smtpPort");
	}

	public static String getAdminEmail() {
		return instance.environment.getProperty("adminEmail");
	}

}