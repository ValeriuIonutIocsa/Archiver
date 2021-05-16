package com.personal.archiver.gui;

import java.util.List;

import com.personal.archiver.gui.data.FileToArchive;
import com.utils.gui_utils.GuiUtils;
import com.utils.gui_utils.objects.tables.tree_table.CustomTreeTableView;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableRow;

class CustomTreeTableRowFileToArchive extends TreeTableRow<FileToArchive> {

	CustomTreeTableRowFileToArchive(
			final CustomTreeTableView<FileToArchive> customTreeTableView) {

		setOnMousePressed(mouseEvent -> {

			if (GuiUtils.isDoubleClick(mouseEvent)) {

				final TreeItem<FileToArchive> treeItem = getTreeItem();
				if (treeItem != null) {

					final FileToArchive fileToArchive = treeItem.getValue();
					if (fileToArchive != null) {

						final boolean folder = fileToArchive.isFolder();
						if (folder) {

							List<FileToArchive> childrenList = fileToArchive.getChildrenList();
							if (childrenList == null) {
								fileToArchive.fillChildrenList();
								childrenList = fileToArchive.getChildrenList();
							}
							for (final FileToArchive fileToArchiveChild : childrenList) {

								final TreeItem<FileToArchive> treeItemChild =
										new TreeItem<>(fileToArchiveChild);
								treeItemChild.setExpanded(true);
								treeItem.expandedProperty().addListener((
										observable,
										oldValue,
										newValue) -> customTreeTableView.fillTreeItemList());
								treeItem.getChildren().add(treeItemChild);
							}
							customTreeTableView.fillTreeItemList();
						}
					}
				}
			}
		});
	}
}
