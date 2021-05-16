package com.utils.gui_utils.objects.browse;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.utils.gui_utils.factories.BasicControlsFactory;
import com.utils.io.IoUtils;
import com.utils.io.PathUtils;

import javafx.stage.FileChooser;

public class HBoxBrowsePathFile extends HBoxBrowsePath {

	public enum Mode {
		OPEN, SAVE
	}

	private final Mode mode;
	private final List<FileChooser.ExtensionFilter> extensionFilters;

	public HBoxBrowsePathFile(
			final String name,
			final String initialValue,
			final Path initialDirectory,
			final Mode mode,
			final List<FileChooser.ExtensionFilter> extensionFilters) {

		super(name, initialValue, initialDirectory);

		this.mode = mode;
		this.extensionFilters = extensionFilters;
	}

	@Override
	String browsePath() {

		String pathString = null;

		final String name = getName();
		final String title = "Browse " + name + " file path:";
		final File initialDirectory = getInitialDirectory();
		final FileChooser fileChooser =
				BasicControlsFactory.createFileChooser(title, initialDirectory);

		if (extensionFilters != null) {

			fileChooser.getExtensionFilters().addAll(extensionFilters);

			final FileChooser.ExtensionFilter lastExtensionFilter =
					extensionFilters.get(extensionFilters.size() - 1);
			fileChooser.setSelectedExtensionFilter(lastExtensionFilter);
		}

		final File file;
		if (mode == Mode.OPEN) {
			file = fileChooser.showOpenDialog(getRoot().getScene().getWindow());
		} else if (mode == Mode.SAVE) {
			file = fileChooser.showSaveDialog(getRoot().getScene().getWindow());
		} else {
			file = null;
		}

		if (file != null) {
			pathString = file.getAbsolutePath();
		}

		return pathString;
	}

	@Override
	File getInitialDirectory() {

		File initialDirectory = null;
		final String pathString = getPathString();
		if (StringUtils.isNotBlank(pathString)) {
			final Path path = PathUtils.tryParsePath(null, pathString);
			if (path != null) {

				final Path parentPath = path.getParent();
				if (IoUtils.directoryExists(parentPath)) {
					initialDirectory = parentPath.toFile();
				}
			}
		}
		if (initialDirectory == null) {
			initialDirectory = super.getInitialDirectory();
		}
		return initialDirectory;
	}
}
