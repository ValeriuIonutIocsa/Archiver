package com.personal.archiver;

import com.personal.archiver.gui.ApplicationArchiver;
import com.utils.log.Logger;

import javafx.application.Application;

final class AppStartArchiver {

	private AppStartArchiver() {
	}

	public static void main(
			final String[] args) {

		Logger.setDebugMode(true);

		Application.launch(ApplicationArchiver.class, args);
	}
}
