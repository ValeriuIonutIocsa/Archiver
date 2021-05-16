package com.personal.archiver.gui.workers;

import java.nio.file.Path;

import com.personal.archiver.gui.data.FileToArchive;
import com.utils.gui_utils.objects.tables.tree_table.CustomTreeTableView;
import com.utils.gui_utils.workers.ComponentDisabler;

import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TreeItem;

public class GuiWorkerCreateArchiveGui extends AbstractGuiWorkerCreateArchive {

	private final CustomTreeTableView<FileToArchive> customTreeTableView;
	private final CheckBox checkBoxCloseOnCompletion;

	public GuiWorkerCreateArchiveGui(
			final Scene scene,
			final ComponentDisabler componentDisabler,
			final Path workingDirPath,
			final Path outputPath,
			final CustomTreeTableView<FileToArchive> customTreeTableView,
			final CheckBox checkBoxCloseOnCompletion) {

		super(scene, componentDisabler, workingDirPath, outputPath);

		this.customTreeTableView = customTreeTableView;
		this.checkBoxCloseOnCompletion = checkBoxCloseOnCompletion;
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

		final boolean closeOnCompletion = checkBoxCloseOnCompletion.isSelected();
		if (closeOnCompletion) {
			getScene().getWindow().hide();
		}
	}
}
