package com.utils.gui_utils.factories;

import java.io.File;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.utils.annotations.ApiMethod;
import com.utils.gui_utils.clipboard.ClipboardUtils;
import com.utils.gui_utils.version.VersionDependentMethods;
import com.utils.io.PathUtils;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import io.github.palexdev.materialfx.controls.MFXToggleNode;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public final class BasicControlsFactory {

	public final static Duration TOOLTIP_SHOW_DELAY = new Duration(100);
	public final static Duration TOOLTIP_SHOW_TIME = new Duration(1_000_000);
	public final static Duration TOOLTIP_CLOSE_DELAY = new Duration(100);

	private BasicControlsFactory() {
	}

	@ApiMethod
	public static Tooltip createTooltip(
			final String text,
			final String... styleClassElements) {

		final Tooltip tooltip = VersionDependentMethods.createTooltip(text);
		if (styleClassElements != null) {
			tooltip.getStyleClass().addAll(styleClassElements);
		}
		return tooltip;
	}

	@ApiMethod
	public static Label createLabel(
			final String text,
			final String... styleClassElements) {

		final Label label = new Label(text);
		label.setMnemonicParsing(false);
		if (styleClassElements != null) {
			label.getStyleClass().addAll(styleClassElements);
		}
		label.setMinWidth(Region.USE_PREF_SIZE);
		label.setMinHeight(Region.USE_PREF_SIZE);
		return label;
	}

	@ApiMethod
	public static TextField createNumberOnlyTextField(
			final int value) {

		final TextField textField = createTextField(String.valueOf(value));
		textField.setOnKeyTyped(event -> {
			final String typedCharacter = event.getCharacter();
			if (!"0123456789".contains(typedCharacter)) {
				event.consume();
			}
		});
		return textField;
	}

	@ApiMethod
	public static TextField createReadOnlyTextField(
			final String text,
			final String... styleClassElements) {

		final TextField textField = createTextField(text);
		if (styleClassElements != null) {
			textField.getStyleClass().addAll(styleClassElements);
		}
		textField.getStylesheets().add("com/utils/gui_utils/factories/read_only_text.css");
		textField.getStyleClass().add("text-read-only");
		textField.setEditable(false);
		return textField;
	}

	@ApiMethod
	public static TextArea createReadOnlyTextArea(
			final String text,
			final String... styleClassElements) {

		final TextArea textArea = createTextArea(text);
		if (styleClassElements != null) {
			textArea.getStyleClass().addAll(styleClassElements);
		}
		textArea.getStylesheets().add("com/utils/gui_utils/factories/read_only_text.css");
		textArea.getStyleClass().add("text-read-only");
		textArea.setEditable(false);
		return textArea;
	}

	@ApiMethod
	public static TextField createTextField(
			final String text) {

		final TextField textField = new TextField(text);
		textField.setOnKeyPressed(event -> {
			if (event.isControlDown() && event.getCode() == KeyCode.C) {
				final String selectedText = textField.getSelectedText();
				if (StringUtils.isNotBlank(selectedText)) {
					ClipboardUtils.putStringInClipBoard(selectedText);
				}
				event.consume();
			}
		});
		return textField;
	}

	@ApiMethod
	public static TextArea createTextArea(
			final String text) {

		final TextArea textArea = new TextArea(text);
		textArea.setOnKeyPressed(event -> {
			if (event.isControlDown() && event.getCode() == KeyCode.C) {
				final String selectedText = textArea.getSelectedText();
				if (StringUtils.isNotBlank(selectedText)) {
					ClipboardUtils.putStringInClipBoard(selectedText);
				}
				event.consume();
			}
		});
		return textArea;
	}

	@ApiMethod
	public static Button createButton(
			final String text,
			final String... styleClassElements) {

		final Button button = new MFXButton(text);
		button.setMnemonicParsing(false);
		if (styleClassElements != null) {
			button.getStyleClass().addAll(styleClassElements);
		}
		button.setMinWidth(Region.USE_PREF_SIZE);
		button.setMinHeight(Region.USE_PREF_SIZE);
		return button;
	}

	@ApiMethod
	public static CheckBox createCheckBox(
			final String text,
			final String... styleClassElements) {

		final CheckBox checkBox = new MFXCheckbox(text);
		checkBox.setMnemonicParsing(false);
		if (styleClassElements != null) {
			checkBox.getStyleClass().addAll(styleClassElements);
		}
		checkBox.setMinWidth(Region.USE_PREF_SIZE);
		checkBox.setMinHeight(Region.USE_PREF_SIZE);
		return checkBox;
	}

	@ApiMethod
	public static ToggleButton createToggleButton(
			final String text,
			final String[] styleClassElements,
			final ToggleGroup toggleGroup) {

		final ToggleButton toggleButton = new MFXToggleButton();
		toggleButton.setMnemonicParsing(false);
		toggleButton.setText(text);
		if (styleClassElements != null) {
			toggleButton.getStyleClass().addAll(styleClassElements);
		}
		if (toggleGroup != null) {
			toggleButton.setToggleGroup(toggleGroup);
		}
		toggleButton.setMaxHeight(20);
		toggleButton.setPadding(new Insets(0));
		return toggleButton;
	}

	@ApiMethod
	public static ToggleButton createToggleNode(
			final String text,
			final String... styleClassElements) {

		final Label label = createLabel(text, styleClassElements);
		final MFXToggleNode mfxToggleNode = new MFXToggleNode(label);
		mfxToggleNode.setSize(24);
		return mfxToggleNode;
	}

	@ApiMethod
	public static Separator createSeparator(
			final Orientation orientation,
			final String... styleClassElements) {

		final Separator separator = new Separator(orientation);
		if (styleClassElements != null) {
			separator.getStyleClass().addAll(styleClassElements);
		}
		return separator;
	}

	@ApiMethod
	public static FileChooser createFileChooser(
			final String title) {
		return createFileChooser(title, null);
	}

	@ApiMethod
	public static FileChooser createFileChooser(
			final String title,
			final File initialDirectoryParam) {

		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(title);
		final File initialDirectory = createInitialDirectory(initialDirectoryParam);
		fileChooser.setInitialDirectory(initialDirectory);
		return fileChooser;
	}

	/**
	 * @param extension
	 *            case sensitive file extension (ex: xml, csv, html)
	 * @return a FileChooser ExtensionFilter that matches files of the given extension
	 */
	@ApiMethod
	public static FileChooser.ExtensionFilter createExtensionFilter(
			final String extension) {
		return new FileChooser.ExtensionFilter(
				extension.toUpperCase(Locale.US) + " file (*." + extension + ")", "*." + extension);
	}

	@ApiMethod
	public static FileChooser.ExtensionFilter createExtensionFilterAll() {
		return new FileChooser.ExtensionFilter("ALL files (*.*)", "*.*");
	}

	@ApiMethod
	public static DirectoryChooser createDirectoryChooser(
			final String title) {

		final File initialDirectory = new File(PathUtils.ROOT_PATH);
		return createDirectoryChooser(title, initialDirectory);
	}

	@ApiMethod
	public static DirectoryChooser createDirectoryChooser(
			final String title,
			final File initialDirectoryParam) {

		final DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle(title);
		final File initialDirectory = createInitialDirectory(initialDirectoryParam);
		directoryChooser.setInitialDirectory(initialDirectory);
		return directoryChooser;
	}

	private static File createInitialDirectory(
			final File initialDirectoryParam) {

		final File initialDirectory;
		if (initialDirectoryParam != null && initialDirectoryParam.exists()) {
			initialDirectory = initialDirectoryParam;
		} else {
			initialDirectory = new File(PathUtils.ROOT_PATH);
		}
		return initialDirectory;
	}
}
