package com.personal.archiver.gui;

import java.nio.file.Path;
import java.util.List;

import com.personal.archiver.gui.res.ImagesArchiver;
import com.utils.app_info.FactoryAppInfo;
import com.utils.gui_utils.GuiUtils;
import com.utils.gui_utils.alerts.CustomAlertConfirm;
import com.utils.gui_utils.stages.StageUtils;
import com.utils.gui_utils.styles.vitesco.VitescoStyleUtils;
import com.utils.io.PathUtils;
import com.utils.log.Logger;

import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class WindowMainArchiver extends Application {

	@Override
	public void start(
			final Stage primaryStage) {

		final String appTitleAndVersion = FactoryAppInfo.getInstance().getAppTitleAndVersion();
		final String title;
		if (Logger.isDebugMode()) {
            title = appTitleAndVersion + " (debug mode)";
        } else {
            title = appTitleAndVersion;
        }
		primaryStage.setTitle(title);
		primaryStage.setWidth(700);
		primaryStage.setHeight(500);
		primaryStage.setMinWidth(500);
		primaryStage.setMinHeight(300);
		StageUtils.centerOnScreen(primaryStage);
		GuiUtils.setAppIcon(primaryStage, ImagesArchiver.createImageApp());

		final VBoxArchiver vBoxArchiver = new VBoxArchiver();
		final Scene scene = new Scene(vBoxArchiver.getRoot());
		VitescoStyleUtils.configureVitescoStyle(
				scene, "com/personal/archiver/gui/style_archiver.css");
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
		if (parameterList.size() >= 2) {

			final String folderPathString = parameterList.get(0);
			Path folderPath = PathUtils.tryParsePath("folder path", folderPathString);
			if (folderPath != null) {

				folderPath = folderPath.toAbsolutePath();

				final String outputPathString = parameterList.get(1);
				Path outputPath = PathUtils.tryParsePath("output path", outputPathString);
				if (outputPath != null) {

					outputPath = outputPath.toAbsolutePath();
					vBoxArchiver.createArchive(folderPath, outputPath);
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
