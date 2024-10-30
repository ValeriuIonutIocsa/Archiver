package com.personal.archiver.gui;

import java.util.List;

import com.personal.archiver.gui.res.ImagesArchiver;
import com.utils.app_info.AppInfo;
import com.utils.app_info.FactoryAppInfo;
import com.utils.gui.GuiUtils;
import com.utils.gui.alerts.CustomAlertConfirm;
import com.utils.gui.stages.StageUtils;
import com.utils.gui.styles.StyleUtils;
import com.utils.io.PathUtils;
import com.utils.log.Logger;

import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class ApplicationArchiver extends Application {

	@Override
	public void start(
			final Stage primaryStage) {

		final AppInfo appInfo = FactoryAppInfo.computeInstance("Archiver", "3.0.0");
		final String appTitleAndVersion = appInfo.getAppTitleAndVersion();
		final String title;
		if (Logger.isDebugMode()) {
			title = appTitleAndVersion + " (debug mode)";
		} else {
			title = appTitleAndVersion;
		}
		primaryStage.setTitle(title);
		primaryStage.setWidth(1_000);
		primaryStage.setHeight(700);
		primaryStage.setMinWidth(500);
		primaryStage.setMinHeight(300);
		StageUtils.centerOnScreen(primaryStage);
		GuiUtils.setAppIcon(primaryStage, ImagesArchiver.createImageApp());

		final VBoxArchiver vBoxArchiver = new VBoxArchiver();
		final Scene scene = new Scene(vBoxArchiver.getRoot());
		StyleUtils.configureStyle(
				scene, "com/personal/archiver/gui/style_archiver.css", null);
		primaryStage.setScene(scene);

		primaryStage.setOnShown(event -> shown(scene, vBoxArchiver));
		primaryStage.setOnCloseRequest(event -> close(vBoxArchiver));

		primaryStage.show();
	}

	private void shown(
			final Scene scene,
			final VBoxArchiver vBoxArchiver) {

		scene.getRoot().requestFocus();

		final Parameters parameters = getParameters();
		final List<String> parameterList = parameters.getRaw();
		String folderPathString;
		if (!parameterList.isEmpty()) {

			folderPathString = parameterList.getFirst();
			folderPathString = PathUtils.computeNormalizedPath("folder path", folderPathString);
			vBoxArchiver.configureFolderPathString(folderPathString);

		} else {
			folderPathString = null;
		}

		if (parameterList.size() >= 2) {

			if (folderPathString != null) {

				String outputPathString = parameterList.get(1);

				boolean cacheInRam = true;
				if (parameterList.size() >= 3) {

					final String cacheInRamString = parameterList.get(2);
					cacheInRam = Boolean.parseBoolean(cacheInRamString);
				}

				outputPathString = PathUtils.computeNormalizedPath("output path", outputPathString);
				if (outputPathString != null) {

					vBoxArchiver.createArchive(folderPathString, cacheInRam, outputPathString);
				}
			}
		}
	}

	private static void close(
			final VBoxArchiver vBoxArchiver) {

		final Scene scene = vBoxArchiver.getRoot().getScene();
		final Cursor cursor = scene.getCursor();
		if (cursor == Cursor.WAIT) {

			final CustomAlertConfirm customAlertConfirm = new CustomAlertConfirm(
					"Are you sure you wish to exit?",
					"There is a task running. Are you sure you wish to stop it and exit?",
					ButtonType.NO, ButtonType.YES);
			customAlertConfirm.showAndWait();

			final ButtonType resultButtonType = customAlertConfirm.getResult();
			if (resultButtonType == ButtonType.YES) {
				vBoxArchiver.close();
				System.exit(0);
			}

		} else {
			System.exit(0);
		}
	}
}
