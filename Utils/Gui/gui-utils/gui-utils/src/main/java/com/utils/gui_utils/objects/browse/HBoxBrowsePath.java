package com.utils.gui_utils.objects.browse;

import java.io.File;
import java.nio.file.Path;

import org.apache.commons.lang3.StringUtils;

import com.utils.gui_utils.CustomControlAbstr;
import com.utils.gui_utils.GuiUtils;
import com.utils.gui_utils.factories.BasicControlsFactory;
import com.utils.gui_utils.factories.LayoutControlsFactory;
import com.utils.io.PathUtils;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public abstract class HBoxBrowsePath extends CustomControlAbstr<HBox> {

	private final String name;

	private final TextField textFieldPath;

	private Path initialDirectoryPath;

	HBoxBrowsePath(
			final String name,
			final String initialValue,
			final Path initialDirectoryPath) {

		this.name = name;
		this.initialDirectoryPath = initialDirectoryPath;

		textFieldPath = BasicControlsFactory.createTextField("");
		if (StringUtils.isNotBlank(initialValue)) {
			textFieldPath.setText(initialValue);
		}
	}

	@Override
	protected HBox createRoot() {

		final HBox hBoxRoot = LayoutControlsFactory.createHBox();

		GuiUtils.addToHBox(hBoxRoot, textFieldPath,
				Pos.CENTER, Priority.ALWAYS, 7, 7, 7, 7);

		final Button buttonBrowse = BasicControlsFactory.createButton("...");
		buttonBrowse.setOnAction(event -> browse());
		GuiUtils.addToHBox(hBoxRoot, buttonBrowse,
				Pos.CENTER, Priority.NEVER, 7, 7, 7, 0);

		return hBoxRoot;
	}

	private void browse() {

		final String pathString = browsePath();
		if (StringUtils.isNotBlank(pathString)) {
			textFieldPath.setText(pathString);
		}
	}

	abstract String browsePath();

	File getInitialDirectory() {

		final File initialDirectory;
		if (initialDirectoryPath != null) {
			initialDirectory = initialDirectoryPath.toFile();
		} else {
			initialDirectory = new File(PathUtils.ROOT_PATH);
		}
		return initialDirectory;
	}

	public String getPathString() {
		return GuiUtils.computeTextInputControlPathString(textFieldPath);
	}

	String getName() {
		return name;
	}

	public void setInitialDirectoryPath(
			final Path initialDirectoryPath) {
		this.initialDirectoryPath = initialDirectoryPath;
	}

	public TextField getTextFieldPath() {
		return textFieldPath;
	}
}
