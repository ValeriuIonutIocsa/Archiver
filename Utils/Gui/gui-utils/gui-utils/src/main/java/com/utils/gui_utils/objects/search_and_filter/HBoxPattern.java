package com.utils.gui_utils.objects.search_and_filter;

import org.apache.commons.lang3.StringUtils;

import com.utils.gui_utils.CustomControlAbstr;
import com.utils.gui_utils.GuiUtils;
import com.utils.gui_utils.factories.BasicControlsFactory;
import com.utils.gui_utils.factories.LayoutControlsFactory;
import com.utils.string.regex.custom_patterns.patterns.CustomPattern;
import com.utils.string.regex.custom_patterns.patterns.CustomPatternGlobRegex;
import com.utils.string.regex.custom_patterns.patterns.CustomPatternSimple;
import com.utils.string.regex.custom_patterns.patterns.CustomPatternUnixRegex;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

class HBoxPattern extends CustomControlAbstr<HBox> {

	private final TextField textFieldPattern;
	private final ToggleButton negateToggleButton;
	private final ToggleButton caseSensitiveToggleButton;

	HBoxPattern(
			final boolean focus,
			final EventHandler<ActionEvent> textFieldEventHandler) {

		textFieldPattern = createTextFieldPattern(focus, textFieldEventHandler);
		negateToggleButton = createNegateToggleButton();
		caseSensitiveToggleButton = createCaseSensitiveToggleButton();
	}

	private static TextField createTextFieldPattern(
			final boolean focus,
			final EventHandler<ActionEvent> textFieldEventHandler) {

		final TextField textFieldPattern = BasicControlsFactory.createTextField("");
		textFieldPattern.setMinWidth(60);
		if (focus) {
			Platform.runLater(textFieldPattern::requestFocus);
		}
		textFieldPattern.setOnAction(textFieldEventHandler);
		return textFieldPattern;
	}

	private static ToggleButton createNegateToggleButton() {

		final ToggleButton negateToggleButton =
				BasicControlsFactory.createToggleNode(" ! ", "bold");
		negateToggleButton.setTooltip(BasicControlsFactory.createTooltip(
				"negate the pattern (show only what doesn't match)"));
		GuiUtils.setDefaultBorder(negateToggleButton);
		return negateToggleButton;
	}

	private static ToggleButton createCaseSensitiveToggleButton() {

		final ToggleButton caseSensitiveToggleButton =
				BasicControlsFactory.createToggleNode("Aa", "bold");
		caseSensitiveToggleButton.setTooltip(
				BasicControlsFactory.createTooltip("case sensitive pattern"));
		GuiUtils.setDefaultBorder(caseSensitiveToggleButton);
		return caseSensitiveToggleButton;
	}

	@Override
	protected HBox createRoot() {

		final HBox hBoxRoot = LayoutControlsFactory.createHBox();

		GuiUtils.addToHBox(hBoxRoot, textFieldPattern,
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 0, 0, 0);

		GuiUtils.addToHBox(hBoxRoot, negateToggleButton,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 3, 0, 3);

		GuiUtils.addToHBox(hBoxRoot, caseSensitiveToggleButton,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 0);

		return hBoxRoot;
	}

	CustomPattern createPattern(
			final int patternTypeIndex) {

		CustomPattern customPattern = null;
		final String pattern = textFieldPattern.getText();
		if (StringUtils.isNotBlank(pattern)) {

			final boolean negate = negateToggleButton.isSelected();
			final boolean caseSensitive = caseSensitiveToggleButton.isSelected();
			if (patternTypeIndex == 0) {
				customPattern = new CustomPatternSimple(pattern, negate, caseSensitive);
			} else if (patternTypeIndex == 1) {
				customPattern = new CustomPatternGlobRegex(pattern, negate, caseSensitive);
			} else if (patternTypeIndex == 2) {
				customPattern = new CustomPatternUnixRegex(pattern, negate, caseSensitive);
			}
		}
		return customPattern;
	}
}
