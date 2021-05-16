package com.utils.gui;

import java.util.ArrayList;
import java.util.List;

import org.testfx.framework.junit5.ApplicationTest;

import com.utils.gui_utils.GuiUtils;
import com.utils.gui_utils.factories.LayoutControlsFactory;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

abstract class AbstractCustomApplicationTest extends ApplicationTest {

	protected static final int LONG_WAIT_TIME = 1_000_000_000;

	private final Image imageApp;

	private StackPane stackPaneContainer;

	AbstractCustomApplicationTest(
			final Image imageApp) {

		this.imageApp = imageApp;
	}

	@Override
	public void start(
			final Stage primaryStage) {

		primaryStage.setOnShown(event -> showSecondaryStage());
		primaryStage.show();
	}

	private void showSecondaryStage() {

		final Stage stage = new Stage();
		stage.setTitle("Test Application");
		GuiUtils.setAppIcon(stage, imageApp);

		stackPaneContainer = LayoutControlsFactory.createStackPane(Pos.CENTER_LEFT);
		final Scene scene = new Scene(stackPaneContainer, 1280, 800);

		final List<String> stylesheetList = new ArrayList<>();
		fillStylesheetList(stylesheetList);
		scene.getStylesheets().addAll(stylesheetList);

		stage.setScene(scene);
		stage.setOnShown(event -> scene.getRoot().requestFocus());
		stage.setOnCloseRequest(event -> System.exit(0));
		stage.show();
	}

	protected abstract void fillStylesheetList(
			List<String> stylesheetList);

	protected StackPane getStackPaneContainer() {
		return stackPaneContainer;
	}
}
