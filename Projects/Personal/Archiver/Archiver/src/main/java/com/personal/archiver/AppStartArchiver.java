package com.personal.archiver;

import com.personal.archiver.gui.WindowMainArchiver;
import com.utils.app_info.FactoryAppInfo;
import com.utils.log.Logger;

import javafx.application.Application;

final class AppStartArchiver {

	private AppStartArchiver() {
	}

	public static void main(
			final String[] args) {

		Logger.setDebugMode(true);

		FactoryAppInfo.initialize("Archiver", "2.0.0");

		Application.launch(WindowMainArchiver.class, args);
	}
}
