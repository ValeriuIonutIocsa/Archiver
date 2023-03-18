package com.utils.gui.objects;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;

import com.jfoenix.controls.JFXProgressBar;
import com.utils.gui.AbstractCustomControl;
import com.utils.gui.GuiUtils;
import com.utils.gui.factories.LayoutControlsFactories;
import com.utils.io.ResourceFileUtils;
import com.utils.io.WriterUtils;
import com.utils.log.Logger;
import com.utils.string.StrUtils;
import com.utils.string.replacements.StringReplacements;
import com.utils.string.replacements.StringReplacementsRegular;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class LevelGauge extends AbstractCustomControl<StackPane> {

	private final String tempFolderPathString;
	private final double level;
	private final int digitCount;
	private final boolean validLevel;
	private final Color fullSpaceColor;
	private final Color emptySpaceColor;

	public LevelGauge(
			final String tempFolderPathString,
			final double level,
			final int digitCount,
			final boolean validLevel,
			final Color fullSpaceColor,
			final Color emptySpaceColor) {

		this.tempFolderPathString = tempFolderPathString;
		this.level = level;
		this.digitCount = digitCount;
		this.validLevel = validLevel;
		this.fullSpaceColor = fullSpaceColor;
		this.emptySpaceColor = emptySpaceColor;
	}

	@Override
	protected StackPane createRoot() {

		final StackPane stackPaneRoot = LayoutControlsFactories.getInstance().createStackPane(Pos.CENTER);

		final Label labelProgress = new Label(StrUtils.doubleToPercentageString(level, digitCount));
		final String style;
		if (validLevel) {
			style = "-fx-font-weight: bold";
		} else {
			style = "-fx-font-weight: bold; -fx-text-fill: red";
		}
		labelProgress.setStyle(style);

		final ProgressBar progressBar = new JFXProgressBar();
		createCustomStyle(tempFolderPathString, progressBar, fullSpaceColor, emptySpaceColor);
		progressBar.setProgress(level);
		progressBar.setMinHeight(20);

		stackPaneRoot.getChildren().setAll(progressBar, labelProgress);

		return stackPaneRoot;
	}

	private static void createCustomStyle(
			final String tempFolderPathString,
			final ProgressBar progressBar,
			final Color fullSpaceColor,
			final Color emptySpaceColor) {

		final String styleClass = "level-gauge-" + fullSpaceColor + "-" + emptySpaceColor;
		progressBar.getStyleClass().remove(2, progressBar.getStyleClass().size());
		progressBar.getStyleClass().add(styleClass);

		try {
			final String styleCssFileContentTemplate = ResourceFileUtils.resourceFileToString(
					"com/utils/gui/objects/style_level_gauge_template.txt");
			if (StringUtils.isBlank(styleCssFileContentTemplate)) {
				throw new Exception();
			}

			final StringReplacements stringReplacements = new StringReplacementsRegular();
			stringReplacements.addReplacement("@@style_class@@", styleClass);
			stringReplacements.addReplacement("@@full_color@@",
					GuiUtils.getFxBackgroundColorString(fullSpaceColor));
			stringReplacements.addReplacement("@@empty_color@@",
					GuiUtils.getFxBackgroundColorString(emptySpaceColor));
			final String styleCssFileContent =
					stringReplacements.performReplacements(styleCssFileContentTemplate);

			final File styleCssFile = new File(tempFolderPathString, styleClass + ".css");
			final String styleCssFilePathString = styleCssFile.getAbsolutePath();
			WriterUtils.stringToFile(styleCssFileContent, StandardCharsets.UTF_8, styleCssFilePathString);

			progressBar.getStylesheets().clear();
			progressBar.getStylesheets().add(styleCssFile.toURI().toURL().toExternalForm());

			styleCssFile.deleteOnExit();

		} catch (final Exception exc) {
			Logger.printError("failed to create custom style");
			Logger.printException(exc);
		}
	}
}
