package com.personal.archiver.gui.workers;

import java.util.ArrayList;

import com.personal.archiver.gui.data.FileToArchive;
import com.utils.gui.workers.ControlDisabler;
import com.utils.io.PathUtils;

import javafx.scene.Scene;

public class GuiWorkerCreateArchiveCli extends AbstractGuiWorkerCreateArchive {

	private final String folderPathString;

	public GuiWorkerCreateArchiveCli(
			final Scene scene,
			final ControlDisabler controlDisabler,
			final String workingDirPathString,
			final boolean cacheInRam,
			final String outputPathString,
			final String folderPathString) {

		super(scene, controlDisabler, workingDirPathString, cacheInRam, outputPathString);

		this.folderPathString = folderPathString;
	}

	@Override
	FileToArchive createFileToArchiveRoot() {

		final String folderName = PathUtils.computeFileName(folderPathString);
		final FileToArchive fileToArchive =
				new FileToArchive(folderName, folderPathString, true);
		fileToArchive.setSelected(true);

		final String parentFolderPathString = PathUtils.computeParentPath(folderPathString);
		final String parentFolderName = PathUtils.computeFileName(parentFolderPathString);
		final FileToArchive fileToArchiveRoot =
				new FileToArchive(parentFolderName, parentFolderPathString, true);
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
