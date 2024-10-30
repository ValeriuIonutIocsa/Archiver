package com.personal.archiver.gui.workers;

import com.personal.archiver.gui.data.FileToArchive;
import com.utils.gui.objects.tables.tree_table.CustomTreeTableView;
import com.utils.gui.workers.ControlDisabler;

import javafx.scene.Scene;
import javafx.scene.control.TreeItem;

public class GuiWorkerCreateArchiveGui extends AbstractGuiWorkerCreateArchive {

	private final CustomTreeTableView<FileToArchive> customTreeTableView;
	private final boolean closeOnCompletion;

	public GuiWorkerCreateArchiveGui(
			final Scene scene,
			final ControlDisabler controlDisabler,
			final String workingDirPathString,
			final String outputPathString,
			final CustomTreeTableView<FileToArchive> customTreeTableView,
			final boolean cacheInRam,
			final boolean closeOnCompletion) {

		super(scene, controlDisabler, workingDirPathString, cacheInRam, outputPathString);

		this.customTreeTableView = customTreeTableView;
		this.closeOnCompletion = closeOnCompletion;
	}

	@Override
	FileToArchive createFileToArchiveRoot() {

		final TreeItem<FileToArchive> treeItemRoot = customTreeTableView.getRoot();
		return treeItemRoot.getValue();
	}

	@Override
	protected void finish() {

		super.finish();

		customTreeTableView.getScene().getRoot().requestFocus();

		if (closeOnCompletion) {
			getScene().getWindow().hide();
		}
	}
}
