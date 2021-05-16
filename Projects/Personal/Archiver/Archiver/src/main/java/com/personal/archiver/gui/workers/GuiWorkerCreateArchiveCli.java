package com.personal.archiver.gui.workers;

import java.nio.file.Path;
import java.util.ArrayList;

import com.personal.archiver.gui.data.FileToArchive;
import com.utils.gui_utils.workers.ComponentDisabler;
import com.utils.io.PathUtils;

import javafx.scene.Scene;

public class GuiWorkerCreateArchiveCli extends AbstractGuiWorkerCreateArchive {

	private final Path folderPath;

	public GuiWorkerCreateArchiveCli(
			final Scene scene,
			final ComponentDisabler componentDisabler,
			final Path workingDirPath,
			final Path outputPath,
			final Path folderPath) {

		super(scene, componentDisabler, workingDirPath, outputPath);

		this.folderPath = folderPath;
	}

	@Override
	FileToArchive createFileToArchiveRoot() {

		final String folderName = PathUtils.computeFileName(folderPath);
		final FileToArchive fileToArchive =
				new FileToArchive(folderName, folderPath, true);
		fileToArchive.setSelected(true);

		final Path parentFolderPath = folderPath.getParent();
		final String parentFolderName = PathUtils.computeFileName(parentFolderPath);
		final FileToArchive fileToArchiveRoot =
				new FileToArchive(parentFolderName, parentFolderPath, true);
		fileToArchiveRoot.setSelected(true);
		fileToArchiveRoot.setChildrenList(new ArrayList<>());
		fileToArchiveRoot.getChildrenList().add(fileToArchive);

		return fileToArchiveRoot;
	}

	@Override
	protected void finish() {

		super.finish();

		System.exit(0);
	}
}
