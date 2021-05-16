package com.personal.archiver.gui;

import java.nio.file.Path;
import java.util.List;

import com.personal.archiver.gui.data.FileToArchive;
import com.utils.gui_utils.alerts.CustomAlertException;
import com.utils.gui_utils.objects.tables.tree_table.UnfilteredTreeItem;
import com.utils.io.PathUtils;
import com.utils.log.Logger;
import com.utils.string.StrUtils;

class ParserFilesToArchive {

	private final Path workingDirPath;
	private final UnfilteredTreeItem<FileToArchive> unfilteredTreeItemRoot;

	private FileToArchive fileToArchiveWorkingDir;

	ParserFilesToArchive(
			final Path workingDirPath,
			final UnfilteredTreeItem<FileToArchive> unfilteredTreeItemRoot) {

		this.workingDirPath = workingDirPath;
		this.unfilteredTreeItemRoot = unfilteredTreeItemRoot;
	}

	void work() {

		try {
			unfilteredTreeItemRoot.getChildrenList().clear();

			final String workingDirName = PathUtils.computeFileName(workingDirPath);
			fileToArchiveWorkingDir = new FileToArchive(workingDirName, workingDirPath, true);
			fileToArchiveWorkingDir.fillChildrenList();

			final List<FileToArchive> childrenList = fileToArchiveWorkingDir.getChildrenList();
			for (final FileToArchive fileToArchive : childrenList) {

				final UnfilteredTreeItem<FileToArchive> unfilteredTreeItem =
						new UnfilteredTreeItem<>(fileToArchive, true);
				unfilteredTreeItemRoot.getChildrenList().add(unfilteredTreeItem);
			}

		} catch (final Exception exc) {
			new CustomAlertException("error!",
					"failed to fill the file system tree items!", exc).showAndWait();
			Logger.printException(exc);
		}
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	FileToArchive getFileToArchiveWorkingDir() {
		return fileToArchiveWorkingDir;
	}
}
