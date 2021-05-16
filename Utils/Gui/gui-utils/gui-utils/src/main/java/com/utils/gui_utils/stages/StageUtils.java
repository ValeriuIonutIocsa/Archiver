package com.utils.gui_utils.stages;

import java.util.List;

import com.utils.annotations.ApiMethod;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

public final class StageUtils {

	private StageUtils() {
	}

	@ApiMethod
	public static void centerOnScreen(
			final Stage primaryStage) {

		final Screen screen = computeWidestScreen();

		final Rectangle2D screenBounds = screen.getBounds();
		primaryStage.setX(screenBounds.getMinX() + (screenBounds.getWidth() - primaryStage.getWidth()) / 2);
		primaryStage.setY(screenBounds.getMinY() + (screenBounds.getHeight() - primaryStage.getHeight()) / 2);
	}

	private static Screen computeWidestScreen() {

		Screen widestScreen = Screen.getPrimary();
		double maxScreenWidth = widestScreen.getVisualBounds().getWidth();

		final List<Screen> screens = Screen.getScreens();
		for (final Screen screen : screens) {

			final double screenWidth = screen.getVisualBounds().getWidth();
			if (screenWidth > maxScreenWidth) {

				widestScreen = screen;
				maxScreenWidth = screenWidth;
			}
		}
		return widestScreen;
	}
}
