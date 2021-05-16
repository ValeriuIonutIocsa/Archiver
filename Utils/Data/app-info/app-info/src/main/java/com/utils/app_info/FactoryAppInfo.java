package com.utils.app_info;

import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.commons.lang3.StringUtils;

import com.utils.annotations.ApiMethod;
import com.utils.string.StrUtils;

public final class FactoryAppInfo {

	private static AppInfo instance;

	private FactoryAppInfo() {
	}

	@ApiMethod
	public static void initialize(
			final String appTitleDefault,
			final String appVersionDefault) {

		String appTitle;
		final boolean defaultTitle;
		final Package classPackage = AppInfo.class.getPackage();
		appTitle = classPackage.getImplementationTitle();
		if (StringUtils.isNotBlank(appTitle)) {
			defaultTitle = false;
		} else {
			appTitle = appTitleDefault;
			defaultTitle = true;
		}
		if (StringUtils.isNotBlank(appTitle)) {
			appTitle = formatTitle(appTitle);
		} else {
			appTitle = "";
		}

		String appVersion = classPackage.getImplementationVersion();
		final boolean defaultVersion;
		if (StringUtils.isNotBlank(appVersion)) {
			defaultVersion = false;
		} else {
			appVersion = appVersionDefault;
			defaultVersion = true;
		}

		String buildTime = null;
		final boolean defaultBuildTime;
		try {
			final URLClassLoader urlClassLoader =
					(URLClassLoader) Thread.currentThread().getContextClassLoader();
			final URL manifestUrl = urlClassLoader.findResource("META-INF/MANIFEST.MF");
			try (InputStream inputStream = manifestUrl.openStream()) {

				final Manifest manifest = new Manifest(inputStream);
				final Attributes mainAttributes = manifest.getMainAttributes();
				buildTime = mainAttributes.getValue("Build-Time");
			}

		} catch (final Exception ignored) {
		}
		if (StringUtils.isNotBlank(buildTime)) {
			defaultBuildTime = true;

		} else {
			buildTime = StrUtils.createDisplayDateTimeString();
			defaultBuildTime = false;
		}

		instance = new AppInfo(
				appTitle, defaultTitle, appVersion, defaultVersion, buildTime, defaultBuildTime);
	}

	static String formatTitle(
			final String appTitle) {

		final StringBuilder stringBuilder = new StringBuilder();
		char lastCh = 0;
		for (int i = 0; i < appTitle.length(); i++) {

			final char ch = appTitle.charAt(i);
			if (i > 0 && Character.isUpperCase(ch) && !Character.isUpperCase(lastCh)) {
				stringBuilder.append(' ');
			}
			if (Character.isLetterOrDigit(ch)) {
				stringBuilder.append(ch);
			}
			lastCh = ch;
		}
		return stringBuilder.toString();
	}

	@ApiMethod
	public static AppInfo getInstance() {
		return instance;
	}
}
