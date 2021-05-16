package com.utils.gui_utils.objects.browse;

import java.io.File;
import java.nio.file.Path;

import org.apache.commons.lang3.StringUtils;

import com.utils.gui_utils.factories.BasicControlsFactory;
import com.utils.io.IoUtils;
import com.utils.io.PathUtils;

import javafx.stage.DirectoryChooser;

public class HBoxBrowsePathFolder extends HBoxBrowsePath {

	public HBoxBrowsePathFolder(
			final String name,
			final String initialValue,
			final Path initialDirectory) {
		super(name, initialValue, initialDirectory);
	}

	@Override
	String browsePath() {

		String pathString = null;

		final String name = getName();
		final String title = "Browse " + name + " folder path:";
		final File initialDirectory = getInitialDirectory();
		final DirectoryChooser directoryChooser =
				BasicControlsFactory.createDirectoryChooser(title, initialDirectory);

		final File file = directoryChooser.showDialog(getRoot().getScene().getWindow());
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
			if (IoUtils.directoryExists(path)) {
				initialDirectory = path.toFile();
			}
		}
		if (initialDirectory == null) {
			initialDirectory = super.getInitialDirectory();
		}
		return initialDirectory;
	}
}
